/*
 * Copyright 2014 Toshiaki Kamoshida
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jpservant.google.module.gcs;

import static com.jpservant.core.common.Constant.RequestMethod.*;
import static com.jpservant.core.common.JacksonUtils.*;
import static com.jpservant.core.common.Utilities.*;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.jpservant.core.common.AccessController;
import com.jpservant.core.common.Constant.FileExtern;
import com.jpservant.core.common.Constant.RequestMethod;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.exception.ApplicationException;
import com.jpservant.core.exception.ApplicationException.ErrorType;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.google.Constant;

/**
 *
 * Google Cloud Storageモジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class GCSModulePlatform implements ModulePlatform {

	private static final GcsService GCS_SERVICE =
			GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

	private ModuleConfiguration config;

	@Override
	public void initialize(ModuleConfiguration config) {

		this.config = config;
	}

	@Override
	public void execute(KernelContext context) throws Exception {

		if (!AccessController.checkAccessMethod(context, GET, POST, PUT, DELETE)) {
			throw new ApplicationException(ErrorType.BadRequest);
		}
		String bucket = this.config.getValue(Constant.ConfigurationName.BucketName);
		String path = createResourcePath(this.config, context, FileExtern.JSON);
		RequestMethod method = RequestMethod.valueOf(context.getMethod());
		GcsFilename name = new GcsFilename(bucket, path);

		if (method == GET) {
			readFile(name, context);
		} else if (method == POST) {
			writeFile(name, context);
		} else if (method == PUT) {
			appendFile(name, context);
		} else if (method == DELETE) {
			deleteFile(name);
		}
	}

	/**
	 *
	 * ファイル読み取り。
	 *
	 * @param file ファイル
	 * @param context コンテキスト
	 * @throws Exception 何らかの例外発生
	 */
	private static void readFile(GcsFilename file, KernelContext context) throws Exception {

		GcsInputChannel channel = null;
		try {
			synchronized (GCS_SERVICE) {
				channel = GCS_SERVICE.openReadChannel(file, 0L);
			}

			String content = loadChannel(channel);
			context.writeResponse(toDataCollection(content));

		} catch (Exception e) {
			throw new ApplicationException(ErrorType.NotFound);
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 *
	 * ファイル書き込み。
	 *
	 * @param file ファイル
	 * @param context コンテキスト
	 * @throws Exception 何らかの例外発生
	 */
	private static void writeFile(GcsFilename file, KernelContext context) throws Exception {

		GcsOutputChannel channel = null;
		try {
			synchronized (GCS_SERVICE) {
				channel = GCS_SERVICE.createOrReplace(file,
						GcsFileOptions.getDefaultInstance());
			}
			String content = toJSONString(context.getParameters());
			saveChannel(channel, content);

		} catch (Exception e) {
			throw new ApplicationException(ErrorType.BadRequest);
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 *
	 * ファイル追記。
	 *
	 * @param file ファイル
	 * @param context コンテキスト
	 * @throws Exception 何らかの例外発生
	 */
	private static void appendFile(GcsFilename file, KernelContext context) throws Exception {

		GcsInputChannel inchannel = null;
		GcsOutputChannel outchannel = null;
		try {
			synchronized (GCS_SERVICE) {
				inchannel = GCS_SERVICE.openReadChannel(file, 0L);
			}

			String content = loadChannel(inchannel);
			DataCollection data = toDataCollection(content);

			inchannel.close();

			synchronized (GCS_SERVICE) {
				outchannel = GCS_SERVICE.createOrReplace(file,
						GcsFileOptions.getDefaultInstance());
			}

			data.addAll(context.getParameters());
			content = toJSONString(data);
			saveChannel(outchannel, content);

		} catch (Exception e) {
			throw new ApplicationException(ErrorType.NotFound);
		} finally {
			if (outchannel != null) {
				try {
					outchannel.close();
				} catch (Exception e) {
				}
			}
			if (inchannel != null && inchannel.isOpen()) {
				try {
					inchannel.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 *
	 * ファイル削除。
	 *
	 * @param file ファイル
	 * @throws Exception 何らかの例外発生
	 *
	 */
	private static void deleteFile(GcsFilename file) throws Exception {

		synchronized (GCS_SERVICE) {
			if (!GCS_SERVICE.delete(file)) {
				throw new ApplicationException(ErrorType.NotFound);
			}
		}

	}

}

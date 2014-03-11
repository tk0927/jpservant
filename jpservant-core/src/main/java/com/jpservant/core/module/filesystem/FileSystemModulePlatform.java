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
package com.jpservant.core.module.filesystem;

import static com.jpservant.core.common.Constant.RequestMethod.*;
import static com.jpservant.core.common.JacksonUtils.*;
import static com.jpservant.core.common.Utilities.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileLock;

import com.jpservant.core.common.AccessController;
import com.jpservant.core.common.Constant;
import com.jpservant.core.common.Constant.FileExtern;
import com.jpservant.core.common.Constant.RequestMethod;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.exception.ApplicationException;
import com.jpservant.core.exception.ApplicationException.ErrorType;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;

/**
 *
 * ファイルシステムI/Oモジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class FileSystemModulePlatform implements ModulePlatform {

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
		String path = createResourcePath(this.config, context, FileExtern.JSON);
		File file = getFile(path);
		RequestMethod method = RequestMethod.valueOf(context.getMethod());

		if (method == GET) {
			readFile(file, context);
		} else if (method == POST) {
			writeFile(file, context, false);
		} else if (method == PUT) {
			writeFile(file, context, true);
		} else if (method == DELETE) {
			deleteFile(file);
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
	private static void readFile(File file, KernelContext context) throws Exception {

		FileInputStream in = null;
		FileLock fl = null;
		try {
			in = new FileInputStream(file);
			fl = in.getChannel().lock(0, Long.MAX_VALUE, true);
			String content = loadStream(in);
			context.writeResponse(toDataObjectList(content));
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.NotFound);
		} finally {
			if (fl != null) {
				try {
					fl.release();
				} catch (Exception e) {
				}
			}

			if (in != null) {
				try {
					in.close();
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
	 * @param append 追加モード（trueなら追加書き込み）
	 * @throws Exception 何らかの例外発生
	 */
	private static void writeFile(File file, KernelContext context, boolean append) throws Exception {

		FileOutputStream fs = null;
		BufferedWriter writer = null;
		FileLock fl = null;
		try {
			fs = new FileOutputStream(file, append);

			try {
				fl = fs.getChannel().lock(0, Long.MAX_VALUE, false);
			} catch (Exception e) {
				throw new ApplicationException(ErrorType.InternalError,
						String.format("File resource is busy %s", file.getAbsolutePath()));
			}

			writer = new BufferedWriter(
					new OutputStreamWriter(fs, Constant.ENCODE_NAME));

			for (DataObject element : context.getParameters()) {

				String content = toJSONString(element);
				writer.write(content);

			}

		} finally {

			if (fl != null) {
				try {
					fl.release();
				} catch (Exception e) {
				}
			}

			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					if (fs != null) {
						try {
							fs.close();
						} catch (Exception e2) {
						}
					}
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
	private static void deleteFile(File file) throws Exception {

		if (!file.exists()) {
			throw new ApplicationException(ErrorType.NotFound,
					String.format("Not exists file:%s", file.getAbsolutePath()));
		}
		if (!file.delete()) {
			throw new ApplicationException(ErrorType.BadRequest,
					String.format("Disable to delete file:%s", file.getAbsolutePath()));
		}

	}

	/**
	 *
	 * ファイルオブジェクトの生成。
	 * ディレクトリパスが無ければ、併せて生成する。
	 *
	 * @param path ファイルパス文字列
	 * @return ファイルオブジェクト
	 * @throws Exception 当該ファイル名のファイル生成不可
	 */
	private static synchronized File getFile(String path) throws Exception {

		File file = new File(path);
		File dir = file.getParentFile();

		if (dir.exists()) {
			if (!dir.isDirectory()) {
				throw new ApplicationException(ErrorType.NotFound,
						String.format("Already exists file:%s", path));
			}
		} else {
			if (!dir.mkdirs()) {
				throw new ApplicationException(ErrorType.NotFound,
						String.format("Disable to create directory:%s", dir.getAbsolutePath()));
			}
		}

		return file;

	}

}

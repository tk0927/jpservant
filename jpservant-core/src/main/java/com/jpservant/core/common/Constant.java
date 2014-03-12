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
package com.jpservant.core.common;

import com.jpservant.core.resolver.ClassPathResolver;
import com.jpservant.core.resolver.FileSystemResolver;
import com.jpservant.core.resolver.ResourceResolver;
import com.jpservant.core.resolver.ServletContextResolver;

/**
 *
 * 共通定数定義。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface Constant {

	/**
	 * エンコード名。常時UTF-8。
	 */
	String ENCODE_NAME = "UTF-8";

	/**
	 * Servletの初期化パラメータ名
	 */
	String SERVLET_INIT_CONFIG_NAME = "config";

	/**
	 *
	 * ファイル拡張子。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static enum FileExtern {

		/** JSONファイル */
		JSON(".json"),
		/** SQLファイル */
		SQL(".sql"), ;

		private String ext;

		private FileExtern(String ext) {
			this.ext = ext;
		}

		public String toString() {
			return this.ext;
		}
	}

	/**
	 *
	 * Httpリクエストメソッド名。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static enum RequestMethod {

		/** GET */
		GET,
		/** POST */
		POST,
		/** PUT */
		PUT,
		/** DELETE */
		DELETE,

		/** 内部用ダミーMethod名 */
		DEFAULT,
	}

	/**
	 *
	 * 設定情報名。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static enum ConfigurationName {

		/** モジュールインスタンスの識別名 兼 モジュールへのディスパッチ対象REST URLパストークン */
		RootPath(true),
		/** モジュールのプラットフォームクラス名 */
		PlatformClass(true),
		/** モジュールがリソースを検索する方法の指定 */
		ResourceType(false),
		/** モジュールがリソースを検索する位置指定 */
		ResourceRoot(false),
		/** モジュールが利用するJDBCリソースの識別名 */
		JDBCResourcePath(false),
		/** （DAOモジュール用）スキーマ名 */
		SchemaName(false),
		/**  (Google Cloud Storageモジュール用)バケット名 */
		BucketName(false);

		private boolean required;

		private ConfigurationName(boolean required) {
			this.required = required;
		}

		public boolean isRequired() {
			return this.required;
		}
	}

	/**
	 *
	 * リソース種別。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static enum ResourceType {

		/** クラスパスから解決する */
		ClassPath(ClassPathResolver.class),
		/** サーブレットコンテクストから解決する */
		ServletContext(ServletContextResolver.class),
		/** ファイルシステムから解決する */
		FileSystem(FileSystemResolver.class),

		;

		Class<? extends ResourceResolver> clazz;

		private ResourceType(Class<? extends ResourceResolver> clazz) {
			this.clazz = clazz;
		}

		public ResourceResolver createResolverInstance() {
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
	}

}

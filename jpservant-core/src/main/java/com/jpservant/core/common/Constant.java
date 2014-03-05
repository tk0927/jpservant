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
	String ENCODE_NAME ="UTF-8";

	/**
	 * Servletの初期化パラメータ名
	 */
	String SERVLET_INIT_CONFIG_NAME ="config";

	/**
	 *
	 * Httpリクエストメソッド名。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static enum RequestMethod{
		GET,
		POST,
		PUT,
		DELETE,
	}

	/**
	 *
	 * 設定情報名。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static enum ConfigurationName{
		RootPath(true),
		PlatformClass(true),
		ResourceType(false),
		ResourceRoot(false),
		JDBCResourcePath(false),
		;

		private boolean required;

		private ConfigurationName(boolean required){
			this.required = required;
		}

		public boolean isRequired(){
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
	public static enum ResourceType{

		/** クラスパスから解決する */
		ClassPath (ClassPathResolver.class),
		/** サーブレットコンテクストから解決する */
		ServletContext (ServletContextResolver.class),
		/** ファイルシステムから解決する */
		FileSystem (FileSystemResolver.class),

		;

		Class<? extends ResourceResolver> clazz;

		private ResourceType(Class<? extends ResourceResolver> clazz){
			this.clazz = clazz;
		}

		public ResourceResolver getInstance(){
			try{
				return clazz.newInstance();
			}catch(Exception e){
				throw new RuntimeException();
			}
		}
	}

}

package com.jpservant.core.common;

import com.jpservant.core.kernel.ResourceResolver;
import com.jpservant.core.kernel.impl.ClassPathResolver;
import com.jpservant.core.kernel.impl.ServletContextResolver;

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
	 * 設定情報名。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static enum ConfigurationName{
		RootPath(true),
		PlatformClass(true),
		ResourceType(true),
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

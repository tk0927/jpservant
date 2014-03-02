package com.jpservant.core.common;

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
		/** クラスパス内から解決する */
		ClassPathResource,
	}

}

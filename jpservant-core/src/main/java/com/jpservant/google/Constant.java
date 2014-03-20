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
package com.jpservant.google;

/**
 *
 * 共通定数定義。(Googleモジュール専用)
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface Constant {

	/**
	 *
	 * 設定情報名(Google固有)。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static enum ConfigurationName {

		/**  (Google Cloud Storageモジュール用)バケット名 */
		BucketName(false),

		/** (Google Client Coreモジュール用)アプリケーション名 */
		ApplicationName(false),
		OAuthClientName(false),
		OAuthClientSecret(false),
		OAuthScope(false),
		OAuthRedirectURI(false),

		;
		private boolean required;

		private ConfigurationName(boolean required) {
			this.required = required;
		}

		public boolean isRequired() {
			return this.required;
		}
	}

}

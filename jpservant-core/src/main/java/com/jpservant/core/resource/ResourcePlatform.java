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
package com.jpservant.core.resource;

import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * リソースのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface ResourcePlatform {

	/**
	 *
	 * 初期化処理を実装する位置。
	 *
	 * @param config 設定情報
	 */
	void initialize(ModuleConfiguration config);

	/**
	 *
	 * 固有の管理対象リソースオブジェクトを提供する。
	 *
	 * @return 管理対象リソースオブジェクト
	 *
	 */
	Object getResource();
}

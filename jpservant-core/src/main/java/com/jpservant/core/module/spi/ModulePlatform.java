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
package com.jpservant.core.module.spi;

import com.jpservant.core.exception.ConfigurationException;
import com.jpservant.core.kernel.KernelContext;

/**
 *
 * モジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface ModulePlatform {

	/**
	 *
	 * 初期化処理を実装する位置。
	 *
	 * @param config 設定情報
	 * @throws ConfigurationException 設定誤り
	 */
	void initialize(ModuleConfiguration config)throws ConfigurationException;

	/**
	 *
	 * リクエストの処理を行う位置。
	 *
	 * @param context コンテキスト情報
	 * @throws Exception 何らかの例外発生
	 */
	void execute(KernelContext context)throws Exception;

}

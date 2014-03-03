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
package com.jpservant.core.kernel;

import java.util.ArrayList;

import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.core.resource.ResourcePlatform;

/**
 *
 * システムの設定情報を格納し、各機能に提供するクラスのインターフェイス定義。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface ConfigurationManager {

	/**
	 *
	 * 設定ファイル内の"RootPath"設定リストを得る。
	 *
	 * @return "RootPath"設定リスト
	 */
	public abstract ArrayList<String> getRootPathList();

	/**
	 *
	 * RootPath値に紐づくモジュール向け設定情報を得る。
	 *
	 * @param path RootPath値
	 * @return モジュール向け設定情報
	 */
	public abstract ModuleConfiguration getModuleConfiguration(String path);

	/**
	 *
	 * RootPath値に紐づく{@link ModulePlatform}インスタンスを得る。
	 *
	 * @param path RootPath値
	 * @return インスタンス
	 */
	public abstract ModulePlatform getModulePlatform(String path);

	/**
	 *
	 * RootPath値に紐づく{@link ResourcePlatform}インスタンスを得る。
	 *
	 * @param path RootPath値
	 * @return インスタンス
	 */
	public abstract ResourcePlatform getResourcePlatform(String path);

}
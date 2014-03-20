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

import java.util.HashMap;

import com.jpservant.core.common.Constant;
import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.kernel.ConfigurationManager;
import com.jpservant.core.resource.ResourcePlatform;

/**
 *
 * モジュール単位の設定情報の集合。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class ModuleConfiguration extends HashMap<String, Object> {

	/** 管理オブジェクトへの参照 */
	private ConfigurationManager manager;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param manager 管理オブジェクトへの参照
	 */
	public ModuleConfiguration(ConfigurationManager manager) {
		this.manager = manager;
	}

	/**
	 *
	 * 管理オブジェクトへの参照を得る。
	 *
	 * @return 管理オブジェクトへの参照
	 */
	public ConfigurationManager getConfigurationManager() {
		return this.manager;
	}

	/**
	 *
	 * 管理するデータベース接続を取得します。
	 *
	 * @param name パラメータ名
	 * @return データベース接続
	 */
	public DatabaseConnectionHolder findJDBCConnection(Constant.ConfigurationName name) {

		ResourcePlatform resource = getConfigurationManager().getResourcePlatform(getValue(name));
		return resource == null ? null : (DatabaseConnectionHolder) resource.getResource();

	}

	/**
	 *
	 * 設定情報 属性値を取得します。
	 *
	 * @param name 属性名
	 * @return 属性値
	 */
	public String getValue(Enum<?> name) {
		return (String) get(name.name());
	}
}

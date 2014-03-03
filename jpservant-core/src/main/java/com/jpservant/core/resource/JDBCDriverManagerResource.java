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

import com.jpservant.core.common.sql.impl.DriverManagerConnectionHolder;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * JDBC DriverManagerに基づくデータベースコネクションを所有するリソースクラス。
 *
 * <pre>
 * リソースを要求されるたびに、設定情報を利用してDB接続を行う。
 * </pre>
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class JDBCDriverManagerResource implements ResourcePlatform {

	public static enum ConfigurationName{
		JDBCDriver,
		JDBCURL,
		JDBCUser,
		JDBCPassword,
	}

	private ModuleConfiguration config;

	@Override
	public void initialize(ModuleConfiguration config) {
		this.config = config;
	}

	@Override
	public Object getResource() {

		return new DriverManagerConnectionHolder(
				(String)config.get(ConfigurationName.JDBCDriver.name()),
				(String)config.get(ConfigurationName.JDBCURL.name()),
				(String)config.get(ConfigurationName.JDBCUser.name()),
				(String)config.get(ConfigurationName.JDBCPassword.name())
			);

	}

}

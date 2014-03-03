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
package com.jpservant.test.miscellaneous;

import com.jpservant.core.common.sql.impl.DriverManagerConnectionHolder;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.resource.ResourcePlatform;

/**
 *
 * JDBC DriverManagerに基づくデータベースコネクションを所有するリソースクラス。
 *
 * <pre>
 * JVMの稼働期間中、同一コネクションを継続して利用する。
 * </pre>
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class TestJDBCDriverManagerResource implements ResourcePlatform {

	public static enum ConfigurationName{
		JDBCDriver,
		JDBCURL,
		JDBCUser,
		JDBCPassword,
	}

	private DriverManagerConnectionHolder impl;

	@Override
	public void initialize(ModuleConfiguration config) {

		this.impl = new DriverManagerConnectionHolder(
				(String)config.get(ConfigurationName.JDBCDriver.name()),
				(String)config.get(ConfigurationName.JDBCURL.name()),
				(String)config.get(ConfigurationName.JDBCUser.name()),
				(String)config.get(ConfigurationName.JDBCPassword.name())
			);

	}

	@Override
	public Object getResource() {
		return this.impl;
	}

}

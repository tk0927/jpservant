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

import static com.jpservant.core.common.Constant.ConfigurationName.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.common.sql.SQLProcessor;
import com.jpservant.core.common.sql.SQLProcessorTest;
import com.jpservant.core.kernel.impl.ConfigurationManagerImpl;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.resource.JDBCDriverManagerResource;
import com.jpservant.core.resource.ResourcePlatform;
import com.jpservant.test.miscellaneous.TestCaseBase;

/**
*
* {@link ConfigurationManagerImpl}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class ConfigurationManagerTest extends TestCaseBase {

	/**
	 *
	 * src/test/resources/configurations/root.jsonの読み取りテスト
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void testConfigurationLoading() throws Exception {

		ModuleConfiguration sqlconfig = MANAGER.getModuleConfiguration("/sql");

		assertEquals("/sql", sqlconfig.get(RootPath.name()));
		assertEquals("com.jpservant.core.module.sql.QueryModulePlatform", sqlconfig.get(PlatformClass.name()));
		assertEquals("ClassPath", sqlconfig.get("ResourceType"));
		assertEquals("/module/sql", sqlconfig.get("ResourceRoot"));

		ModuleConfiguration daoconfig = MANAGER.getModuleConfiguration("/dao");

		assertEquals("/dao", daoconfig.get(RootPath.name()));
		assertEquals("com.jpservant.core.module.dao.DAOModulePlatform", daoconfig.get(PlatformClass.name()));

	}

	/**
	 *
	 * src/test/resources/configurations/root.jsonの設定値に基づく{@link JDBCDriverManagerResource}の初期化/稼働テスト
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void testJDBCDriverManagerResourceExecution() throws Exception {

		ResourcePlatform resource = MANAGER.getResourcePlatform("/JDBCResource1");
		DatabaseConnectionHolder holder = (DatabaseConnectionHolder) resource.getResource();

		//テスト用のSQL実行
		SQLProcessorTest.executeTestSQL(new SQLProcessor(holder));

	}
}

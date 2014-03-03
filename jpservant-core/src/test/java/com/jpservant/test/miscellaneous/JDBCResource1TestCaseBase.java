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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.kernel.ConfigurationManagerTest;
import com.jpservant.core.kernel.impl.ConfigurationManagerImpl;
import com.jpservant.core.resource.ResourcePlatform;

/**
 *
 * JDBCを利用するテストの共通基底クラス。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class JDBCResource1TestCaseBase {


	protected static ConfigurationManagerImpl MANAGER;

	/**
	 *
	 * 初期化
	 * @throws Exception
	 *
	 */
	@BeforeClass
	public static void beforeClass()throws Exception {

		MANAGER = new ConfigurationManagerImpl();
		MANAGER.initialize(ConfigurationManagerTest.class.getResource("/configurations/root.json"));

	}
	/**
	 *
	 * 初期化
	 * @throws Exception
	 *
	 */
	@AfterClass
	public static void afterClass()throws Exception {

		ResourcePlatform resource = MANAGER.getResourcePlatform("/JDBCResource1");
		DatabaseConnectionHolder holder = (DatabaseConnectionHolder)resource.getResource();
		if(holder.isConnected()){
			holder.close();
		}

	}


	/**
	 *
	 * テストケースごとのトランザクション巻き戻し
	 *
	 * @throws Exception
	 */
	@After
	public void after()throws Exception {

		ResourcePlatform resource = MANAGER.getResourcePlatform("/JDBCResource1");
		DatabaseConnectionHolder holder = (DatabaseConnectionHolder)resource.getResource();
		if(holder.isConnected()){
			holder.rollback();
		}

	}

}

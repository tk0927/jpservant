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

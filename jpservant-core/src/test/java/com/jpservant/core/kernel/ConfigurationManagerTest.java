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
import com.jpservant.test.miscellaneous.JDBCResource1TestCaseBase;

/**
*
* {@link ConfigurationManagerImpl}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class ConfigurationManagerTest  extends JDBCResource1TestCaseBase {

	/**
	 *
	 * src/test/resources/configurations/root.jsonの読み取りテスト
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void testConfigurationLoading()throws Exception {

		ModuleConfiguration sqlconfig = MANAGER.getModuleConfiguration("/sql");

		assertEquals("/sql", sqlconfig.get(RootPath.name()));
		assertEquals("com.jpservant.core.module.sql.QueryModulePlatform", sqlconfig.get(PlatformClass.name()));
		assertEquals("ClassPathResource", sqlconfig.get("ResourceType"));
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
	public void testJDBCDriverManagerResourceExecution() throws Exception{

		ResourcePlatform resource = MANAGER.getResourcePlatform("/JDBCResource1");
		DatabaseConnectionHolder holder = (DatabaseConnectionHolder)resource.getResource();

		//テスト用のSQL実行
		SQLProcessorTest.executeTestSQL(new SQLProcessor(holder));

	}
}

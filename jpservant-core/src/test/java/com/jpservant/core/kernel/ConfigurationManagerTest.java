package com.jpservant.core.kernel;

import static com.jpservant.core.common.Constant.ConfigurationName.*;
import static junit.framework.Assert.*;

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

		assertEquals(sqlconfig.get(RootPath.name()),"/sql");
		assertEquals(sqlconfig.get(PlatformClass.name()),"com.jpservant.core.module.sql.QueryModulePlatform");
		assertEquals(sqlconfig.get("ResourceType"),"ClassPathResource");
		assertEquals(sqlconfig.get("ResourceRoot"),"/module/sql");

		ModuleConfiguration daoconfig = MANAGER.getModuleConfiguration("/dao");

		assertEquals(daoconfig.get(RootPath.name()),"/dao");
		assertEquals(daoconfig.get(PlatformClass.name()),"com.jpservant.core.module.dao.DAOModulePlatform");

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

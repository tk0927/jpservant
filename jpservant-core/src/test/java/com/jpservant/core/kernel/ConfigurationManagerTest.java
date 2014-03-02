package com.jpservant.core.kernel;

import static com.jpservant.core.common.Constant.ConfigurationName.*;
import static junit.framework.Assert.*;

import org.junit.Test;

import com.jpservant.core.module.spi.ModuleConfiguration;

/**
*
* {@link ConfigurationManager}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class ConfigurationManagerTest {

	@Test
	public void testConfigurationLoading()throws Exception {

		ConfigurationManager manager = new ConfigurationManager(getClass().getResource("/configurations/root.json"));

		ModuleConfiguration sqlconfig = manager.getModuleConfiguration("/sql");

		assertEquals(sqlconfig.get(ModuleRootPath.name()),"/sql");
		assertEquals(sqlconfig.get(PlatformClass.name()),"com.jpservant.core.module.sql.SQLModulePlatform");
		assertEquals(sqlconfig.get("ResourceType"),"ClassPathResource");
		assertEquals(sqlconfig.get("ResourceRoot"),"/module/sql");

		ModuleConfiguration daoconfig = manager.getModuleConfiguration("/dao");

		assertEquals(daoconfig.get(ModuleRootPath.name()),"/dao");
		assertEquals(daoconfig.get(PlatformClass.name()),"com.jpservant.core.module.dao.DAOModulePlatform");

	}

}

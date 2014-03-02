package com.jpservant.core.resource;

import com.jpservant.core.common.sql.impl.DriverManagerConnectionHolder;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * JDBC DriverManagerに基づくデータベースコネクションを所有するリソースクラス。
 *
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

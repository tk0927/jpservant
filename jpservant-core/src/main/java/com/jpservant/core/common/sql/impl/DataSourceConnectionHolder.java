package com.jpservant.core.common.sql.impl;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * J2EE {@link DataSource}から得られるデータベース接続を保管するクラス。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class DataSourceConnectionHolder extends AbstractDatabaseConnectionHolder {

	/** DataSourceのJNDI名 */
	private String jndiname;

	/** DataSource */
	private DataSource datasource;


	/**
	 *
	 * コンストラクタ
	 *
	 * @param jndiname リソースのJNDIバインド名
	 */
	public DataSourceConnectionHolder(String jndiname){
		this.jndiname =jndiname;
	}

	@Override
	public synchronized Connection connectImpl() throws Exception {

		if(this.datasource == null){
			InitialContext ctx = new InitialContext();
			this.datasource = (DataSource)ctx.lookup(jndiname);
		}
		return this.datasource.getConnection();
	}

	@Override
	public void releaseSession() throws Exception {
		super.commit();
	}
}

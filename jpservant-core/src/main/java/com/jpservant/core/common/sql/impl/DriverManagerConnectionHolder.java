package com.jpservant.core.common.sql.impl;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * {@link DriverManager}から得られる、旧来のJDBCデータベース接続を保管するクラス。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class DriverManagerConnectionHolder extends AbstractDatabaseConnectionHolder {

	/** DataSourceのJNDI名 */
	private String drivername;
	private String url;
	private String username;
	private String password;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param drivername JDBCドライバ名
	 * @param url URL
	 * @param username ユーザー名
	 * @param password パスワード
	 */
	public DriverManagerConnectionHolder(String drivername,String url,String username,String password){

		this.drivername = drivername;
		this.url = url;
		this.username = username;
		this.password = password;

	}

	@Override
	public synchronized Connection connectImpl() throws Exception {

		Class.forName(this.drivername);
		return DriverManager.getConnection(this.url, this.username, this.password);
	}

	@Override
	public void releaseSession() throws Exception {
		super.commit();
		super.close();
	}


}

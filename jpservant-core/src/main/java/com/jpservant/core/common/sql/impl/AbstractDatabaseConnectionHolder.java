package com.jpservant.core.common.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.jpservant.core.common.sql.DatabaseConnectionHolder;

/**
 *
 * データベース接続保管クラスの共通インプリメンテーション。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public abstract class AbstractDatabaseConnectionHolder implements DatabaseConnectionHolder {

	/** 接続状態フラグ */
	private boolean isconnected = false;

	/** JDBC コネクション */
	private Connection connection;


	@Override
	public synchronized void connect() throws SQLException {

		if(this.isconnected){
			throw new SQLException("Already connected.");
		}

		try{

			this.connection = connectImpl();
			this.connection.setAutoCommit(false);
			this.isconnected = true;

		}catch(Exception e){
			throw new SQLException(e);
		}

	}

	/**
	 *
	 * 正味の接続処理を実装する位置
	 *
	 * @return データベース接続
	 * @throws Exception 何らかの例外発生
	 */
	public abstract Connection connectImpl() throws Exception;

	@Override
	public synchronized Connection getConnection() {
		return this.connection;
	}

	@Override
	public synchronized void close() throws SQLException {

		if(!this.isconnected){
			throw new SQLException("Already closed.");
		}
		try{
			this.connection.close();
		}finally{
			this.isconnected = false;
		}
	}

	@Override
	public boolean isConnected() {
		return this.isconnected;
	}

}

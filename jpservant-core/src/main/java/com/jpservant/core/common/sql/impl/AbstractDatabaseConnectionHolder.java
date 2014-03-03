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
package com.jpservant.core.common.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

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
	 * 派生クラスが正味のデータベース接続処理を実装する位置。
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

		if(!this.isConnected()){
			throw new SQLException("Not connected.");
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

	@Override
	public void commit()throws SQLException{

		if(!this.isConnected()){
			throw new SQLException("Not connected.");
		}
		this.connection.commit();

	}

	@Override
	public void rollback()throws SQLException{

		if(!this.isConnected()){
			throw new SQLException("Not connected.");
		}
		this.connection.rollback();

	}

	@Override
	public Savepoint getSavepoint() throws SQLException{

		if(!this.isConnected()){
			throw new SQLException("Not connected.");
		}
		return this.connection.setSavepoint();

	}


	@Override
	public void rollbackSavepoint(Savepoint s)throws SQLException{

		if(!this.isConnected()){
			throw new SQLException("Not connected.");
		}
		this.connection.rollback(s);

	}

	@Override
	public void releaseSavepoint(Savepoint s)throws SQLException{

		if(!this.isConnected()){
			throw new SQLException("Not connected.");
		}
		this.connection.releaseSavepoint(s);

	}

}

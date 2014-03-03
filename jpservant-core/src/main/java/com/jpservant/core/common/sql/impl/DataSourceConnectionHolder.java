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
		rollback();
	}
}

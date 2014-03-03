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
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

/**
*
* {@link DataCollection}を行データ集合として取り扱い、
* バルク更新処理を行うSQL DML実行オブジェクト。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class DataCollectionDMLExecutor extends DMLExecutor {

	/** バインドパラメータ集合リスト */
	private DataCollection parameterrows;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL文
	 * @param conn データベースコネクション
	 */
	public DataCollectionDMLExecutor(String sql,Connection conn){
		this(sql, conn, null);
	}

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL文
	 * @param conn データベースコネクション
	 * @param parameterrows バインドパラメータ
	 */
	public DataCollectionDMLExecutor(String sql,Connection conn,DataCollection parameterrows){
		super(new BindParameterParsedSQL(sql),conn);
		this.parameterrows = parameterrows;
	}

	/**
	 * 型の取り扱いが面倒なので、パラメータがあればすべてString型でバインドする。
	 */
	@Override
	public void bindParameters(PreparedStatement ps) throws SQLException {

		if(this.parameterrows != null){

			ParameterMetaData pmd = ps.getParameterMetaData();

			for(DataObject row : this.parameterrows){
				for(Map.Entry<String, Object> entry : row.entrySet()){
					getBindParameterParsedSQL().bind(
							ps, pmd, entry.getKey(), entry.getValue());
				}
				ps.addBatch();
			}
		}else{
			ps.addBatch();
		}
	}

}

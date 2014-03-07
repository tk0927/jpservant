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

import static com.jpservant.core.common.Utilities.*;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

/**
 *
 * {@link DataObject}をバインドパラメータ集合として取り扱い、
 * 検索結果を{@link DataCollection}に格納して返却するSQL Query実行オブジェクト。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class DataObjectSQLExecutor extends SQLExecutor<DataCollection> {

	/** バインドパラメータ */
	private DataObject parameters;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL文
	 * @param conn データベースコネクション
	 */
	public DataObjectSQLExecutor(String sql,Connection conn){
		this(sql,conn,null);
	}

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL文
	 * @param conn データベースコネクション
	 * @param parameters バインドパラメータ
	 */
	public DataObjectSQLExecutor(String sql,Connection conn,DataObject parameters){

		super(new BindParameterParsedSQL(sql),conn);
		this.parameters = parameters;

	}

	/**
	 * 型の取り扱いが面倒なので、パラメータがあればすべてString型でバインドする。
	 */
	@Override
	public void bindParameters(PreparedStatement ps) throws SQLException {

		if(this.parameters != null){

			ParameterMetaData pmd = ps.getParameterMetaData();

			for(Map.Entry<String, Object> entry : this.parameters.entrySet()){
				getBindParameterParsedSQL().bind(
						ps, pmd, entry.getKey(), entry.getValue());
			}
		}

	}

	/**
	 * 型の取り扱いが面倒なので、すべてString型でフェッチする。
	 */
	@Override
	public DataCollection readResultSet(ResultSet rs) throws SQLException {

		DataCollection result = new DataCollection();
		ResultSetMetaData rsm = rs.getMetaData();
		while(rs.next()){

			DataObject row = new DataObject();
			for(int i = 1 ; i <= rsm.getColumnCount() ; i++){
				row.put(convertSnakeToCamel(rsm.getColumnName(i)),rs.getString(i));
			}

			result.add(row);

		}
		return result;
	}
}

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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

/**
 *
 * {@link DatabaseMetaData}操作のためのユーティリティメソッド。
 *
 * @author Toshiaki.Kamoshida <kamoshida.toshiaki@gmail.com>
 * @version 0.1
 *
 */
public class DatabaseMetaDataUtils {

	/**
	 *
	 *  {@link DatabaseMetaData}操作の定型処理を隠蔽するクラス。
	 *
	 *
	 *
	 * @author Toshiaki.Kamoshida <kamoshida.toshiaki@gmail.com>
	 * @version 0.1
	 *
	 */
	private abstract static class AccessProcessor {

		/** ResultSetからの抽出対象カラム名リスト */
		private HashSet<String> accepts = new HashSet<String>();

		/**
		 *
		 * コンストラクタ。
		 *
		 * @param accepts ResultSetからの抽出対象カラム名リスト
		 */
		private AccessProcessor(String... accepts){
			this.accepts.addAll(Arrays.asList(accepts));
		}

		/**
		 *
		 * メソッドの戻り値から得られるResultSetを全件読み取って、DataCollectionに格納して返す。
		 *
		 * @return ResultSetから抽出された全データ
		 * @throws SQLException SQL例外発生
		 */
		public DataCollection read() throws SQLException {

			ResultSet rs = null;
			try {
				rs = execute();
				ResultSetMetaData rsm = rs.getMetaData();
				DataCollection retvalue = new DataCollection();

				while (rs.next()) {

					DataObject row = new DataObject();

					for (int i = 1; i <= rsm.getColumnCount(); i++) {
						String colname = convertSnakeToCamel(rsm.getColumnName(i));
						if(this.accepts.contains(colname)){
							row.put(colname, rs.getString(i));
						}
					}

					retvalue.add(row);

				}

				return retvalue;

			} finally {
				if (rs != null) {
					rs.close();
				}
			}
		}

		/**
		 *
		 * {@link DatabaseMetaData}のメソッド呼び出しを実装する位置。
		 *
		 * @return DatabaseMetaDataのメソッド戻り値であるResultSet
		 * @throws SQLException SQL例外発生
		 */
		public abstract ResultSet execute() throws SQLException;
	}

	/**
	 *
	 * 検索処理可能なテーブル名一覧を得る。
	 *
	 * @param dmd DatabaseMetaData
	 * @param schema 対象スキーマ名
	 * @return 検索結果
	 * @throws SQLException SQL例外発生
	 */
	public static DataCollection getSelectableTableNames(
			final DatabaseMetaData dmd,final String schema) throws SQLException{

		return new AccessProcessor("TableName","TableType"){
			@Override
			public ResultSet execute() throws SQLException {
				return dmd.getTables(null, schema, "%", new String[]{"TABLE","VIEW"});
			}
		}.read();

	}

	/**
	 *
	 * テーブルのカラム名一覧を得る。
	 *
	 * @param dmd DatabaseMetaData
	 * @param schema 対象スキーマ名
	 * @return 検索結果
	 * @throws SQLException SQL例外発生
	 */
	public static DataCollection getColumnNames(
			final DatabaseMetaData dmd,final String schema) throws SQLException{

		return new AccessProcessor("ColumnName","TableName","TableType","ColumnSize","Nullable") {
			@Override
			public ResultSet execute() throws SQLException {
				return dmd.getColumns(null, schema, "%", "%");
			}
		}.read();

	}

	/**
	 *
	 * テーブルのプライマリーキー列名を得る。
	 *
	 * @param dmd DatabaseMetaData
	 * @param schema 対象スキーマ名
	 * @param table 対象テーブル名
	 * @return 検索結果
	 * @throws SQLException SQL例外発生
	 */
	public static DataCollection getPrimaryKeys(
			final DatabaseMetaData dmd,final String schema,final String table)throws SQLException{

		return new AccessProcessor("ColumnName") {
			@Override
			public ResultSet execute() throws SQLException {
				return dmd.getPrimaryKeys(null, schema, table);
			}
		}.read();

	}

}
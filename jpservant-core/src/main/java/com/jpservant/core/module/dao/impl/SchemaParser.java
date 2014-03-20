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
package com.jpservant.core.module.dao.impl;

import static com.jpservant.core.common.sql.impl.DatabaseMetaDataUtils.*;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jpservant.core.common.Constant.ConfigurationName;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * 各モジュールが利用するスキーマ情報の解析処理機。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class SchemaParser {

	/**
	 *
	 * テーブルのメタ情報オブジェクト。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static class TableMetaData {

		private String name;
		private List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
		private List<String> primarykeys = new ArrayList<String>();

		private TableMetaData(String name) {
			this.name = name;
		}

		private void addColumnSchema(ColumnMetaData column) {
			this.columns.add(column);
		}

		private void addPrimaryKey(String primarykey) {
			this.primarykeys.add(primarykey);
		}

		public String getName() {
			return this.name;
		}

		public List<ColumnMetaData> getColumns() {
			return columns;
		}

		public List<String> getPrimaryKeys() {
			return primarykeys;
		}

		public List<String> getColumnNames() {

			ArrayList<String> colnames = new ArrayList<String>();
			for (ColumnMetaData column : getColumns()) {
				colnames.add(column.getName());
			}
			return colnames;

		}
	}

	/**
	 *
	 * テーブルカラムのメタ情報オブジェクト。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static class ColumnMetaData {

		private String name;
		private String type;
		private int javatype;
		private int size;
		private boolean nullable;
		private boolean autoincrement;

		private ColumnMetaData(DataObject column) {

			this.name = (String) column.get("ColumnName");
			this.type = (String) column.get("TypeName");
			this.javatype = toInteger(column.get("DataType"));
			this.size = toInteger(column.get("ColumnSize"));
			this.nullable = toBoolean(column.get("IsNullable"));
			this.autoincrement = toBoolean(column.get("IsAutoincrement"));
		}

		public String getName() {
			return this.name;
		}

		public String getType() {
			return this.type;
		}

		public int getJavaType() {
			return this.javatype;
		}

		public int getSize() {
			return this.size;
		}

		public boolean isNullable() {
			return this.nullable;
		}

		public boolean isAutoincrement() {
			return this.autoincrement;
		}

	}

	/**
	 *
	 * データベーススキーマの解析処理を行い、相応のオブジェクトモデルを構築する。
	 *
	 * @param configuration モジュール設定情報
	 * @return 解析結果
	 */
	public static Map<String, TableMetaData> parse(ModuleConfiguration configuration) throws SQLException {

		DatabaseConnectionHolder holder = null;
		Map<String, TableMetaData> tableschemas = new HashMap<String, TableMetaData>();

		try {

			holder = configuration.findJDBCConnection(
					ConfigurationName.JDBCResourcePath);
			holder.connect();

			DatabaseMetaData dmd = holder.getConnection().getMetaData();
			String schemaname = configuration.getValue(ConfigurationName.SchemaName);

			for (DataObject table : getSelectableTableNames(dmd, schemaname)) {

				String tablename = (String) table.get("TableName");
				TableMetaData tableschema = new TableMetaData(tablename);
				tableschemas.put(tablename, tableschema);

				for (DataObject pk : getPrimaryKeys(dmd, schemaname, tablename)) {
					tableschema.addPrimaryKey((String) pk.get("ColumnName"));
				}

			}

			for (DataObject column : getColumnNames(dmd, schemaname)) {

				TableMetaData tableschema = tableschemas.get(column.get("TableName"));
				tableschema.addColumnSchema(new ColumnMetaData(column));

			}

			return tableschemas;

		} finally {
			if (holder != null) {
				try {
					holder.releaseSession();
				} catch (Exception e) {
					//no operation
				}
			}
		}
	}

}

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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jpservant.core.common.Constant.ConfigurationName;
import com.jpservant.core.common.DataCollection;
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

	public static class TableSchema {

		private String name;
		private List<ColumnSchema> columns = new ArrayList<ColumnSchema>();
		private List<String> primarykeys = new ArrayList<String>();

		private TableSchema(String name) {
			this.name = name;
		}

		private void addColumnSchema(ColumnSchema column) {
			this.columns.add(column);
		}

		private void addPrimaryKey(String primarykey) {
			this.primarykeys.add(primarykey);
		}

		public String getName() {
			return this.name;
		}

		public Iterator<ColumnSchema> getColumns() {
			return columns.iterator();
		}

		public Iterator<String> getPrimaryKeys() {
			return primarykeys.iterator();
		}

	}

	public static class ColumnSchema {

		private String name;
		private String type;
		private int javatype;
		private int size;
		private boolean nullable;
		private boolean autoincrement;

		private ColumnSchema(DataObject column) {

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
	public static Map<String, TableSchema> parse(ModuleConfiguration configuration) throws SQLException {

		//TODO: Implementation

		DatabaseConnectionHolder holder =
				configuration.findJDBCConnection(ConfigurationName.JDBCResourcePath.name());

		holder.connect();
		Connection conn = holder.getConnection();
		DatabaseMetaData dmd = conn.getMetaData();
		String schemaname = (String) configuration.get(ConfigurationName.SchemaName.name());

		Map<String, TableSchema> tableschemas = new HashMap<String, TableSchema>();
		DataCollection tables = getSelectableTableNames(dmd, schemaname);
		for (DataObject table : tables) {

			String tablename = (String) table.get("TableName");
			TableSchema tableschema = new TableSchema(tablename);
			tableschemas.put(tablename, tableschema);

			DataCollection pklist = getPrimaryKeys(dmd, schemaname, tablename);
			for (DataObject pk : pklist) {
				tableschema.addPrimaryKey((String) pk.get("ColumnName"));
			}

		}

		DataCollection columns = getColumnNames(dmd, schemaname);
		for (DataObject column : columns) {

			TableSchema tableschema = tableschemas.get(column.get("TableName"));
			tableschema.addColumnSchema(new ColumnSchema(column));
		}

		return tableschemas;
	}

}

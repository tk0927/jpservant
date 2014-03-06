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
package com.jpservant.core.kernel.impl;

import static com.jpservant.core.common.sql.impl.DatabaseMetadataUtils.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import com.jpservant.core.common.Constant.ConfigurationName;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;

/**
 *
 * 各モジュールが利用するスキーマ情報のオンメモリディクショナリ。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class SchemaRepository {

	/**
	 *
	 * モジュール単位の、紐づくデータベーススキーマの解析結果を格納するクラス。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 */
	public static class SchemaEntry{

	}

	/**
	 * ディクショナリの実体。
	 * モジュールインスタンス毎にエントリを持つ。
	 */
	private static HashMap<ModulePlatform,SchemaEntry> POOL = new HashMap<ModulePlatform,SchemaEntry>();

	/**
	 *
	 * モジュールに対応するエントリーを得る。
	 *
	 * @param module モジュールインスタンス参照
	 * @return ディクショナリエントリー
	 */
	public static SchemaEntry getEntry(ModulePlatform module){
		return POOL.get(module);
	}

	/**
	 *
	 * モジュールに紐づくデータベースの解析を行い、得られたエントリーをディクショナリへ登録する。
	 *
	 * @param module モジュールインスタンス参照
	 * @param configuration モジュール設定情報
	 */
	public static void addEntry(ModulePlatform module,ModuleConfiguration configuration)throws SQLException{
		POOL.put(module,analyze(configuration));
	}

	/**
	 *
	 * データベーススキーマの解析処理を行い、ディクショナリエントリーを生成する。
	 *
	 * @param configuration モジュール設定情報
	 * @return ディクショナリエントリー
	 */
	private static SchemaEntry analyze(ModuleConfiguration configuration)throws SQLException{

		//TODO: Implementation

		DatabaseConnectionHolder holder =
				configuration.findJDBCConnection(ConfigurationName.JDBCResourcePath.name());

		holder.connect();
		Connection conn = holder.getConnection();
		DatabaseMetaData dmd = conn.getMetaData();
		String schemaname = (String)configuration.get(ConfigurationName.SchemaName.name());

		DataCollection tables = getSelectableTableNames(dmd, schemaname);
		DataCollection columns = getColumnNames(dmd, schemaname);

		System.out.println(tables);
		System.out.println(columns);

		for(DataObject table : tables){
			String tablename = (String)table.get("TABLE_NAME");
			DataCollection pklist = getPrimaryKeys(dmd, schemaname, tablename);
			System.out.println(tablename +"="+ pklist);
		}

		return null;
	}
}

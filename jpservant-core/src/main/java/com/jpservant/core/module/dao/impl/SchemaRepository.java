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

import static com.jpservant.core.common.Utilities.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jpservant.core.common.Constant.RequestMethod;
import com.jpservant.core.module.dao.impl.SchemaParser.ColumnMetaData;
import com.jpservant.core.module.dao.impl.SchemaParser.TableMetaData;
import com.jpservant.core.module.dao.impl.action.DeleteAllAction;
import com.jpservant.core.module.dao.impl.action.InsertAction;
import com.jpservant.core.module.dao.impl.action.RowCountAction;
import com.jpservant.core.module.dao.impl.action.SelectAllAction;
import com.jpservant.core.module.dao.impl.action.SelectByPrimaryKeyAction;
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
	 * ディクショナリの実体。モジュールインスタンス毎にエントリーを持つ。
	 */
	private static HashMap<ModulePlatform, SchemaEntry> POOL = new HashMap<ModulePlatform, SchemaEntry>();

	/**
	 *
	 * モジュールに対応するREST APIのディクショナリエントリーを得る。
	 *
	 * @param module モジュールインスタンス参照
	 * @return ディクショナリエントリー
	 */
	public static SchemaEntry getEntry(ModulePlatform module) {
		return POOL.get(module);
	}

	/**
	 *
	 * モジュールに紐づくデータベースの解析を行い、得られたREST APIのディクショナリエントリーをディクショナリへ登録する。
	 *
	 * @param module モジュールインスタンス参照
	 * @param configuration モジュール設定情報
	 */
	public static void addEntry(ModulePlatform module, ModuleConfiguration configuration) throws SQLException {
		POOL.put(module, analyze(configuration));
	}

	/**
	 *
	 * データベーススキーマの解析処理を行い、REST APIのディクショナリエントリーを生成する。
	 *
	 * @param configuration モジュール設定情報
	 * @return ディクショナリエントリー
	 */
	private static SchemaEntry analyze(ModuleConfiguration configuration) throws SQLException {

		Map<String, TableMetaData> tableschemas = SchemaParser.parse(configuration);
		SchemaEntry schemaentry = new SchemaEntry(tableschemas);

		for (Map.Entry<String, TableMetaData> entry : tableschemas.entrySet()) {

			String tablename = entry.getKey();
			TableMetaData tmd = entry.getValue();
			ArrayList<String> colnames = new ArrayList<String>();
			for (ColumnMetaData column : tmd.getColumns()) {
				colnames.add(column.getName());
			}
			List<String> primarykeys = tmd.getPrimaryKeys();
			String tablepath = convertSnakeToCamel(tablename);

			schemaentry.addEntry(
					concatPathTokens(tablepath), RequestMethod.GET,
					new SelectAllAction(tablename));
			schemaentry.addEntry(
					concatPathTokens(tablepath), RequestMethod.POST,
					new InsertAction(tablename, colnames));
			schemaentry.addEntry(
					concatPathTokens(tablepath), RequestMethod.DELETE,
					new DeleteAllAction(tablename));
			schemaentry.addEntry(
					concatPathTokens(tablepath, "Count"), RequestMethod.GET,
					new RowCountAction(tablename));
			schemaentry.addEntry(
					concatPathTokens(tablepath, primarykeys), RequestMethod.GET,
					new SelectByPrimaryKeyAction(tablename, convertSnakeToCamel(primarykeys)));

		}

		return schemaentry;
	}
}

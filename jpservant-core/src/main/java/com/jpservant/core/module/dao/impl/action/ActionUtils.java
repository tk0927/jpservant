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
package com.jpservant.core.module.dao.impl.action;

import static com.jpservant.core.common.Utilities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

/**
 *
 * Action向けのユーティリティメソッドを提供します。
 *
 * @author Toshiaki.Kamoshida <kamoshida.toshiaki@gmail.com>
 * @version 0.1
 *
 */
public class ActionUtils {

	public static String createColumnsToken(List<String> columnnames) {

		if (columnnames == null | columnnames.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String colname : columnnames) {
			sb.append(colname);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();

	}

	public static String createInsertPlaceholderToken(List<String> columnnames) {

		if (columnnames == null | columnnames.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String colname : columnnames) {
			sb.append(":");
			sb.append(convertSnakeToCamel(colname));
			sb.append(",");
		}

		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String createUpdatePlaceholderToken(List<String> columnnames) {

		if (columnnames == null | columnnames.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String colname : columnnames) {
			sb.append(colname);
			sb.append("=:");
			sb.append(convertSnakeToCamel(colname));
			sb.append(",");
		}

		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String createWhereClause(List<String> columnnames) {
		return createWhereClause(columnnames, "");
	}

	public static String createWhereClause(List<String> columnnames, String prefix) {

		if (columnnames == null | columnnames.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String colname : columnnames) {
			sb.append(colname);
			sb.append("=:");
			sb.append(prefix + convertSnakeToCamel(colname));
			sb.append(" AND ");
		}

		return sb.substring(0, sb.length() - 4);
	}

	/**
	 *
	 * 検索条件に合致するSQL Where句を生成する。
	 *
	 * @param colnames テーブルのカラム名リスト
	 * @param criteria 検索条件オブジェクト
	 * @return SQL Where句
	 */
	public static String createWhereClause(List<String> columnnames, DataObject criteria) {

		return createWhereClause(columnnames, criteria, "");

	}

	public static String createWhereClause(List<String> columnnames, DataObject criteria, String prefix) {

		ArrayList<String> criteriatokens = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : criteria.entrySet()) {

			String ccolname = entry.getKey();
			if (ccolname.startsWith(prefix)) {
				criteriatokens.add(convertCamelToSnake(ccolname.replaceAll(prefix, "")));
			}
		}
		return createWhereClause(criteriatokens, prefix);

	}

	public static String createWhereClause(List<String> columnnames, List<String> bindnames) {

		if (columnnames == null | columnnames.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String colname : columnnames) {
			sb.append(colname);
			sb.append("=:");
			sb.append(bindnames.get(i++));
			sb.append(" AND ");
		}

		return sb.substring(0, sb.length() - 4);
	}

	public static String createOrderByClause(List<String> columnnames) {

		if (columnnames == null | columnnames.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String colname : columnnames) {
			sb.append(colname);
			sb.append(",");
		}

		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static DataObject bindPathTokens(List<String> colnames, List<String> values) {

		DataObject retvalue = new DataObject();

		for (int i = 0; i < colnames.size(); i++) {
			retvalue.put(colnames.get(i), values.get(i));
		}

		return retvalue;

	}

	public static DataCollection bindPathTokens(DataCollection list, List<String> colnames, List<String> values) {

		DataCollection retvalue = new DataCollection();
		for (DataObject obj : list) {

			DataObject element = new DataObject(obj);
			for (int i = 0; i < colnames.size(); i++) {
				element.put(colnames.get(i), values.get(i));
			}
			retvalue.add(element);
		}
		return retvalue;
	}

	public static List<String> appendPrefixes(List<String> list, String prefix) {

		List<String> retvalue = new ArrayList<String>();
		for (String obj : list) {
			retvalue.add(prefix + obj);
		}
		return retvalue;
	}

}

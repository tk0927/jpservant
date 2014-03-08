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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jpservant.core.common.Constant.RequestMethod;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.common.sql.SQLProcessor;
import com.jpservant.core.exception.ApplicationException;
import com.jpservant.core.exception.ApplicationException.ErrorType;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.dao.impl.SchemaParser.TableMetaData;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * モジュール単位の、紐づくデータベーススキーマの解析結果を格納するクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 */
public class SchemaEntry {

	/**
	 *
	 * データベーススキーマの検索キーオブジェクト。
	 *
	 * @author Toshiaki.Kamoshida <kamoshida.toshiaki@gmail.com>
	 * @version 0.1
	 *
	 */
	public static class SchemaEntryKey {

		private String path;
		private RequestMethod method;
		int hashcode;

		private SchemaEntryKey(String path, RequestMethod method) {
			this.path = path;
			this.method = method;
			this.hashcode = (path + method).hashCode();
		}

		public int hashCode() {
			return this.hashcode;
		}

		public boolean equals(Object dst) {
			if (dst == null || !(dst instanceof SchemaEntryKey)) {
				return false;
			}
			SchemaEntryKey dstobj = (SchemaEntryKey) dst;
			return (this.path.equals(dstobj.path) && this.method.equals(dstobj.method));
		}
	}

//	private Map<String, TableMetaData> metadata;

	/** 格納位置の実体 */
	private Map<SchemaEntryKey, DataAccessAction> impl = new HashMap<SchemaEntryKey, DataAccessAction>();

	/**
	 *
	 * コンストラクタ
	 *
	 * @param metadata メタデータ
	 */
	public SchemaEntry(Map<String, TableMetaData> metadata) {
//		this.metadata = metadata;
	}

	/**
	 *
	 * エントリー追加
	 *
	 * @param path URIパス
	 * @param method Resuestメソッド
	 * @param action 対応するアクション
	 */
	public void addEntry(String path, RequestMethod method, DataAccessAction action) {
		impl.put(new SchemaEntryKey(path, method), action);
	}

	/**
	 *
	 * REST API処理を実行する。
	 *
	 * @param config 設定情報
	 * @param context コンテキスト情報
	 * @param holder データベース接続
	 * @return 実行結果
	 * @throws SQLException 何らかのSQL例外発生
	 * @throws ApplicationException URIに対応するアクションが存在しない
	 */
	public DataCollection execute(ModuleConfiguration config, KernelContext context, DatabaseConnectionHolder holder)
			throws SQLException, ApplicationException {

		String path = context.getRequestPath();
		String method = context.getMethod();
		List<String> pathtokens = null;

		DataAccessAction action = impl.get(
				new SchemaEntryKey(path, RequestMethod.valueOf(method)));

		if (action == null) {

			for (SchemaEntryKey key : impl.keySet()) {

				pathtokens = findPathTokens(key, path, method);
				if(pathtokens != null){
					action = impl.get(key);
					break;
				}
			}

			if (action == null) {
				throw new ApplicationException(ErrorType.NotFound);
			}
		}

		SQLProcessor processor = new SQLProcessor(holder);

		return action.execute(processor, config, context, pathtokens);

	}

	/**
	 *
	 * パスのパラメータ部分(テーブル名を示す最上位を除く部分)を考慮して、
	 * リクエストパラメータ、メソッドとエントリーキーが一致するかを検査する。
	 *
	 *
	 * @param key エントリーキー
	 * @param path リクエストURIパス
	 * @param method リクエストメソッド
	 * @return 一致する場合に限り、パスパラメータ部分リスト 一致しない場合null
	 */
	private static List<String> findPathTokens(SchemaEntryKey key,String path,String method){

		if (!key.method.name().equals(method)) {
			return null;
		}

		Pattern p = Pattern.compile(key.path);
		Matcher m = p.matcher(path);

		if (!m.matches()) {
			return null;
		}
		List<String> retvalue = new ArrayList<String>();
		for(int i = 1 ; i <= m.groupCount() ; i++){
			retvalue.add(m.group(i));
		}

		return retvalue;
	}
}
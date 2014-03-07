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
import java.util.HashMap;
import java.util.Map;

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

	public static class SchemaEntryKey {

		private String path;
		private RequestMethod method;
		int hashcode;

		public SchemaEntryKey(String path, RequestMethod method) {
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

	private Map<String, TableMetaData> metadata;

	/** 格納位置の実体 */
	private Map<SchemaEntryKey, DataAccessAction> impl = new HashMap<SchemaEntryKey, DataAccessAction>();

	/**
	 *
	 * コンストラクタ
	 *
	 * @param metadata メタデータ
	 */
	public SchemaEntry(Map<String, TableMetaData> metadata) {
		this.metadata = metadata;
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

		DataAccessAction action = impl.get(
				new SchemaEntryKey(path, RequestMethod.valueOf(method)));
		if (action == null) {
			//TODO: 可変パス検査

			throw new ApplicationException(ErrorType.NotFound);
		}

		SQLProcessor processor = new SQLProcessor(holder);

		return action.execute(processor, config, context);

	}
}
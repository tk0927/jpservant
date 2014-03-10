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
import java.util.List;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.sql.SQLProcessor;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * DAO REST APIに紐づく処理を実装する位置。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public abstract class DataAccessAction {

	private SchemaEntry entry;

	/**
	 *
	 * 処理を実行します。
	 *
	 * @param processor SQL実行機
	 * @param config 設定情報
	 * @param context コンテキスト
	 * @param pathtokens URLパストークンリスト
	 * @return 検索・更新実行結果
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public abstract DataCollection execute(
			SQLProcessor processor, ModuleConfiguration config, KernelContext context, List<String> pathtokens)
			throws SQLException;

	public String toString() {
		return getClass().getSimpleName();
	}

	public void setSchemaEntry(SchemaEntry entry) {
		this.entry = entry;
	}

	public SchemaEntry getSchemaEntry(){
		return this.entry;
	}
}

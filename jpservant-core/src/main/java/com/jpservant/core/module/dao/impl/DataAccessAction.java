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

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
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

	private String tablename;

	protected DataAccessAction(String tablename) {
		this.tablename = tablename;
	}

	public abstract DataCollection execute(
			SQLProcessor processor, ModuleConfiguration config, KernelContext context)
			throws SQLException;

	/**
	 *
	 * 全件検索Action。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static class SelectAllAction extends DataAccessAction {

		public SelectAllAction(String tablename) {
			super(tablename);
		}

		@Override
		public DataCollection execute(
				SQLProcessor processor, ModuleConfiguration config, KernelContext context)
				throws SQLException {
			return processor.executeQuery(String.format("SELECT * FROM %s", super.tablename));
		}
	}

	/**
	 *
	 * 全件削除Action。
	 *
	 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
	 * @version 0.1
	 *
	 */
	public static class DeleteAllAction extends DataAccessAction {

		public DeleteAllAction(String tablename) {
			super(tablename);
		}

		@Override
		public DataCollection execute(
				SQLProcessor processor, ModuleConfiguration config, KernelContext context)
				throws SQLException {

			int result = processor.executeUpdate(String.format("DELETE * FROM %s", super.tablename));
			return new DataCollection(new DataObject("count", result));

		}
	}

}

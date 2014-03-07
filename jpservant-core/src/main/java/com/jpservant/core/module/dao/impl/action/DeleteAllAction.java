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

import java.sql.SQLException;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.sql.SQLProcessor;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.dao.impl.DataAccessAction;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * 全件削除Action。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class DeleteAllAction implements DataAccessAction {

	private String sql;

	public DeleteAllAction(String tablename) {
		this.sql = String.format("DELETE * FROM %s", tablename);
	}

	@Override
	public DataCollection execute(
			SQLProcessor processor, ModuleConfiguration config, KernelContext context)
			throws SQLException {

		int result = processor.executeUpdate(this.sql);
		return new DataCollection(new DataObject("count", result));

	}
}
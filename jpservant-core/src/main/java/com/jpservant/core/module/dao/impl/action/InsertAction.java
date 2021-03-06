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
import static com.jpservant.core.module.dao.impl.action.ActionUtils.*;

import java.sql.SQLException;
import java.util.List;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.sql.SQLProcessor;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.dao.impl.DataAccessAction;
import com.jpservant.core.module.dao.impl.SchemaParser.TableMetaData;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * データ挿入Action。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class InsertAction extends DataAccessAction {

	private String tablename;
	private String sql;

	public InsertAction(String tablename) {

		this.tablename = tablename;

	}

	@Override
	protected void initialize() {

		TableMetaData tmd = getSchemaEntry().getTableMetaData(this.tablename);
		List<String> columnnames = tmd.getColumnNames();

		this.sql = String.format("INSERT INTO %s( %s )VALUES( %s )",
				tablename,
				createColumnsToken(columnnames),
				createInsertPlaceholderToken(columnnames));

	}

	@Override
	public DataCollection execute(
			SQLProcessor processor, ModuleConfiguration config, KernelContext context, List<String> pathtokens)
			throws SQLException {

		int[] result = processor.executeUpdate(this.sql, context.getParameters());
		return new DataCollection(new DataObject("Count", toStringArray(result)));

	}

}
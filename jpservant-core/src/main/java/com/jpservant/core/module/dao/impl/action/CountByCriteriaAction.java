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
 * 条件指定検索Action。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class CountByCriteriaAction extends DataAccessAction {

	private String tablename;
	private String sql;

	public CountByCriteriaAction(String tablename) {

		this.tablename = tablename;
		this.sql = String.format("SELECT COUNT(*) AS COUNT FROM %s WHERE ", tablename);

	}

	@Override
	public DataCollection execute(
			SQLProcessor processor, ModuleConfiguration config, KernelContext context, List<String> pathtokens)
			throws SQLException {

		TableMetaData tmd = getSchemaEntry().getTableMetaData(tablename);
		List<String> colnames = tmd.getColumnNames();
		DataCollection critrias = context.getParameters();
		DataCollection retvalue = new DataCollection();

		if (critrias == null || critrias.isEmpty()) {
			return retvalue;
		}

		int count = 1;
		for (DataObject criteria : critrias) {

			retvalue.add(new DataObject(String.valueOf(count++),
					processor.executeQuery(
							sql + createWhereClause(colnames, criteria), criteria)));

		}

		return retvalue.size() == 1 ?
				(DataCollection) retvalue.get(0).get("1") : retvalue;

	}

}
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
package com.jpservant.core.module.sql;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.Utilities;
import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.common.sql.SQLProcessor;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.kernel.PostProcessor;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.core.resource.ResourcePlatform;

/**
 *
 * SQLモジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class QueryModulePlatform implements ModulePlatform {

	public static enum ConfigurationName{
		JDBCResourcePath,
		ResourceRoot,
	}

	/**
	 * SQL文がQueryであるかを判定するキー文字列
	 */
	public static final String SQL_QUERY_KEY = "SELECT";
	/**
	 * SQL文定義ファイルの拡張子
	 */
	public static final String SQL_FILE_EXT = ".sql";

	private ModuleConfiguration config;

	@Override
	public void initialize(ModuleConfiguration config) {

		this.config = config;
	}

	@Override
	public void execute(KernelContext context) throws Exception{

		ResourcePlatform resource = config.getConfigurationManager().getResourcePlatform(
				(String) config.get(ConfigurationName.JDBCResourcePath.name()));

		final DatabaseConnectionHolder holder = (DatabaseConnectionHolder)resource.getResource();

		String path = String.format("%s%s%s",
				config.get(ConfigurationName.ResourceRoot.name()),
				context.getRequestPath(),
				SQL_FILE_EXT);

		String content = Utilities.loadResource(context.getResource(path)).trim();

		SQLProcessor processor = new SQLProcessor(holder);

		DataCollection collection = context.getParameters();

		if(content.toUpperCase().startsWith(SQL_QUERY_KEY)){

			DataCollection result = processor.executeQuery(content,
					collection == null || collection.isEmpty() ? null :collection.get(0));

			context.writeResponse(result);

		}else{

			int[] result = processor.executeUpdate(content, collection);
			DataObject response = new DataObject();
			response.put("count", result);

			context.writeResponse(response);
		}

		context.addPostProcessor(new PostProcessor() {
			@Override
			public void execute() throws Exception {
				holder.commit();
				holder.releaseSession();
			}
		});

		context.addErrorProcessor(new PostProcessor() {
			@Override
			public void execute() throws Exception {
				holder.rollback();
				holder.releaseSession();
			}
		});
	}

}

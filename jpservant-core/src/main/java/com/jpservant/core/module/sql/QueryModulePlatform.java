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

import static com.jpservant.core.common.Constant.RequestMethod.*;
import static com.jpservant.core.common.Utilities.*;

import java.sql.SQLException;

import com.jpservant.core.common.AccessController;
import com.jpservant.core.common.Constant.ConfigurationName;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.common.sql.SQLProcessor;
import com.jpservant.core.exception.ApplicationException;
import com.jpservant.core.exception.ApplicationException.ErrorType;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.kernel.PostProcessor;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;

/**
 *
 * SQLモジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class QueryModulePlatform implements ModulePlatform {

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
	public void execute(KernelContext context) throws Exception {

		try{

			if(!AccessController.checkAccessMethod(context, POST)){
				throw new ApplicationException(ErrorType.BadRequest);
			}

			DatabaseConnectionHolder holder = config.findJDBCConnection(
					ConfigurationName.JDBCResourcePath.name());
			String path = createResourcePath(config, context, SQL_FILE_EXT);
			String content = findResource(context.getResource(path)).trim();
			DataCollection collection = context.getParameters();

			SQLProcessor processor = new SQLProcessor(holder);
			if (content.toUpperCase().startsWith(SQL_QUERY_KEY)) {

				DataCollection result = processor.executeQuery(content,
						(collection == null || collection.isEmpty()) ? null : collection.get(0));

				context.writeResponse(result);

			} else {

				int[] result = processor.executeUpdate(content, collection);
				DataObject response = new DataObject();
				response.put("count", result);

				context.writeResponse(response);
			}

			registerPostProcess(context, holder);

		}catch(SQLException e){

			throw new ApplicationException(ErrorType.BadRequest,e);

		}

	}

	/**
	 *
	 * 後処理を登録します。
	 *
	 * @param context コンテキスト
	 * @param holder データベース接続
	 */
	public void registerPostProcess(KernelContext context, final DatabaseConnectionHolder holder) {

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

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
package com.jpservant.core.module.dao;

import java.sql.SQLException;

import com.jpservant.core.common.Constant.ConfigurationName;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.exception.ApplicationException;
import com.jpservant.core.exception.ApplicationException.ErrorType;
import com.jpservant.core.exception.ConfigurationException;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.dao.impl.SchemaEntry;
import com.jpservant.core.module.dao.impl.SchemaRepository;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.core.module.spi.ModuleUtils;

/**
 *
 * DAOモジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class DAOModulePlatform implements ModulePlatform {

	private ModuleConfiguration config;

	@Override
	public void initialize(ModuleConfiguration config) throws ConfigurationException {

		this.config = config;
		try {
			SchemaRepository.addEntry(this, config);
		} catch (SQLException e) {
			throw new ConfigurationException(e);
		}
	}

	@Override
	public void execute(KernelContext context) throws Exception {

		DatabaseConnectionHolder holder = null;
		try {

			holder = config.findJDBCConnection(
					ConfigurationName.JDBCResourcePath);

			SchemaEntry entry = SchemaRepository.getEntry(this);
			DataCollection response = entry.execute(config, context, holder);
			context.writeResponse(response);

		} catch (SQLException e) {

			throw new ApplicationException(ErrorType.BadRequest, e);

		} finally {

			ModuleUtils.registerPostProcessForConnection(context, holder);

		}
	}
}

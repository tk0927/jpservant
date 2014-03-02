package com.jpservant.core.module.sql;

import java.io.IOException;
import java.sql.SQLException;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.Utilities;
import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.common.sql.SQLProcessor;
import com.jpservant.core.kernel.KernelContext;
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
public class SQLModulePlatform implements ModulePlatform {

	public static enum ConfigurationName{
		JDBCResourcePath,
		ResourceType,
		JDBCUser,
		ResourceRoot,
	}

	private static final String SQL_QUERY_KEY = "SELECT";
	private static final String SQL_FILE_EXT = ".sql";

	private ModuleConfiguration config;

	@Override
	public void initialize(ModuleConfiguration config) {

		this.config = config;
	}

	@Override
	public void execute(KernelContext context) throws IOException,SQLException{

		ResourcePlatform resource = config.getConfigurationManager().getResourcePlatform(
				(String) config.get(ConfigurationName.JDBCResourcePath.name()));

		DatabaseConnectionHolder holder = (DatabaseConnectionHolder)resource.getResource();

		String path = String.format("%s%s%s",
				config.get(ConfigurationName.ResourceRoot.name()),
				context.getRequestPath(),
				SQL_FILE_EXT);

		String content = Utilities.loadResource(getClass().getResource(path)).trim();

		SQLProcessor processor = new SQLProcessor(holder);

		DataCollection collection = context.getParameters();

		if(content.toUpperCase().startsWith(SQL_QUERY_KEY)){

			DataCollection result = processor.executeQuery(content,
					collection == null || collection.isEmpty() ? null :collection.get(0));

			context.writeResponse(result);

		}else{

			int[] result = processor.executeUpdate(content, collection);
			DataCollection response = new DataCollection();
			DataObject count = new DataObject();
			count.put("count", result);

			context.writeResponse(response);
		}
	}

}

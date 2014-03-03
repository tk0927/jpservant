package com.jpservant.core.module.mock;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.Utilities;
import com.jpservant.core.kernel.KernelContext;
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
public class MockModulePlatform implements ModulePlatform {

	public static enum ConfigurationName{
		ResourceRoot,
	}

	/**
	 * SQL文定義ファイルの拡張子
	 */
	public static final String JSON_FILE_EXT = ".json";

	private static final String METHOD_NAME_DEFAULT = "DEFAULT";

	private ModuleConfiguration config;

	@Override
	public void initialize(ModuleConfiguration config) {

		this.config = config;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(KernelContext context) throws Exception{

		String path = String.format("%s%s%s",
				config.get(ConfigurationName.ResourceRoot.name()),
				context.getRequestPath(),
				JSON_FILE_EXT);

		String method = context.getMethod();

		ObjectMapper mapper = new ObjectMapper();
		String content = Utilities.loadResource(context.getResource(path)).trim();

		DataObject mock = mapper.readValue(content, DataObject.class);
		Object object = mock.get(method);
		if(object == null){
			object = mock.get(METHOD_NAME_DEFAULT);
		}
		if(object == null){
			object = mock;
		}

		DataCollection collection = new DataCollection();
		if(object instanceof Map<?, ?>){

			DataObject data = new DataObject();
			data.putAll((Map<String,Object>)object);
			collection.add(data);

		}

		context.writeResponse(collection);
	}


}

package com.jpservant.core.kernel;

import static com.jpservant.core.common.Constant.ConfigurationName.*;

import java.net.URL;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * システムの設定情報を格納し、各機能に提供するクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class ConfigurationManager {


	private HashMap<String,ModuleConfiguration> impl = new  HashMap<String,ModuleConfiguration>();


	public ConfigurationManager(URL url)throws ConfigurationException{

		try{

			ObjectMapper mapper = new ObjectMapper();
			DataCollection parsed = mapper.readValue(url.openStream(), DataCollection.class);
			parseConfiguration(parsed);

		}catch(Exception e){
			throw new ConfigurationException(e);
		}
	}


	private void parseConfiguration(DataCollection parsed){

		for(DataObject element: parsed){

			ModuleConfiguration config = new ModuleConfiguration();
			config.putAll(element);

			impl.put(element.get(ModuleRootPath.name()).toString(),config);
		}

	}

	public ModuleConfiguration getModuleConfiguration(String path){
		return impl.get(path);
	}

}

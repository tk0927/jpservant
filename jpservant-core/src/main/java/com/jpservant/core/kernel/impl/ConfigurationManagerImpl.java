package com.jpservant.core.kernel.impl;

import static com.jpservant.core.common.Constant.ConfigurationName.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.kernel.ConfigurationException;
import com.jpservant.core.kernel.ConfigurationManager;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.core.resource.ResourcePlatform;

/**
 *
 * システムの設定情報を格納し、各機能に提供するクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class ConfigurationManagerImpl implements ConfigurationManager {


	private HashMap<String,ModuleConfiguration> configmap = new  HashMap<String,ModuleConfiguration>();
	private HashMap<String,ModulePlatform> modulemap = new  HashMap<String,ModulePlatform>();
	private HashMap<String,ResourcePlatform> resourcemap = new  HashMap<String,ResourcePlatform>();


	/**
	 *
	 * コンストラクタ。
	 *
	 * @param url 設定ファイルへのURL
	 * @throws ConfigurationException 初期化失敗
	 */
	public void initialize(URL url)throws ConfigurationException{

		try{

			ObjectMapper mapper = new ObjectMapper();
			DataCollection parsed = mapper.readValue(url.openStream(), DataCollection.class);
			parseConfiguration(parsed);

		}catch(Exception e){
			throw new ConfigurationException(e);
		}
	}


	/**
	 *
	 * 設定ファイルの解析処理。
	 *
	 * @param parsed JSON形式設定ファイルをPOJOに置換した結果
	 * @throws ConfigurationException 初期化失敗
	 */
	private void parseConfiguration(DataCollection parsed)throws ConfigurationException{

		try{

			for(DataObject element: parsed){

				ModuleConfiguration config = new ModuleConfiguration(this);
				config.putAll(element);

				String path = (String) element.get(RootPath.name());

				if(configmap.containsKey(path)){
					throw new ConfigurationException(String.format("Duplicated RootPath[%s]",path));
				}
				configmap.put(path,config);

				String classname = (String)element.get(PlatformClass.name());

				Class<?> clazz = Class.forName(classname);

				Object platform = clazz.newInstance();

				if(platform instanceof ModulePlatform){

					ModulePlatform module = (ModulePlatform)platform;
					modulemap.put(path, module);

				}else if(platform instanceof ResourcePlatform){

					ResourcePlatform module = (ResourcePlatform)platform;
					resourcemap.put(path, module);

				}else{
					throw new ConfigurationException(String.format("[%s] is not Platform-Class",classname));
				}


			}

			for(Map.Entry<String, ResourcePlatform> entry: resourcemap.entrySet()){
				entry.getValue().initialize(configmap.get(entry.getKey()));
			}

			for(Map.Entry<String, ModulePlatform> entry: modulemap.entrySet()){
				entry.getValue().initialize(configmap.get(entry.getKey()));
			}

		}catch(ConfigurationException e){
			throw e;
		}catch(Exception e){
			throw new ConfigurationException(e);
		}

	}


	@Override
	public ArrayList<String> getRootPathList(){
		return new ArrayList<String>(configmap.keySet());
	}


	@Override
	public ModuleConfiguration getModuleConfiguration(String path){
		return configmap.get(path);
	}


	@Override
	public ModulePlatform getModulePlatform(String path){
		return modulemap.get(path);
	}


	@Override
	public ResourcePlatform getResourcePlatform(String path){
		return resourcemap.get(path);
	}

}
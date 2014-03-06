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
package com.jpservant.core.kernel.impl;

import static com.jpservant.core.common.Constant.ConfigurationName.*;
import static com.jpservant.core.common.JacksonUtils.*;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.exception.ConfigurationException;
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


	/**
	 * 設定情報のオンメモリストア
	 */
	private HashMap<String,ModuleConfiguration> configmap = new  HashMap<String,ModuleConfiguration>();

	/**
	 * モジュールオブジェクトのオンメモリストア
	 */
	private HashMap<String,ModulePlatform> modulemap = new  HashMap<String,ModulePlatform>();

	/**
	 * リソースオブジェクトのオンメモリストア
	 */
	private HashMap<String,ResourcePlatform> resourcemap = new  HashMap<String,ResourcePlatform>();


	/**
	 *
	 * コンストラクタ。
	 *
	 * @param url 設定ファイルへのURL
	 * @throws ConfigurationException 初期化失敗
	 */
	public void initialize(URL url)throws ConfigurationException{

		InputStream in = null;
		try{

			in = url.openStream();
			DataCollection parsed = readDataCollection(in);
			parseConfiguration(parsed);

		}catch(Exception e){
			throw new ConfigurationException(e);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(Exception e){}
			}
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
				generatePlatform(element);
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


	/**
	 *
	 * 設定ファイル情報の1ノードに対応するPlatformオブジェクトを生成し、レジストする。
	 *
	 * @param element 設定情報ノード
	 * @throws ConfigurationException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void generatePlatform(DataObject element)
			throws ConfigurationException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {

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

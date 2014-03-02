package com.jpservant.core.module.spi;

import java.util.HashMap;

import com.jpservant.core.kernel.ConfigurationManager;

/**
 *
 * モジュール単位の設定情報の集合。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class ModuleConfiguration extends HashMap<String,Object>{

	/** 管理オブジェクトへの参照 */
	private ConfigurationManager manager;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param manager 管理オブジェクトへの参照
	 */
	public ModuleConfiguration(ConfigurationManager manager){
		this.manager = manager;
	}

	/**
	 *
	 * 管理オブジェクトへの参照を得る。
	 *
	 * @return 管理オブジェクトへの参照
	 */
	public ConfigurationManager getConfigurationManager(){
		return this.manager;
	}

}

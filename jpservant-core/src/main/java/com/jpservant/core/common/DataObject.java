package com.jpservant.core.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * REST APIリクエストに含まれるJSON オブジェクト要素を格納するクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class DataObject extends LinkedHashMap<String,Object>{

	/**
	 * デフォルトコンストラクタ。
	 *
	 */
	public DataObject(){}

	/**
	 *
	 * コピーコンストラクタ。
	 *
	 * @param impl コピー元
	 */
	public DataObject(Map<String,Object> impl){
		putAll(impl);
	}
}

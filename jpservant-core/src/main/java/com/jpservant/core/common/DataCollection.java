package com.jpservant.core.common;

import java.util.ArrayList;

/**
 *
 * REST APIリクエストに含まれるJSON オブジェクトリストを格納するクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class DataCollection extends ArrayList<DataObject>{

	/**
	 * デフォルトコンストラクタ。
	 *
	 */
	public DataCollection(){}

	/**
	 *
	 * コピーコンストラクタ。
	 *
	 * @param impl コピー元
	 */
	public DataCollection(ArrayList<DataObject> impl){
		addAll(impl);
	}

}

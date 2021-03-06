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
public class DataCollection extends ArrayList<DataObject> {

	/**
	 * デフォルトコンストラクタ。
	 *
	 */
	public DataCollection() {
	}

	/**
	 *
	 * コピーコンストラクタ。
	 *
	 * @param impl コピー元
	 */
	public DataCollection(ArrayList<DataObject> impl) {
		addAll(impl);
	}

	/**
	 *
	 * 要素一個を所有する状態とするコンストラクタ。
	 *
	 * @param obj 要素
	 */
	public DataCollection(DataObject obj) {
		add(obj);
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder("[\r\n");
		for (DataObject element : this) {
			sb.append(element == null ? "{null}" : element.toString());
			sb.append("\r\n");
		}
		sb.append("]\r\n");
		return sb.toString();

	}
}

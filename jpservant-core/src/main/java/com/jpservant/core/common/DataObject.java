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
public class DataObject extends LinkedHashMap<String, Object> {

	/**
	 * デフォルトコンストラクタ。
	 *
	 */
	public DataObject() {
	}

	/**
	 *
	 * コピーコンストラクタ。
	 *
	 * @param impl コピー元
	 */
	public DataObject(Map<String, Object> impl) {
		putAll(impl);
	}
}

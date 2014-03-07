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

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * JacksonライブラリによるJSON解析・生成処理を集約するユーティリティクラス。
 *
 * @author Toshiaki.Kamoshida <kamoshida.toshiaki@gmail.com>
 * @version 0.1
 *
 */
public class JacksonUtils {

	private static final ObjectMapper INSTANCE = new ObjectMapper();

	/**
	 *
	 * 任意のオブジェクトをJSON文字列に変換し、出力ストリームに書き出します。
	 *
	 * @param out 出力ストリーム
	 * @param object オブジェクト
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static void writeJSONString(Writer out, Object object)
			throws JsonMappingException, JsonParseException, IOException {

		INSTANCE.writeValue(out, object);

	}

	/**
	 *
	 * 任意のオブジェクトをJSON文字列に書き出します。
	 *
	 * @param object オブジェクト
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static String toJSONString(Object object)
			throws JsonMappingException, JsonParseException, IOException {

		return INSTANCE.writeValueAsString(object);

	}

	/**
	 *
	 * 入力ストリームから読み出したJSON文字列から {@link DataCollection}を生成します。
	 *
	 * @param content JSON文字列
	 * @return DataCollectionインスタンス
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static DataCollection readDataCollection(InputStream in)
			throws JsonMappingException, JsonParseException, IOException {

		return INSTANCE.readValue(in, DataCollection.class);

	}

	/**
	 *
	 * 入力ストリームから読み出したJSON文字列から {@link DataObject}を生成します。
	 *
	 * @param content JSON文字列
	 * @return DataObjectインスタンス
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static DataObject readDataObject(InputStream in)
			throws JsonMappingException, JsonParseException, IOException {

		return INSTANCE.readValue(in, DataObject.class);

	}

	/**
	 *
	 * 入力ストリームから読み出したJSON文字列から {@link DataObject}を生成し、これのみを格納する
	 * DataCollectionを生成します。
	 *
	 * @param content  JSON文字列
	 * @return DataCollectionインスタンス
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static DataCollection readOneDataObjectContainedCollection(InputStream in)
			throws JsonMappingException, JsonParseException, IOException {

		DataObject obj = INSTANCE.readValue(in, DataObject.class);
		DataCollection parameter = new DataCollection();
		parameter.add(obj);
		return parameter;

	}

	/**
	 *
	 * JSON文字列から {@link DataCollection}を生成します。
	 *
	 * @param content JSON文字列
	 * @return DataCollectionインスタンス
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static DataCollection toDataCollection(String content)
			throws JsonMappingException, JsonParseException, IOException {

		return INSTANCE.readValue(content, DataCollection.class);

	}

	/**
	 *
	 * JSON文字列から {@link DataObject}を生成します。
	 *
	 * @param content  JSON文字列
	 * @return DataObjectインスタンス
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static DataObject toDataObject(String content)
			throws JsonMappingException, JsonParseException, IOException {

		return INSTANCE.readValue(content, DataObject.class);

	}

	/**
	 *
	 * JSON文字列から {@link DataObject}を生成し、これのみを格納する
	 * DataCollectionを生成します。
	 *
	 * @param content  JSON文字列
	 * @return DataCollectionインスタンス
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static DataCollection toOneDataObjectContainedCollection(String content)
			throws JsonMappingException, JsonParseException, IOException {

		DataObject obj = INSTANCE.readValue(content, DataObject.class);
		DataCollection parameter = new DataCollection();
		parameter.add(obj);
		return parameter;

	}

}

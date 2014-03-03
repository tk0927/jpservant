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
package com.jpservant.core.kernel;

import java.io.IOException;
import java.net.URL;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

/**
 *
 * リクエストのコンテキスト情報を格納するクラスのインターフェイス定義。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface KernelContext {

	/**
	 *
	 * ユーザーのHttpリクエストパスURIを取得する。
	 *
	 * @return リクエストパスURI
	 */
	public String getRequestPath();

	/**
	 *
	 * HttpリクエストMethod名を得る。
	 *
	 * @return Method名
	 */
	public String getMethod();

	/**
	 *
	 * リクエスト(JSONオブジェクトのパース結果)を得る。
	 *
	 * @return リクエスト
	 */
	public DataCollection getParameters();


	/**
	 *
	 * 指定されたリソースのURLを得る。
	 *
	 * @param リソースパス
	 * @return URL
	 * @thrwos Exception 何らかの例外発生
	 */
	public URL getResource(String path)throws Exception;


	/**
	 *
	 * レスポンス(JSONオブジェクトの要素)を出力する。
	 *
	 *
	 * @param response レスポンス
	 * @throws  IOException 出力失敗
	 */
	public void writeResponse(DataCollection response)throws IOException;

	/**
	 *
	 * レスポンス(JSONオブジェクトの要素)を出力する。
	 *
	 *
	 * @param response レスポンス
	 * @throws  IOException 出力失敗
	 */
	public void writeResponse(DataObject response)throws IOException;


	/**
	 *
	 * モジュール側で後処理を登録する。
	 *
	 * @param processor 後処理ロジック
	 */
	public void addPostProcessor(PostProcessor processor);

	/**
	 *
	 * モジュール側でエラー時後処理を登録する。
	 *
	 * @param processor 後処理ロジック
	 */
	public void addErrorProcessor(PostProcessor processor);

}

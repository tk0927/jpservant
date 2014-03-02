package com.jpservant.core.kernel;

import java.io.IOException;
import java.net.URL;

import com.jpservant.core.common.DataCollection;

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
	 * モジュール側で後処理を登録する。
	 *
	 * @param processor 後処理ロジック
	 */
	public void addPostProcessor(PostProcessor processor);

}

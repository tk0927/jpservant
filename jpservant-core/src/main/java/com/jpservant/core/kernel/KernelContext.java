package com.jpservant.core.kernel;

import java.io.IOException;

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
	 * レスポンス(JSONオブジェクトの要素)を出力する。
	 *
	 *
	 * @param response レスポンス
	 * @throws  IOException 出力失敗
	 */
	public void writeResponse(DataCollection response)throws IOException;

}

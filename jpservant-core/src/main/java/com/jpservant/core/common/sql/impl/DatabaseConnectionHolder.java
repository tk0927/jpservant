package com.jpservant.core.common.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * 1つのデータベース接続を保管するクラスの共通インターフェイス定義。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface DatabaseConnectionHolder {

	/**
	 *
	 * データベースへ接続します。
	 *
	 * @throws SQLException 何らかの例外発生
	 */
	public void connect() throws SQLException;

	/**
	 *
	 * 格納するデータベースコネクションを取得します。
	 *
	 * @return JDBC データベースコネクション
	 */
	public Connection getConnection();

	/**
	 *
	 * データベース接続をクローズします。
	 *
	 * @throws SQLException 何らかの例外発生
	 */
	public void close() throws SQLException;


	/**
	 *
	 * 現在接続状態であるかのフラグを返します。
	 *
	 * @return 接続中ならtrue
	 */
	public boolean isConnected();
}

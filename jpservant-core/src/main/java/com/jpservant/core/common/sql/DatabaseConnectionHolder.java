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
package com.jpservant.core.common.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

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
	void connect() throws SQLException;

	/**
	 *
	 * 格納するデータベースコネクションを取得します。
	 *
	 * @return JDBC データベースコネクション
	 */
	Connection getConnection();

	/**
	 *
	 * データベース接続をクローズします。
	 *
	 * @throws SQLException 何らかの例外発生
	 */
	void close() throws SQLException;

	/**
	 *
	 * 現在接続状態であるかのフラグを返します。
	 *
	 * @return 接続中ならtrue
	 */
	boolean isConnected();

	/**
	 *
	 * トランザクションをコミットし、新たにトランザクションを開始します。
	 *
	 * @throws SQLException 何らかのSQL例外発生
	 */
	void commit() throws SQLException;

	/**
	 *
	 * トランザクションをロールバックし、新たにトランザクションを開始します。
	 *
	 * @throws SQLException 何らかのSQL例外発生
	 */
	void rollback() throws SQLException;

	/**
	 *
	 * セーブポイントを生成します。
	 *
	 * @return セーブポイント
	 * @throws SQLException
	 */
	Savepoint getSavepoint() throws SQLException;

	/**
	 *
	 * セーブポイントへのロールバックを行います。
	 *
	 * @param s セーブポイント
	 * @throws SQLException 何らかのSQL例外発生
	 */
	void rollbackSavepoint(Savepoint s) throws SQLException;

	/**
	 *
	 * セーブポイントを無効化します。
	 *
	 * @param s セーブポイント
	 * @throws SQLException 何らかのSQL例外発生
	 */
	void releaseSavepoint(Savepoint s) throws SQLException;

	/**
	 *
	 * 常駐プロセスにおけるセッション終了を行います。
	 *
	 *
	 * @throws Exception 何らかの例外発生
	 */
	void releaseSession() throws Exception;
}

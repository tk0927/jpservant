package com.jpservant.core.common.sql;

import java.sql.SQLException;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.sql.impl.DataCollectionDMLExecutor;
import com.jpservant.core.common.sql.impl.DataObjectSQLExecutor;

/**
 *
 * SQL実行に関するFacadeクラス。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class SQLProcessor {

	/** データベース接続保持オブジェクト */
	private DatabaseConnectionHolder holder;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param holder データベース接続保持オブジェクト
	 * @throws SQLException 何らかの例外発生
	 */
	public SQLProcessor(DatabaseConnectionHolder holder)throws SQLException{

		this.holder = holder;
		if(!this.holder.isConnected()){
			this.holder.connect();
		}

	}

	/**
	 *
	 * バインドパラメータを必要しない任意のSQLを実行します。
	 *
	 * @param sql SQL文
	 * @return 検索結果
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public DataCollection executeQuery(String sql)throws SQLException{

		return new DataObjectSQLExecutor(
				sql, this.holder.getConnection()).execute();

	}

	/**
	 *
	 * 任意のSQLを実行します。
	 *
	 * @param sql SQL文
	 * @param parameters バインドパラメータ集合
	 * @return 検索結果
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public DataCollection executeQuery(String sql,DataObject parameters)throws SQLException{

		return new DataObjectSQLExecutor(
				sql, this.holder.getConnection(),parameters).execute();

	}

	/**
	 *
	 * バインドパラメータを必要しない任意のDMLを実行します。
	 *
	 * @param dml DML文
	 * @return 更新件数
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public int executeUpdate(String dml)throws SQLException{

		return new DataCollectionDMLExecutor(
				dml, this.holder.getConnection()).execute()[0];

	}

	/**
	 *
	 * 任意のDMLによる1レコード更新を実行します。
	 *
	 * @param dml DML文
	 * @param row バインドパラメータ
	 * @return 更新件数
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public int executeUpdate(String dml,DataObject row)throws SQLException{

		DataCollection rows = new DataCollection();
		rows.add(row);
		return executeUpdate(dml,rows)[0];

	}

	/**
	 *
	 * 任意のDMLによるバルク更新を実行します。
	 *
	 * @param dml DML文
	 * @param rows バインドパラメータリスト
	 * @return 更新件数
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public int[] executeUpdate(String dml,DataCollection rows)throws SQLException{

		return new DataCollectionDMLExecutor(
				dml, this.holder.getConnection(),rows).execute();

	}

}

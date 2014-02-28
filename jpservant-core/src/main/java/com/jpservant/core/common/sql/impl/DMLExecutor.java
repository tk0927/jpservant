package com.jpservant.core.common.sql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * SQL DML文 ({@link java.sql.PreparedStatement#executeUpdate()}で実行可能なSQL）の実行
 * を簡素化するための共通基底クラス。
 *
 * 外部リソースのハンドラであるJDBCオブジェクトの生成・解放処理を隠蔽する。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 */
public abstract class DMLExecutor {

	/**
	 * SQL DML文
	 */
	private BindParameterParsedSQL dml;
	/**
	 * JDBCコネクション
	 */
	private Connection conn;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param dml SQL DML文
	 * @param conn JDBCコネクション
	 */
	public DMLExecutor(BindParameterParsedSQL dml,Connection conn){
		this.dml = dml;
		this.conn = conn;
	}

	/**
	 *
	 * SQL DML文を実行する。
	 *
	 * @return バッチリクエストごとの更新レコード数
	 * @throws SQLException 何らかのSQL例外が発生
	 */
	public int[] execute() throws SQLException{

		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(dml.getParsedSQL());

			bindParameters(ps);

			return ps.executeBatch();

		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
				}
			}
			if(ps != null){
				try{
					ps.close();
				}catch(Exception e){
				}
			}
		}

	};
	/**
	 *
	 * SQL解析処理オブジェクトを取得する。
	 *
	 * @return SQL解析処理オブジェクト
	 */
	protected BindParameterParsedSQL getBindParameterParsedSQL(){
		return dml;
	}

	/**
	 *
	 * {@link PreparedStatement}へのパラメータバインドを実装する位置。
	 *
	 * @param ps JDBC PreparedStatement
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public abstract void bindParameters(PreparedStatement ps) throws SQLException;

}

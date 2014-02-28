package com.jpservant.core.common.sql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * Query文 ({@link java.sql.PreparedStatement#executeQuery()}で実行可能なSQL）の実行
 * を簡素化するための共通基底クラス。
 *
 * 外部リソースのハンドラであるJDBCオブジェクトの生成・解放処理を隠蔽する。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 */
public abstract class SQLExecutor<T>{

	/**
	 * SQL Query文
	 */
	private BindParameterParsedSQL sql;
	/**
	 * JDBCコネクション
	 */
	private Connection conn;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL Query文
	 * @param conn JDBCコネクション
	 */
	public SQLExecutor(BindParameterParsedSQL sql,Connection conn){
		this.sql = sql;
		this.conn = conn;
	}

	/**
	 *
	 * SQL Query文を実行する。
	 *
	 * @return SQL Query文実行結果（{@link #readResultSet(ResultSet)}の戻り値）
	 * @throws SQLException 何らかのSQL例外が発生
	 */
	public T execute() throws SQLException{

		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = this.conn.prepareStatement(this.sql.getParsedSQL());

			bindParameters(ps);

			rs = ps.executeQuery();

			return readResultSet(rs);

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
		return sql;
	}

	/**
	 *
	 * {@link PreparedStatement}へのパラメータバインドを実装する位置。
	 *
	 * @param ps JDBC PreparedStatement
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public abstract void bindParameters(PreparedStatement ps) throws SQLException;

	/**
	 *
	 * {@link ResultSet}の読み取り処理を実装する位置。
	 *
	 * @param rs JDBC ResultSet
	 * @return 読み取り結果を格納するオブジェクト
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public abstract T readResultSet(ResultSet rs) throws SQLException;

}

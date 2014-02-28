package com.jpservant.core.common.sql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

/**
 *
 * {@link DataObject}をバインドパラメータ集合として取り扱い、
 * 検索結果を{@link DataCollection}に格納して返却するSQL Query実行オブジェクト。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class DataObjectSQLExecutor extends SQLExecutor<DataCollection> {

	/** バインドパラメータ */
	private DataObject parameters;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL文
	 * @param conn データベースコネクション
	 */
	public DataObjectSQLExecutor(String sql,Connection conn){
		this(sql,conn,null);
	}

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL文
	 * @param conn データベースコネクション
	 * @param parameters バインドパラメータ
	 */
	public DataObjectSQLExecutor(String sql,Connection conn,DataObject parameters){

		super(new BindParameterParsedSQL(sql),conn);
		this.parameters = parameters;

	}

	/**
	 * 型の取り扱いが面倒なので、パラメータがあればすべてString型でバインドする。
	 */
	@Override
	public void bindParameters(PreparedStatement ps) throws SQLException {

		if(this.parameters != null){
			for(Map.Entry<String, Object> entry : this.parameters.entrySet()){
				getBindParameterParsedSQL().bind(
						ps,entry.getKey(),entry.getValue());
			}
		}

	}

	/**
	 * 型の取り扱いが面倒なので、すべてString型でフェッチする。
	 */
	@Override
	public DataCollection readResultSet(ResultSet rs) throws SQLException {

		DataCollection result = new DataCollection();
		ResultSetMetaData rsm = rs.getMetaData();
		while(rs.next()){

			DataObject row = new DataObject();
			for(int i = 1 ; i <= rsm.getColumnCount() ; i++){
				row.put(rsm.getColumnName(i),rs.getString(i));
			}

			result.add(row);

		}
		return result;
	}
}

package com.jpservant.core.common.sql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

/**
*
* {@link DataCollection}を行データ集合として取り扱い、
* バルク更新処理を行うSQL DML実行オブジェクト。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class DataCollectionDMLExecutor extends DMLExecutor {

	/** バインドパラメータ集合リスト */
	private DataCollection parameterrows;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL文
	 * @param conn データベースコネクション
	 */
	public DataCollectionDMLExecutor(String sql,Connection conn){
		this(sql, conn, null);
	}

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param sql SQL文
	 * @param conn データベースコネクション
	 * @param parameterrows バインドパラメータ
	 */
	public DataCollectionDMLExecutor(String sql,Connection conn,DataCollection parameterrows){
		super(new BindParameterParsedSQL(sql),conn);
		this.parameterrows = parameterrows;
	}

	/**
	 * 型の取り扱いが面倒なので、パラメータがあればすべてString型でバインドする。
	 */
	@Override
	public void bindParameters(PreparedStatement ps) throws SQLException {

		if(this.parameterrows != null){

			for(DataObject row : this.parameterrows){
				for(Map.Entry<String, Object> entry : row.entrySet()){
					getBindParameterParsedSQL().bind(
							ps,entry.getKey(),entry.getValue());
				}
				ps.addBatch();
			}
		}else{
			ps.addBatch();
		}
	}

}

package com.jpservant.core.common.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.jpservant.core.common.DataCollection;

public class DataCollectionDMLExecutor extends DMLExecutor {

	private DataCollection data;

	public DataCollectionDMLExecutor(BindParameterParsedSQL sql,Connection conn,DataCollection data){
		super(sql,conn);
		this.data = data;
	}

	@Override
	public void bindParameters(PreparedStatement ps) throws SQLException {
		// TODO 自動生成されたメソッド・スタブ

	}

}

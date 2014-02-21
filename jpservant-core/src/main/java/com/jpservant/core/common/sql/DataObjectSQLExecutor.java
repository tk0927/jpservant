package com.jpservant.core.common.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

public class DataObjectSQLExecutor extends SQLExecutor<DataCollection> {

	private DataObject data;

	public DataObjectSQLExecutor(BindParameterParsedSQL sql,Connection conn,DataObject data){
		super(sql,conn);
		this.data = data;
	}

	@Override
	public void bindParameters(PreparedStatement ps) throws SQLException {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public DataCollection readResultSet(ResultSet rs) throws SQLException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}

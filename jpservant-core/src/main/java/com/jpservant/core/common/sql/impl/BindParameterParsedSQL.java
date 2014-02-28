package com.jpservant.core.common.sql.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL文のバインドパラメータ位置解析オブジェクト。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class BindParameterParsedSQL {

	/** パラメータ文字列検出用の正規表現 */
	private static final String PARAMETER_REGEXP = ":([A-Za-z0-9_]+)";

	/** 解析前のSQL文 */
	private String definedsql;

	/** JDBCにて実行可能なSQL文 */
	private String parsedsql;

	/** バインドパラメータの位置情報格納マップ */
	private LinkedHashMap<String,Integer> positionmap = new LinkedHashMap<String,Integer>();


	/**
	 *
	 * コンストラクタ
	 *
	 * @param definedsql SQL文
	 */
	public BindParameterParsedSQL(String definedsql){
		this.definedsql = definedsql;
		parse();
	}

	/**
	 *
	 * SQL文を取得する。
	 *
	 * @return SQL文
	 */
	public String getDefinedSQL(){
		return this.definedsql;
	}

	/**
	 *
	 * JDBCにて実行可能なSQL文を取得する。
	 *
	 *
	 * @return JDBCにて実行可能なSQL文
	 */
	public String getParsedSQL(){
		return this.parsedsql;
	}

	/**
	 *
	 * バインドパラメータ名リストを得る。
	 *
	 * @return バインドパラメータリスト
	 */
	public Iterator<String> getBindParameterNames(){
		return this.positionmap.keySet().iterator();
	}

	/**
	 *
	 * 特定バインドパラメータ名の、SQL文上での位置情報を得る。
	 *
	 * @param name バインドパラメータ名
	 * @return パラメータの位置(パラメータ名が無ければ-1)
	 */
	public int getParameterPosition(String name){

		Integer position = this.positionmap.get(name);
		return ( position == null ? -1 : position);

	}

	/**
	 *
	 * SQL文のパース処理。
	 *
	 */
	private void parse(){

		Pattern pattern = Pattern.compile(PARAMETER_REGEXP);
		Matcher matcher = pattern.matcher(definedsql);

		int position = 0;
		int number = 1;
		while(matcher.find(position)){
			this.positionmap.put(matcher.group(1), number++);
			position = matcher.end();
		}

		this.parsedsql = this.definedsql.replaceAll(PARAMETER_REGEXP, "?");

	}

	/**
	 *
	 * パラメータをSQL内部の適正位置にバインドする。
	 *
	 * <pre>
	 * 型の取り扱いが面倒なので、すべてString型でバインドする。
	 * </pre>
	 * @param ps ステートメント
	 * @param name パラメータ名
	 * @param value パラメータ値
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public void bind(PreparedStatement ps,String name,Object value)
		throws SQLException{

		ps.setString(this.getParameterPosition(name),value.toString());

	}
}

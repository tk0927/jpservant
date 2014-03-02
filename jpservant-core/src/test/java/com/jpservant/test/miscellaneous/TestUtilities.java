package com.jpservant.test.miscellaneous;

import java.util.Properties;

import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.common.sql.SQLProcessorTest;
import com.jpservant.core.common.sql.impl.DriverManagerConnectionHolder;

/**
 *
 * テスト用のユーティリティ。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class TestUtilities {

	/** データベースコネクション保持オブジェクト */
	public static final DatabaseConnectionHolder CONNECTION_HOLDER;

	/**
	 *
	 * 初期化。設定情報を用いてデータベース接続を行う。
	 *
	 * @throws Exception 何らかの例外発生
	 */
	static{
		try{

			Properties p = new Properties();
			p.load(SQLProcessorTest.class.getResourceAsStream("/configurations/test.properties"));

			CONNECTION_HOLDER = new DriverManagerConnectionHolder(
					p.getProperty("driver"),
					p.getProperty("url"),
					p.getProperty("username"),
					p.getProperty("password"));

		}catch(Exception e){

			throw new RuntimeException(e);

		}

	}

}

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

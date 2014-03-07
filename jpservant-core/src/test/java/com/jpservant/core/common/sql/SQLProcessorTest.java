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
package com.jpservant.core.common.sql;

import static com.jpservant.test.miscellaneous.TestUtilities.*;
import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;

/**
 *
 * {@link SQLProcessor}のテストクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class SQLProcessorTest {

	/**
	 *
	 * 各種SQLの実行テスト。
	 *
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void testExecution() throws Exception {

		SQLProcessor processor = new SQLProcessor(CONNECTION_HOLDER);

		executeTestSQL(processor);

		//トランザクションロールバック(DB状態復元)
		CONNECTION_HOLDER.rollback();

	}

	/**
	 *
	 * SQL実行の本体。外部からの呼び出しに備えSTATICメソッド化。
	 *
	 * @param processor SQLProcessor
	 * @throws SQLException 何らかのSQL例外発生
	 */
	public static void executeTestSQL(SQLProcessor processor) throws SQLException {

		//テーブルクリア
		processor.executeUpdate("DELETE FROM T_SAMPLE_TEST_TABLE");

		//テスト用データの挿入
		DataCollection rows = new DataCollection();
		DataObject row1 = new DataObject();
		row1.put("StringCol", "AAA");
		row1.put("TimestampCol", "2014-01-01 00:00:01.0");
		row1.put("NumberCol", "1024");
		row1.put("BoolCol", "0");
		rows.add(row1);

		DataObject row2 = new DataObject();
		row2.put("StringCol", "BBB");
		row2.put("TimestampCol", null);
		row2.put("NumberCol", null);
		row2.put("BoolCol", null);
		rows.add(row2);

		processor.executeUpdate(
				"INSERT INTO T_SAMPLE_TEST_TABLE(STRING_COL,TIMESTAMP_COL,NUMBER_COL,BOOL_COL)" +
						" VALUES(:StringCol,:TimestampCol,:NumberCol,:BoolCol)", rows);

		//テスト用データの検索結果確認
		DataObject criteria1 = new DataObject();
		criteria1.put("StringCol", "AAA");
		DataCollection result1 = processor.executeQuery(
				"SELECT * FROM T_SAMPLE_TEST_TABLE WHERE STRING_COL = :StringCol", criteria1);
		assertEquals(result1.size(), 1);
		DataObject resultrow1 = result1.get(0);
		assertEquals("AAA", resultrow1.get("StringCol"));
		assertEquals("2014-01-01 00:00:01.0", resultrow1.get("TimestampCol"));
		assertEquals("1024", resultrow1.get("NumberCol"));
		assertEquals("0", resultrow1.get("BoolCol"));

		DataObject criteria2 = new DataObject();
		criteria2.put("StringCol", "BBB");
		DataCollection result2 = processor.executeQuery(
				"SELECT * FROM T_SAMPLE_TEST_TABLE WHERE STRING_COL = :StringCol", criteria2);
		assertEquals(result2.size(), 1);
		DataObject resultrow2 = result2.get(0);
		assertEquals("BBB", resultrow2.get("StringCol"));
		assertNull(resultrow2.get("TimestampCol"));
		assertNull(resultrow2.get("NumberCol"));
		assertNull(resultrow2.get("BoolCol"));

	}

}

package com.jpservant.core.common.sql;

import static com.jpservant.test.miscellaneous.TestUtilities.*;

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
	public void testExecution() throws Exception{

		SQLProcessor processor = new SQLProcessor( CONNECTION_HOLDER );

		processor.executeUpdate("DELETE FROM T_SAMPLE_TEST_TABLE");

		DataCollection rows = new DataCollection();
		DataObject row1 = new DataObject();
		row1.put("StringCol", "AAA");
		row1.put("TimestampCol", "2014-01-01 00:00:01");
		row1.put("NumberCol", 1);
		row1.put("BoolCol", 0);
		rows.add(row1);

		DataObject row2 = new DataObject();
		row2.put("StringCol", "BBB");
		row2.put("TimestampCol", null);
		row2.put("NumberCol", null);
		row2.put("BoolCol", null);
		rows.add(row2);

		processor.executeUpdate(
				"INSERT INTO T_SAMPLE_TEST_TABLE(STRING_COL,TIMESTAMP_COL,NUMBER_COL,BOOL_COL)" +
				" VALUES(:StringCol,:TimestampCol,:NumberCol,:BoolCol)",rows);

		DataCollection result = processor.executeQuery(
				"SELECT * FROM T_SAMPLE_TEST_TABLE WHERE STRING_COL = :StringCol", row1);
		System.out.println("result1=" + result);

		DataCollection result2 = processor.executeQuery(
				"SELECT * FROM T_SAMPLE_TEST_TABLE WHERE STRING_COL = :StringCol", row2);
		System.out.println("result2=" + result2);

		CONNECTION_HOLDER.commit();
	}

}

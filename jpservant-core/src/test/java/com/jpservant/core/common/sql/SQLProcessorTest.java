package com.jpservant.core.common.sql;

import static com.jpservant.test.miscellaneous.TestUtilities.*;
import static junit.framework.Assert.*;

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
		row1.put("STRING_COL", "AAA");
		row1.put("TIMESTAMP_COL", "2014-01-01 00:00:01.0");
		row1.put("NUMBER_COL", "1024");
		row1.put("BOOL_COL", "0");
		rows.add(row1);

		DataObject row2 = new DataObject();
		row2.put("STRING_COL", "BBB");
		row2.put("TIMESTAMP_COL", null);
		row2.put("NUMBER_COL", null);
		row2.put("BOOL_COL", null);
		rows.add(row2);

		processor.executeUpdate(
				"INSERT INTO T_SAMPLE_TEST_TABLE(STRING_COL,TIMESTAMP_COL,NUMBER_COL,BOOL_COL)" +
				" VALUES(:STRING_COL,:TIMESTAMP_COL,:NUMBER_COL,:BOOL_COL)",rows);

		DataCollection result1 = processor.executeQuery(
				"SELECT * FROM T_SAMPLE_TEST_TABLE WHERE STRING_COL = :STRING_COL", row1);
		assertEquals(result1.size(),1);
		DataObject resultrow1 = result1.get(0);
		assertEquals(resultrow1.get("STRING_COL"),"AAA");
		assertEquals(resultrow1.get("TIMESTAMP_COL"),"2014-01-01 00:00:01.0");
		assertEquals(resultrow1.get("NUMBER_COL"),"1024");
		assertEquals(resultrow1.get("BOOL_COL"),"0");


		DataCollection result2 = processor.executeQuery(
				"SELECT * FROM T_SAMPLE_TEST_TABLE WHERE STRING_COL = :STRING_COL", row2);
		assertEquals(result2.size(),1);
		DataObject resultrow2 = result2.get(0);
		assertEquals(resultrow2.get("STRING_COL"),"BBB");
		assertNull(resultrow2.get("TIMESTAMP_COL"));
		assertNull(resultrow2.get("NUMBER_COL"));
		assertNull(resultrow2.get("BOOL_COL"));

		CONNECTION_HOLDER.rollback();

	}

}

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

		SQLProcessor processor = new SQLProcessor(CONNECTION_HOLDER );

		processor.executeUpdate("DELETE FROM T_SAMPLE_TEST_TABLE");

		DataCollection rows = new DataCollection();
		DataObject row1 = new DataObject();
		row1.put("StringCol", "AAA");
		rows.add(row1);

		DataObject row2 = new DataObject();
		row2.put("StringCol", "BBB");
		rows.add(row2);

		processor.executeUpdate("INSERT INTO T_SAMPLE_TEST_TABLE(STRING_COL) VALUES(:StringCol)",rows);

		DataCollection result = processor.executeQuery("SELECT * FROM T_SAMPLE_TEST_TABLE");
		System.out.println(result);

		processor.commit();
	}

}

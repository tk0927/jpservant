package com.jpservant.core.module.sql;

import static junit.framework.Assert.*;

import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.kernel.impl.KernelContextImpl;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.test.miscellaneous.JDBCResource1TestCaseBase;

/**
*
* {@link SQLModulePlatformTest}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class SQLModulePlatformTest extends JDBCResource1TestCaseBase {

	/**
	 *
	 * src/test/resources/configurations/root.jsonの設定値に基づく{@link SQLModulePlatform}の初期化/稼働テスト
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void testSQLModulePlatformExecution() throws Exception{

		ObjectMapper mapper = new ObjectMapper();

		ModulePlatform module = MANAGER.getModulePlatform("/sql");

		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl("/TSampleTestTable/DeleteAll","POST", null ,sw));

		//テスト用データの挿入
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
		sw = new StringWriter();
		module.execute(new KernelContextImpl("/TSampleTestTable/Insert","POST", rows ,sw));

		//テスト用データの検索結果確認
		DataCollection criteriarows1 = new DataCollection();
		DataObject criteria1 = new DataObject();
		criteria1.put("STRING_COL", "AAA");
		criteriarows1.add(criteria1);
		sw = new StringWriter();
		module.execute(new KernelContextImpl("/TSampleTestTable/SelectByStringCol","POST", criteriarows1 ,sw));

		DataCollection result1 = mapper.readValue(sw.toString(), DataCollection.class);
		assertEquals(result1.size(),1);
		DataObject resultrow1 = result1.get(0);
		assertEquals(resultrow1.get("STRING_COL"),"AAA");
		assertEquals(resultrow1.get("TIMESTAMP_COL"),"2014-01-01 00:00:01.0");
		assertEquals(resultrow1.get("NUMBER_COL"),"1024");
		assertEquals(resultrow1.get("BOOL_COL"),"0");

		//テスト用データの検索結果確認
		DataCollection criteriarows2 = new DataCollection();
		DataObject criteria2 = new DataObject();
		criteria2.put("STRING_COL", "BBB");
		criteriarows2.add(criteria2);
		sw = new StringWriter();
		module.execute(new KernelContextImpl("/TSampleTestTable/SelectByStringCol","POST", criteriarows2 ,sw));

		DataCollection result2 = mapper.readValue(sw.toString(), DataCollection.class);
		assertEquals(result2.size(),1);
		DataObject resultrow2 = result2.get(0);
		assertEquals(resultrow2.get("STRING_COL"),"BBB");
		assertNull(resultrow2.get("TIMESTAMP_COL"));
		assertNull(resultrow2.get("NUMBER_COL"));
		assertNull(resultrow2.get("BOOL_COL"));
	}


}

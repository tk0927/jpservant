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
package com.jpservant.core.module.dao;

import static com.jpservant.core.common.JacksonUtils.*;
import static org.junit.Assert.*;

import java.io.StringWriter;

import org.junit.Test;

import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.kernel.impl.KernelContextImpl;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.core.module.sql.QueryModulePlatform;
import com.jpservant.test.miscellaneous.JDBCResource1TestCaseBase;

/**
*
* {@link DAOModulePlatform}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class DAOModulePlatformTest extends JDBCResource1TestCaseBase {

	/**
	 *
	 * src/test/resources/configurations/root.jsonの設定値に基づく{@link QueryModulePlatform}の初期化/稼働テスト
	 *
	 * <pre>
	 * クラスパス上の資源読み取り設定。
	 * </pre>
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void testDAOModulePlatformExecution() throws Exception {

		execute("/dao");

	}

	/**
	 *
	 * テスト用初期化処理
	 */
	private void execute(String path) throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform(path);

		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable", "DELETE", null, null, sw));

		//テスト用データの挿入
		DataCollection rows = new DataCollection();
		DataObject row1 = new DataObject();
		row1.put("Id", "1");
		row1.put("StringCol", "AAA");
		row1.put("TimestampCol", "2014-01-01 00:00:01.0");
		row1.put("NumberCol", "1024");
		row1.put("BoolCol", "0");
		rows.add(row1);

		DataObject row2 = new DataObject();
		row2.put("Id", "2");
		row2.put("StringCol", "BBB");
		row2.put("TimestampCol", null);
		row2.put("NumberCol", null);
		row2.put("BoolCol", null);
		rows.add(row2);
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable", "POST", rows, null, sw));

		//全件検索
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable", "GET", null, null, sw));

		DataCollection result = toDataCollection(sw.toString());
		assertEquals(2, result.size());

		for (DataObject obj : result) {
			if ("AAA".equals(obj.get("StringCol"))) {
				assertEquals("1", obj.get("Id"));
				assertEquals("2014-01-01 00:00:01.0", obj.get("TimestampCol"));
				assertEquals("1024", obj.get("NumberCol"));
				assertEquals("0", obj.get("BoolCol"));
			}
			else if ("BBB".equals(obj.get("StringCol"))) {
				assertEquals("2", obj.get("Id"));
				assertNull(obj.get("TimestampCol"));
				assertNull(obj.get("NumberCol"));
				assertNull(obj.get("BoolCol"));
			} else {
				fail();
			}
		}
		/*
				//PK指定検索
				sw = new StringWriter();
				module.execute(new KernelContextImpl(
						"/TSampleTestTable/1", "GET", null, null, sw));
				result = toDataCollection(sw.toString());
				assertEquals(1, result.size());

				assertEquals("1", result.get(0).get("Id"));
				assertEquals("AAA", result.get(0).get("StringCol"));
				assertEquals("2014-01-01 00:00:01.0", result.get(0).get("TimestampCol"));
				assertEquals("1024", result.get(0).get("NumberCol"));
				assertEquals("0", result.get(0).get("BoolCol"));

				sw = new StringWriter();
				module.execute(new KernelContextImpl(
						"/TSampleTestTable/2", "GET", null, null, sw));
				result = toDataCollection(sw.toString());
				assertEquals(1, result.size());

				assertEquals("2", result.get(0).get("Id"));
				assertEquals("BBB", result.get(0).get("StringCol"));
				assertNull(result.get(0).get("TimestampCol"));
				assertNull(result.get(0).get("NumberCol"));
				assertNull(result.get(0).get("BoolCol"));
		*/

		//行カウント
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Count", "GET", null, null, sw));
		DataObject result2 = toDataObject(sw.toString());
		assertEquals("2", result2.get("Count"));

	}

}

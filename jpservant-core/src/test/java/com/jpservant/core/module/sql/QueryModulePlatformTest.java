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
package com.jpservant.core.module.sql;

import static org.junit.Assert.*;

import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.Constant;
import com.jpservant.core.common.Constant.ResourceType;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.kernel.impl.KernelContextImpl;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.test.miscellaneous.JDBCResource1TestCaseBase;

/**
*
* {@link QueryModulePlatformTest}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class QueryModulePlatformTest extends JDBCResource1TestCaseBase {

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
	public void testQueryModulePlatformExecution() throws Exception{

		execute("/sql");

	}

	/**
	 *
	 * src/test/resources/configurations/root.jsonの設定値に基づく{@link QueryModulePlatform}の初期化/稼働テスト
	 *
	 * <pre>
	 * ファイルシステム上の資源読み取り設定。
	 * </pre>
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void testQueryModulePlatformExecution2() throws Exception{

		execute("/sql2");
	}

	/**
	 *
	 * SQL実行テストの一シナリオ。
	 *
	 * @param rootpath 設定名
	 * @throws Exception 何らかの例外発生
	 */
	private void execute(String rootpath) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		ModulePlatform module = MANAGER.getModulePlatform(rootpath);
		String strtype = (String)MANAGER.getModuleConfiguration(rootpath).get(
				Constant.ConfigurationName.ResourceType.name());
		ResourceType type = ResourceType.valueOf(strtype);

		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/DeleteAll","POST", null ,type.getInstance(),sw));

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
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Insert","POST", rows ,type.getInstance(),sw));

		//テスト用データの検索結果確認
		DataCollection criteriarows1 = new DataCollection();
		DataObject criteria1 = new DataObject();
		criteria1.put("StringCol", "AAA");
		criteriarows1.add(criteria1);
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/SelectByStringCol","POST", criteriarows1 ,type.getInstance(),sw));

		DataObject result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("AAA",						result.get("STRING_COL"));
		assertEquals("2014-01-01 00:00:01.0",	result.get("TIMESTAMP_COL"));
		assertEquals("1024",					result.get("NUMBER_COL"));
		assertEquals("0",						result.get("BOOL_COL"));

		//テスト用データの検索結果確認
		DataCollection criteriarows2 = new DataCollection();
		DataObject criteria2 = new DataObject();
		criteria2.put("StringCol", "BBB");
		criteriarows2.add(criteria2);
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/SelectByStringCol","POST", criteriarows2 ,type.getInstance(),sw));

		DataObject result2 = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("BBB",						result2.get("STRING_COL"));
		assertNull(result2.get("TIMESTAMP_COL"));
		assertNull(result2.get("NUMBER_COL"));
		assertNull(result2.get("BOOL_COL"));
	}

}

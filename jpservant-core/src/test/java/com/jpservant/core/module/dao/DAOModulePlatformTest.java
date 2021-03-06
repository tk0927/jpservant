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
import com.jpservant.test.miscellaneous.TestCaseBase;

/**
*
* {@link DAOModulePlatform}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class DAOModulePlatformTest extends TestCaseBase {

	/**
	 *
	 * テスト用データ初期化
	 *
	 * @param module
	 * @throws Exception
	 */
	private void initData(ModulePlatform module) throws Exception {

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

		row1 = new DataObject();
		row1.put("Id", "2");
		row1.put("StringCol", "BBB");
		row1.put("TimestampCol", "2015-01-01 00:00:01.0");
		row1.put("NumberCol", "1025");
		row1.put("BoolCol", "1");
		rows.add(row1);

		row1 = new DataObject();
		row1.put("Id", "3");
		row1.put("StringCol", "CCC");
		row1.put("TimestampCol", "2016-01-01 00:00:01.0");
		row1.put("NumberCol", "1026");
		row1.put("BoolCol", "0");
		rows.add(row1);

		row1 = new DataObject();
		row1.put("Id", "4");
		row1.put("StringCol", "DDD");
		row1.put("TimestampCol", "2017-01-01 00:00:01.0");
		row1.put("NumberCol", "1027");
		row1.put("BoolCol", "1");
		rows.add(row1);
		sw = new StringWriter();

		module.execute(new KernelContextImpl(
				"/TSampleTestTable", "POST", rows, null, sw));
	}

	/**
	 *
	 * src/test/resources/configurations/root.jsonの設定値に基づく{@link DAOModulePlatform}の初期化/稼働テスト
	 *
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void execute1() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/dao");

		initData(module);

		//全件検索
		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable", "GET", null, null, sw));

		DataCollection result = toDataCollection(sw.toString());
		assertEquals(4, result.size());

		for (DataObject obj : result) {
			if ("AAA".equals(obj.get("StringCol"))) {
				assertEquals("1", obj.get("Id"));
				assertEquals("2014-01-01 00:00:01.0", obj.get("TimestampCol"));
				assertEquals("1024", obj.get("NumberCol"));
				assertEquals("0", obj.get("BoolCol"));
			}
			else if ("BBB".equals(obj.get("StringCol"))) {
				assertEquals("2", obj.get("Id"));
				assertEquals("2015-01-01 00:00:01.0", obj.get("TimestampCol"));
				assertEquals("1025", obj.get("NumberCol"));
				assertEquals("1", obj.get("BoolCol"));
			}
			else if ("CCC".equals(obj.get("StringCol"))) {
				assertEquals("3", obj.get("Id"));
				assertEquals("2016-01-01 00:00:01.0", obj.get("TimestampCol"));
				assertEquals("1026", obj.get("NumberCol"));
				assertEquals("0", obj.get("BoolCol"));
			}
			else if ("DDD".equals(obj.get("StringCol"))) {
				assertEquals("4", obj.get("Id"));
				assertEquals("2017-01-01 00:00:01.0", obj.get("TimestampCol"));
				assertEquals("1027", obj.get("NumberCol"));
				assertEquals("1", obj.get("BoolCol"));
			} else {
				fail();
			}
		}
	}

	@Test
	public void execute2() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/dao");

		initData(module);

		//PK指定検索
		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/1", "GET", null, null, sw));
		DataObject result2 = toDataObject(sw.toString());
		assertEquals("1", result2.get("Id"));
		assertEquals("AAA", result2.get("StringCol"));
		assertEquals("2014-01-01 00:00:01.0", result2.get("TimestampCol"));
		assertEquals("1024", result2.get("NumberCol"));
		assertEquals("0", result2.get("BoolCol"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/2", "GET", null, null, sw));
		result2 = toDataObject(sw.toString());
		assertEquals("2", result2.get("Id"));
		assertEquals("BBB", result2.get("StringCol"));
		assertEquals("2015-01-01 00:00:01.0", result2.get("TimestampCol"));
		assertEquals("1025", result2.get("NumberCol"));
		assertEquals("1", result2.get("BoolCol"));

	}

	@Test
	public void execute3() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/dao");

		initData(module);

		//行カウント
		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Count", "GET", null, null, sw));
		DataObject result2 = toDataObject(sw.toString());
		assertEquals("4", result2.get("Count"));

	}

	@Test
	public void execute4() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/dao");

		initData(module);

		//PK指定更新
		//テスト用データの挿入
		DataCollection rows2 = new DataCollection();
		DataObject row3 = new DataObject();
		row3.put("Id", "1");
		row3.put("StringCol", "XXX");
		row3.put("TimestampCol", "2015-01-01 00:00:01.0");
		row3.put("NumberCol", "2048");
		row3.put("BoolCol", "1");
		rows2.add(row3);

		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/1", "PUT", rows2, null, sw));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/1", "GET", null, null, sw));
		DataObject result3 = toDataObject(sw.toString());
		assertEquals("1", result3.get("Id"));
		assertEquals("XXX", result3.get("StringCol"));
		assertEquals("2015-01-01 00:00:01.0", result3.get("TimestampCol"));
		assertEquals("2048", result3.get("NumberCol"));
		assertEquals("1", result3.get("BoolCol"));

	}

	@Test
	public void execute5() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/dao");

		initData(module);

		//一件削除
		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/1", "DELETE", null, null, sw));
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Count", "GET", null, null, sw));
		DataObject result2 = toDataObject(sw.toString());
		assertEquals("3", result2.get("Count"));

	}

	@Test
	public void execute6() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/dao");

		initData(module);

		//条件指定検索
		DataCollection rows1 = new DataCollection();
		DataObject row1 = new DataObject();
		row1.put("StringCol", "DDD");
		rows1.add(row1);
		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Select", "POST", rows1, null, sw));

		DataObject result = toDataObject(sw.toString());
		assertEquals("4", result.get("Id"));
		assertEquals("DDD", result.get("StringCol"));
		assertEquals("2017-01-01 00:00:01.0", result.get("TimestampCol"));
		assertEquals("1027", result.get("NumberCol"));
		assertEquals("1", result.get("BoolCol"));

		//条件指定検索
		rows1 = new DataCollection();
		row1 = new DataObject();
		row1.put("TimestampCol", "2016-01-01 00:00:01.0");
		rows1.add(row1);
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Select", "POST", rows1, null, sw));

		result = toDataObject(sw.toString());
		assertEquals("3", result.get("Id"));
		assertEquals("CCC", result.get("StringCol"));
		assertEquals("2016-01-01 00:00:01.0", result.get("TimestampCol"));
		assertEquals("1026", result.get("NumberCol"));
		assertEquals("0", result.get("BoolCol"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Count", "POST", rows1, null, sw));
		result = toDataObject(sw.toString());
		assertEquals("1", result.get("Count"));

	}

	@Test
	public void execute7() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/dao");

		initData(module);

		//条件指定削除
		DataCollection rows1 = new DataCollection();
		DataObject row1 = new DataObject();
		row1.put("TimestampCol", "2016-01-01 00:00:01.0");
		rows1.add(row1);
		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Delete", "POST", rows1, null, sw));
		DataObject result = toDataObject(sw.toString());
		assertEquals("1", result.get("Count"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Select", "POST", rows1, null, sw));

		result = toDataObject(sw.toString());
		assertEquals(0, result.size());

	}

	@Test
	public void execute8() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/dao");

		initData(module);

		//条件指定更新
		DataCollection rows1 = new DataCollection();
		DataObject row1 = new DataObject();
		row1.put("CriteriaStringCol", "DDD");
		row1.put("Id", "104");
		row1.put("StringCol", "XXX");
		row1.put("TimestampCol", "1998-01-01 00:00:01.0");
		row1.put("NumberCol", "4096");
		row1.put("BoolCol", "0");
		rows1.add(row1);
		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Update", "POST", rows1, null, sw));
		DataObject result = toDataObject(sw.toString());
		assertEquals("1", result.get("Count"));

		//条件指定検索
		rows1 = new DataCollection();
		row1 = new DataObject();
		row1.put("StringCol", "XXX");
		rows1.add(row1);
		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Select", "POST", rows1, null, sw));

		result = toDataObject(sw.toString());
		assertEquals("104", result.get("Id"));
		assertEquals("XXX", result.get("StringCol"));
		assertEquals("1998-01-01 00:00:01.0", result.get("TimestampCol"));
		assertEquals("4096", result.get("NumberCol"));
		assertEquals("0", result.get("BoolCol"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/TSampleTestTable/Count", "POST", rows1, null, sw));
		result = toDataObject(sw.toString());
		assertEquals("1", result.get("Count"));

	}

}

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
package com.jpservant.core.module.filesystem;

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
* {@link FileSystemModulePlatform}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class FileSystemModulePlatformTest extends TestCaseBase {

	@Test
	public void execute1() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/filesystem");

		//テスト用データ生成
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

		module.execute(new KernelContextImpl(
				"/sample/sample1", "POST", rows, null, null));

		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/sample/sample1", "GET", null, null, sw));

		DataCollection result = toDataCollection(sw.toString());
		assertEquals(4, result.size());
	}

	@Test
	public void execute2() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/filesystem");

		//テスト用データ生成
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

		module.execute(new KernelContextImpl(
				"/sample/sample2", "POST", rows, null, null));

		rows = new DataCollection();
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

		module.execute(new KernelContextImpl(
				"/sample/sample2", "PUT", rows, null, null));

		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/sample/sample2", "GET", null, null, sw));

		DataCollection result = toDataCollection(sw.toString());
		assertEquals(4, result.size());
	}

	@Test
	public void execute3() throws Exception {

		ModulePlatform module = MANAGER.getModulePlatform("/filesystem");

		//テスト用データ生成
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

		module.execute(new KernelContextImpl(
				"/sample/sample3", "POST", rows, null, null));

		module.execute(new KernelContextImpl(
				"/sample/sample3", "DELETE", null, null, null));

		rows = new DataCollection();
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

		module.execute(new KernelContextImpl(
				"/sample/sample3", "PUT", rows, null, null));

		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/sample/sample3", "GET", null, null, sw));

		DataCollection result = toDataCollection(sw.toString());
		assertEquals(2, result.size());
	}
}

package com.jpservant.core.module.mock;

import static org.junit.Assert.*;

import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.Constant;
import com.jpservant.core.common.Constant.ResourceType;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.kernel.impl.KernelContextImpl;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.core.module.sql.QueryModulePlatform;
import com.jpservant.test.miscellaneous.JDBCResource1TestCaseBase;

/**
*
* {@link MockModulePlatformTest}のテストクラス。
*
* @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
* @version 0.1
*
*/
public class MockModulePlatformTest extends JDBCResource1TestCaseBase {

	/**
	 *
	 * src/test/resources/configurations/root.jsonの設定値に基づく{@link MockModulePlatform}の初期化/稼働テスト
	 *
	 * <pre>
	 * クラスパス上の資源読み取り設定。
	 * </pre>
	 *
	 * @throws Exception 何らかの例外発生
	 */
	@Test
	public void testQueryModulePlatformExecution() throws Exception{

		execute("/mock");

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

		execute("/mock2");
	}

	/**
	 *
	 * Mock結果取得テストの一シナリオ。
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

		//テスト用データの検索結果確認
		StringWriter sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test","GET", null ,type.getInstance(),sw));

		DataObject result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo1",	result.get("Foo"));
		assertEquals("Bar1",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test","PUT", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo2",	result.get("Foo"));
		assertEquals("Bar2",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test","POST", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo3",	result.get("Foo"));
		assertEquals("Bar3",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test","DELETE", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo4",	result.get("Foo"));
		assertEquals("Bar4",	result.get("Bar"));


		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test2","GET", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo5",	result.get("Foo"));
		assertEquals("Bar5",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test2","PUT", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo6",	result.get("Foo"));
		assertEquals("Bar6",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test2","POST", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo6",	result.get("Foo"));
		assertEquals("Bar6",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test2","DELETE", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo6",	result.get("Foo"));
		assertEquals("Bar6",	result.get("Bar"));


		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test3","GET", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo7",	result.get("Foo"));
		assertEquals("Bar7",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test3","PUT", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo7",	result.get("Foo"));
		assertEquals("Bar7",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test3","POST", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo7",	result.get("Foo"));
		assertEquals("Bar7",	result.get("Bar"));

		sw = new StringWriter();
		module.execute(new KernelContextImpl(
				"/test/Test3","DELETE", null ,type.getInstance(),sw));

		result = mapper.readValue(sw.toString(), DataObject.class);
		assertEquals("Foo7",	result.get("Foo"));
		assertEquals("Bar7",	result.get("Bar"));
	}

}
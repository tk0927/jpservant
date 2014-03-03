package com.jpservant.core.common;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * {@link Utilities}のテスト。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class UtilitiesTest {

	@Test
	public void testSplitURI() {

		String[] result = Utilities.splitURI("/context/sql/foo/bar/baz_foo%20%bar.ext");

		assertEquals(3, result.length);
		assertEquals("/context", result[0]);
		assertEquals("/sql", result[1]);
		assertEquals("/foo/bar/baz_foo%20%bar.ext", result[2]);
	}

}

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
	public void testDevideURI() {

		String[] result = Utilities.devideURI("/sql/foo/bar/baz_foo%20%bar.ext");

		assertEquals(2, result.length);
		assertEquals("/sql", result[0]);
		assertEquals("/foo/bar/baz_foo%20%bar.ext", result[1]);
	}

}

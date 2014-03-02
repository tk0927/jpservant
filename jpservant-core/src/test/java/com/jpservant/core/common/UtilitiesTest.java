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

		assertEquals(result.length,2);
		assertEquals(result[0], "/sql");
		assertEquals(result[1], "/foo/bar/baz_foo%20%bar.ext");
	}

}

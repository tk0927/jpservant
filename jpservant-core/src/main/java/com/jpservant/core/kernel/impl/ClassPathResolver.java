package com.jpservant.core.kernel.impl;

import java.net.URL;

import com.jpservant.core.kernel.ResourceResolver;

/**
 *
 * ClassPathによるリソース解決処理。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class ClassPathResolver implements ResourceResolver {

	@Override
	public void setReference(Object ref) {

	}

	@Override
	public URL resolve(String path) {
		return getClass().getResource(path);
	}

}

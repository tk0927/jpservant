package com.jpservant.core.kernel.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import com.jpservant.core.kernel.ResourceResolver;

/**
 *
 * ServletContextによるリソース解決処理。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class ServletContextResolver implements ResourceResolver {

	private ServletContext ref;

	@Override
	public void setReference(Object ref) {
		this.ref = (ServletContext)ref;

	}

	@Override
	public URL resolve(String path) throws MalformedURLException{

		return ref.getResource(path);
	}

}

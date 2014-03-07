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
package com.jpservant.core.resolver;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

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
		this.ref = (ServletContext) ref;

	}

	@Override
	public URL resolve(String path) throws MalformedURLException {

		return ref.getResource(path);
	}

}

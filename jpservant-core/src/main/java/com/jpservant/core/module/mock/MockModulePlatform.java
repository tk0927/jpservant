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
package com.jpservant.core.module.mock;

import static com.jpservant.core.common.JacksonUtils.*;
import static com.jpservant.core.common.Utilities.*;

import java.util.Map;

import com.jpservant.core.common.DataObject;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;

/**
 *
 * SQLモジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class MockModulePlatform implements ModulePlatform {

	/**
	 * SQL文定義ファイルの拡張子
	 */
	public static final String JSON_FILE_EXT = ".json";

	private static final String METHOD_NAME_DEFAULT = "DEFAULT";

	private ModuleConfiguration config;

	@Override
	public void initialize(ModuleConfiguration config) {

		this.config = config;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(KernelContext context) throws Exception {

		String path = createResourcePath(this.config,context,JSON_FILE_EXT);
		String method = context.getMethod();
		String content = findResource(context.getResource(path)).trim();

		DataObject mock = toDataObject(content);
		Object object = mock.get(method);

		if(object == null){
			object = mock.get(METHOD_NAME_DEFAULT);
		}
		if(object == null){
			object = mock;
		}

		context.writeResponse(new DataObject((Map<String,Object>)object));
	}


}

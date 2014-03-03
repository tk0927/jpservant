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
package com.jpservant.core.kernel;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.Constant;
import com.jpservant.core.common.Constant.ResourceType;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.DataObject;
import com.jpservant.core.common.Utilities;
import com.jpservant.core.kernel.impl.ConfigurationManagerImpl;
import com.jpservant.core.kernel.impl.KernelContextImpl;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.core.resolver.ResourceResolver;

/**
 *
 * REST リクエストを適切な処理にディスパッチする機能を担当するクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class RequestDispatcher extends HttpServlet {

	/**
	 * JPServant設定情報
	 */
	private ConfigurationManagerImpl manager;

	@Override
	public void init(ServletConfig config) throws ServletException {

		try{

			this.manager = new ConfigurationManagerImpl();
			this.manager.initialize(config.getServletContext().getResource(
					config.getInitParameter(Constant.SERVLET_INIT_CONFIG_NAME)));

		}catch(Exception e){
			throw new ServletException(e);
		}

	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		try{

			String[] uritoken = Utilities.splitURI(request.getRequestURI());
			ModulePlatform platform = this.manager.getModulePlatform(uritoken[1]);
			if(platform == null){
				throw new IOException(String.format("Token[%s] mapped module not found.",uritoken[1]));
			}
			response.setHeader("Content-Type", "application/json;charset=UTF-8");

			KernelContextImpl context = createKernelContext(request, response, uritoken);

			try{

				platform.execute(context);
				context.doPostProcess();

			}catch(Exception e){

				context.doErrorProcess();
				throw e;

			}

		}catch(IOException e){
			throw e;
		}catch(Exception e){
			throw new ServletException(e);
		}
	}

	/**
	 *
	 * KernelContextを生成する。
	 *
	 * @param request HttpServletリクエスト
	 * @param response HttpServletレスポンス
	 * @param uritoken 分割したURI
	 * @return KernelContextのインスタンス
	 * @throws Exception 何らかの例外発生
	 */
	private KernelContextImpl createKernelContext(HttpServletRequest request, HttpServletResponse response,
			String[] uritoken) throws  Exception {

		String method = request.getMethod();
		String content = Utilities.loadStream(request.getInputStream());

		DataCollection parameter = parseRequestBody(content);

		ModuleConfiguration config = this.manager.getModuleConfiguration(uritoken[1]);

		ResourceType type = ResourceType.valueOf((String)
				config.get(Constant.ConfigurationName.ResourceType.name()));
		ResourceResolver resolver = type.getInstance();
		resolver.setReference(request.getSession().getServletContext());

		return new KernelContextImpl(uritoken[2], method, parameter, resolver,response.getWriter());

	}

	/**
	 *
	 * HttpリクエストボディのJSON文字列を解析する。
	 *
	 * @param content Httpリクエストボディ
	 * @return 解析結果
	 * @throws Exception 何らかの例外発生
	 */
	private static DataCollection parseRequestBody(String content) throws Exception {

		if(content != null && content.length() > 0){

			ObjectMapper mapper = new ObjectMapper();

			try{

				return mapper.readValue(content, DataCollection.class);

			}catch(Exception e){

				DataObject obj = mapper.readValue(content, DataObject.class);
				DataCollection parameter = new DataCollection();
				parameter.add(obj);
				return parameter;

			}
		}

		return null;
	}

}

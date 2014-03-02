package com.jpservant.core.kernel;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.Constant;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.common.Utilities;
import com.jpservant.core.kernel.impl.ConfigurationManagerImpl;
import com.jpservant.core.kernel.impl.KernelContextImpl;
import com.jpservant.core.module.spi.ModulePlatform;

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
			this.manager.initialize(getClass().getResource(
					config.getInitParameter(Constant.SERVLET_INIT_CONFIG_NAME)));

		}catch(ConfigurationException e){
			throw new ServletException(e);
		}

	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try{

			String uri = request.getRequestURI();
			String method = request.getMethod();
			String content = Utilities.loadStream(request.getInputStream());
			DataCollection parameter = null;

			if(content != null && content.length() > 0){
				ObjectMapper mapper = new ObjectMapper();
				parameter = mapper.readValue(content, DataCollection.class);
			}

			String[] token = Utilities.devideURI(uri);
			ModulePlatform platform = this.manager.getModulePlatform(token[0]);
			KernelContextImpl context =
					new KernelContextImpl(token[1], method, parameter, response.getWriter());
			platform.execute(context);

		}catch(IOException e){
			throw e;
		}catch(Exception e){
			throw new ServletException(e);
		}
	}

}

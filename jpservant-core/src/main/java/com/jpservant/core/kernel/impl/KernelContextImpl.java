package com.jpservant.core.kernel.impl;

import java.io.IOException;
import java.io.Writer;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.kernel.KernelContext;

/**
 *
 * リクエストのコンテキスト情報を格納するクラス。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class KernelContextImpl implements KernelContext {

	private String path;
	private String method;
	private DataCollection request;
	private Writer writer;

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param path HttpリクエストURIパス
	 * @param method Httpリクエストメソッド
	 * @param request Httpリクエストボディ(JSONのパース結果)
	 * @param writer Httpレスポンス出力用Writer
	 */
	public KernelContextImpl(String path,String method,DataCollection request,Writer writer){

		this.path = path;
		this.method = method;
		this.request = request;
		this.writer = writer;

	}

	@Override
	public String getRequestPath() {
		return this.path;
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public DataCollection getParameters() {
		return this.request;
	}

	@Override
	public void writeResponse(DataCollection response) throws IOException {

		try{

			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(this.writer, response);
			this.writer.flush();

		}catch(JsonMappingException e){
			throw new IOException(e);
		}
	}

}

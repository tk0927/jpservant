package com.jpservant.core.kernel.impl;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpservant.core.common.DataCollection;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.kernel.PostProcessor;
import com.jpservant.core.resolver.ResourceResolver;

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
	private ResourceResolver resolver;
	private Writer writer;
	private ArrayList<PostProcessor> processlist = new ArrayList<PostProcessor>();
	private ArrayList<PostProcessor> errorlist = new ArrayList<PostProcessor>();

	/**
	 *
	 * コンストラクタ。
	 *
	 * @param path HttpリクエストURIパス
	 * @param method Httpリクエストメソッド
	 * @param request Httpリクエストボディ(JSONのパース結果)
	 * @param writer Httpレスポンス出力用Writer
	 */
	public KernelContextImpl(String path,String method,DataCollection request,ResourceResolver resolver,Writer writer){

		this.path = path;
		this.method = method;
		this.request = request;
		this.resolver = resolver;
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
	public URL getResource(String path) throws Exception{
		return this.resolver.resolve(path);
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

	@Override
	public void addPostProcessor(PostProcessor processor) {
		this.processlist.add(processor);
	}

	@Override
	public void addErrorProcessor(PostProcessor processor) {
		this.errorlist.add(processor);
	}

	/**
	 *
	 * 登録された後処理を実施する。
	 *
	 * @throws Exception
	 */
	public void doPostProcess()throws Exception{

		for(PostProcessor process: this.processlist){
			process.execute();
		}

	}
	/**
	 *
	 * 登録されたエラー時後処理を実施する。
	 *
	 * @throws Exception
	 */
	public void doErrorProcess()throws Exception{

		for(PostProcessor process: this.errorlist){
			process.execute();
		}

	}
}

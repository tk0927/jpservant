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
package com.jpservant.test.tool.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * REST API呼び出しツール
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class HttpRequestExecutor {

	/**
	 *
	 * メインメソッド
	 *
	 * <pre>
	 * 第一引数 ･･･ Http URL
	 * 第二引数 ･･･ Http Method 省略時はGET、GET以外の場合はContent-Type="application/json"固定）
	 * 第三引数 ･･･ Http リクエストボディ 省略時は空文字列
	 * </pre>
	 *
	 * @param args 引数
	 * @throws Exception 何らかの例外発生
	 */
	public static void main(String[] args)throws Exception{

		System.out.println(
				execute(
					args[0],
					args.length > 1 ? args[1] : "GET",
					args.length > 2 ? args[2] : ""));
	}

	/**
	 *
	 * テストメソッド等からの呼び出し用
	 *
	 * @param urlstring Http URL
	 * @param method Http Method
	 * @param content Http リクエストボディ
	 * @return Httpレスポンスボディ
	 * @throws Exception 何らかの例外発生
	 */
	public static String execute(String urlstring,String method,String content)throws Exception{


		HttpURLConnection conn = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		boolean isGet = method.equals("GET");

		try{
			URL url = new URL(urlstring);

			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(!isGet);
			if(!isGet){
				conn.setRequestProperty("Content-Type", "application/json");
			}

			conn.connect();

			if(!isGet){
				writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
				writer.write(content);
				writer.close();
			}

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line);
				sb.append("\r\n");
			}
			return sb.toString();

		}finally{

			if(writer != null){
				try{
					writer.close();
				}catch(Exception e){}
			}
			if(reader != null){
				try{
					reader.close();
				}catch(Exception e){}
			}
			if(conn != null){
				try{
					conn.disconnect();
				}catch(Exception e){}
			}

		}

	}

}

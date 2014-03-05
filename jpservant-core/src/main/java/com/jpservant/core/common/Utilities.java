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
package com.jpservant.core.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jpservant.core.common.Constant.ConfigurationName;
import com.jpservant.core.exception.ApplicationException;
import com.jpservant.core.exception.ApplicationException.ErrorType;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * ユーティリティメソッドクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class Utilities {

	/**
	 *
	 * リソースの検索パス文字列を生成します。
	 *
	 * @param config モジュール設定
	 * @param context コンテキスト情報
	 * @param ext 拡張子
	 * @return パス文字列
	 */
	public static String createResourcePath(
			ModuleConfiguration config,KernelContext context,String ext) {

		return String.format("%s%s%s",
				config.get(ConfigurationName.ResourceRoot.name()),
				context.getRequestPath(),ext);

	}

	/**
	 *
	 * URLに紐づくリソースをUTF-8エンコードで読み取る。
	 *
	 * @param url URL
	 * @return 読み取り結果
	 * @throws IOException IO例外発生
	 */
	public static String findResource(URL url)throws ApplicationException {

		try{

			return loadResource(url);

		}catch(IOException e){
			throw new ApplicationException(ErrorType.NotFound, e);
		}
	}

	/**
	 *
	 * URLに紐づくリソースをUTF-8エンコードで読み取る。
	 *
	 * @param url URL
	 * @return 読み取り結果
	 * @throws IOException IO例外発生
	 */
	public static String loadResource(URL url)throws IOException {

		InputStream in = null;

		try{

			in = url.openStream();
			return loadStream(in);

		}finally{
			if(in != null){
				try{
					in.close();
				}catch(Exception e){}
			}
		}
	}

	/**
	 *
	 * 入力ストリームを終端までUTF-8エンコードで読み取る。
	 *
	 * @param in 入力ストリーム
	 * @return 読み取り結果
	 * @throws IOException IO例外発生
	 */
	public static String loadStream(InputStream in)throws IOException {

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		while((len = in.read(buff)) > 0){
			bout.write(buff,0,len);
		}

		bout.close();

		return bout.toString(Constant.ENCODE_NAME);
	}

	/**
	 *
	 * URIを先頭要素とそれ以外の2つの文字列に分割する。
	 *
	 * @param uri URI
	 * @return 3要素の文字列配列(コンテキストパス、設定分割用パス、資源指定用パス)
	 */
	public static String[] splitURI(String uri){

		Pattern p = Pattern.compile("(/[^/]+)(/[^/]+)(/[\\S]+)");
		Matcher m = p.matcher(uri);
		m.matches();
		String[] retvalue = new String[3];
		retvalue[0] = m.group(1);
		retvalue[1] = m.group(2);
		retvalue[2] = m.group(3);

		return retvalue;
	}
}

package com.jpservant.core.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		Pattern p = Pattern.compile("(/[a-zA-Z0-9%_]+)(/[a-zA-Z0-9%_]+)(/[a-zA-Z0-9%_/\\.]+)");
		Matcher m = p.matcher(uri);
		m.matches();
		String[] retvalue = new String[3];
		retvalue[0] = m.group(1);
		retvalue[1] = m.group(2);
		retvalue[2] = m.group(3);

		return retvalue;
	}
}

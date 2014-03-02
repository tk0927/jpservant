package com.jpservant.core.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try{
			in = url.openStream();
			byte[] buff = new byte[1024];
			int len = 0;
			while((len = in.read(buff)) > 0){
				bout.write(buff,0,len);
			}

			bout.close();

			return bout.toString(Constant.ENCODE_NAME);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(Exception e){}
			}
		}

	}


}

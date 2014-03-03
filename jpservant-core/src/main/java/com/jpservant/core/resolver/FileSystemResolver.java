package com.jpservant.core.resolver;

import java.io.File;
import java.net.URL;


/**
 *
 * 通常のファイルシステムによるリソース解決処理。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class FileSystemResolver implements ResourceResolver {

	@Override
	public void setReference(Object ref) {

	}

	@Override
	public URL resolve(String path) {
		try{
			return new File(path).toURI().toURL();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}

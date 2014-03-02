package com.jpservant.core.kernel;
/**
 *
 * 設定誤り例外。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class ConfigurationException extends Exception {

	public ConfigurationException(Exception e){
		super(e);
	}
	public ConfigurationException(String message){
		super(message);
	}
	public ConfigurationException(String message,Exception e){
		super(message,e);
	}
}

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
package com.jpservant.core.exception;

/**
 *
 * アプリケーションロジック中に発生する例外です。
 *
 * @author Toshiaki.Kamoshida <kamoshida.toshiaki@gmail.com>
 * @version 0.1
 *
 */
public class ApplicationException extends Exception {

	public static enum ErrorType {

		NotFound,
		BadRequest,
		InternalError,

	}

	private ErrorType type;

	public ApplicationException(ErrorType type){
		super();
		this.type = type;
	}

	public ApplicationException(ErrorType type,Exception e){
		super(e);
		this.type = type;
	}

	public ApplicationException(ErrorType type,String message){
		super(message);
		this.type = type;
	}


	public ApplicationException(ErrorType type,String message,Exception e){
		super(message,e);
		this.type = type;
	}

	public ErrorType getErrorType(){
		return this.type;
	}
}

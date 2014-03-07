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

import com.jpservant.core.common.Constant.RequestMethod;
import com.jpservant.core.kernel.KernelContext;

/**
 *
 * アクセスコントロール関連の機能を提供する。
 *
 * @author Toshiaki.Kamoshida <kamoshida.toshiaki@gmail.com>
 * @version 0.1
 *
 */
public class AccessController {

	/**
	 *
	 * 許容するリクエストメソッドであるかをチェックします。
	 *
	 *
	 * @param context コンテキスト情報
	 * @param accepts 許容するリクエストメソッド
	 * @return 許容するならtrue
	 */
	public static boolean checkAccessMethod(KernelContext context, RequestMethod... accepts) {

		for (RequestMethod accept : accepts) {

			if (context.getMethod().equals(accept.name())) {
				return true;
			}

		}

		return false;
	}

}

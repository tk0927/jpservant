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
package com.jpservant.core.module.spi;

import java.sql.SQLException;

import com.jpservant.core.common.sql.DatabaseConnectionHolder;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.kernel.PostProcessor;

/**
 *
 * モジュール向けの各種ユーティリティメソッドクラス。
 *
 * @author Toshiaki.Kamoshida <kamoshida.toshiaki@gmail.com>
 * @version 0.1
 *
 */
public class ModuleUtils {

	/**
	 *
	 * データベース接続を利用するモジュール向けの後処理を登録する。
	 *
	 * @param context コンテキスト
	 * @param holder データベース接続
	 */
	public static void registerPostProcessForConnection(KernelContext context, final DatabaseConnectionHolder holder) {

		if (holder == null) {
			return;
		}

		context.addPostProcessor(new PostProcessor() {
			@Override
			public void execute() throws Exception {
				holder.commit();
				holder.releaseSession();
			}
		});

		context.addErrorProcessor(new PostProcessor() {
			@Override
			public void execute() throws Exception {
				try {
					holder.rollback();
				} catch (SQLException e) {
					//no operation
				}
				try {
					holder.releaseSession();
				} catch (SQLException e) {
					//no operation
				}
			}
		});
	}

}

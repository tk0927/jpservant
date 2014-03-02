package com.jpservant.core.module.spi;

import com.jpservant.core.kernel.KernelContext;

/**
 *
 * モジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface ModulePlatform {

	/**
	 *
	 * 初期化処理を実装する位置。
	 *
	 * @param config 設定情報
	 */
	void initialize(ModuleConfiguration config);

	/**
	 *
	 * リクエストの処理を行う位置。
	 *
	 * @param context コンテキスト情報
	 * @throws Exception 何らかの例外発生
	 */
	void execute(KernelContext context)throws Exception;

}

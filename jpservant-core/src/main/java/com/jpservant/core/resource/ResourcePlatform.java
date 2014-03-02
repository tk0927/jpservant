package com.jpservant.core.resource;

import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * リソースのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface ResourcePlatform {

	/**
	 *
	 * 初期化処理を実装する位置。
	 *
	 * @param config 設定情報
	 */
	void initialize(ModuleConfiguration config);

	/**
	 *
	 * 固有の管理対象リソースオブジェクトを提供する。
	 *
	 * @return 管理対象リソースオブジェクト
	 *
	 */
	Object getResource();
}

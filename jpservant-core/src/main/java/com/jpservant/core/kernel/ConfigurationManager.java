package com.jpservant.core.kernel;

import java.util.ArrayList;

import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;
import com.jpservant.core.resource.ResourcePlatform;

/**
 *
 * システムの設定情報を格納し、各機能に提供するクラスのインターフェイス定義。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface ConfigurationManager {

	/**
	 *
	 * 設定ファイル内の"RootPath"設定リストを得る。
	 *
	 * @return "RootPath"設定リスト
	 */
	public abstract ArrayList<String> getRootPathList();

	/**
	 *
	 * RootPath値に紐づくモジュール向け設定情報を得る。
	 *
	 * @param path RootPath値
	 * @return モジュール向け設定情報
	 */
	public abstract ModuleConfiguration getModuleConfiguration(String path);

	/**
	 *
	 * RootPath値に紐づく{@link ModulePlatform}インスタンスを得る。
	 *
	 * @param path RootPath値
	 * @return インスタンス
	 */
	public abstract ModulePlatform getModulePlatform(String path);

	/**
	 *
	 * RootPath値に紐づく{@link ResourcePlatform}インスタンスを得る。
	 *
	 * @param path RootPath値
	 * @return インスタンス
	 */
	public abstract ResourcePlatform getResourcePlatform(String path);

}
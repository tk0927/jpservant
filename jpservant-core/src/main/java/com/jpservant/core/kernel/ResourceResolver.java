package com.jpservant.core.kernel;

import java.net.URL;

/**
 *
 * リソースの解決処理を行うリゾルバのインターフェイス定義。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface ResourceResolver {

	void setReference(Object ref);

	URL resolve(String path) throws Exception;

}

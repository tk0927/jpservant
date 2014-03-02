package com.jpservant.core.kernel;

import java.net.URI;

import com.jpservant.core.common.DataObject;

/**
 *
 * リクエストのコンテキスト情報を格納するクラス。
 *
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public interface KernelContext {

	public String getRequestPath();

	public String getMethod();

	public URI getResource(String name);

	public DataObject getParameters();

	public void writeResponse(DataObject response);

}

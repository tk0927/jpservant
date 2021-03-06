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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jpservant.core.common.Constant.ConfigurationName;
import com.jpservant.core.common.Constant.FileExtern;
import com.jpservant.core.exception.ApplicationException;
import com.jpservant.core.exception.ApplicationException.ErrorType;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.spi.ModuleConfiguration;

/**
 *
 * ユーティリティメソッドクラス。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class Utilities {

	/**
	 *
	 * リソースの検索パス文字列を生成します。
	 *
	 * @param config モジュール設定
	 * @param context コンテキスト情報
	 * @param ext 拡張子
	 * @return パス文字列
	 */
	public static String createResourcePath(
			ModuleConfiguration config, KernelContext context, FileExtern ext) {

		return String.format("%s%s%s",
				config.getValue(ConfigurationName.ResourceRoot),
				context.getRequestPath(), ext.toString());

	}

	/**
	 *
	 * URLに紐づくリソースをUTF-8エンコードで読み取る。
	 *
	 * @param url URL
	 * @return 読み取り結果
	 * @throws ApplicationException リソースが見つからない場合、NotFound例外を創出
	 */
	public static String findResource(URL url) throws ApplicationException {

		try {

			return loadResource(url);

		} catch (IOException e) {
			throw new ApplicationException(ErrorType.NotFound, e);
		}
	}

	/**
	 *
	 * URLに紐づくリソースをUTF-8エンコードで読み取る。
	 *
	 * @param url URL
	 * @return 読み取り結果
	 * @throws IOException IO例外発生
	 */
	public static String loadResource(URL url) throws IOException {

		InputStream in = null;

		try {

			in = url.openStream();
			return loadStream(in);

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 *
	 * NIO形式入力ストリームを終端までUTF-8エンコードで読み取る。
	 *
	 * @param in 入力ストリーム
	 * @return 読み取り結果
	 * @throws IOException IO例外発生
	 */
	public static String loadChannel(ReadableByteChannel in) throws IOException {

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ByteBuffer buff = ByteBuffer.allocate(4096);
		int len = 0;
		while ((len = in.read(buff)) > 0) {
			bout.write(buff.array(), 0, len);
		}

		bout.close();

		return bout.toString(Constant.ENCODE_NAME);
	}

	/**
	 *
	 * NIO形式出力ストリームへ書き込む。
	 *
	 * @param out 入力ストリーム
	 * @param content 書き込み対象データ
	 * @throws IOException IO例外発生
	 */
	public static void saveChannel(WritableByteChannel out, String content) throws IOException {

		ByteBuffer buf = ByteBuffer.wrap(content.getBytes(Constant.ENCODE_NAME));
		out.write(buf);

	}

	/**
	 *
	 * 入力ストリームを終端までUTF-8エンコードで読み取る。
	 *
	 * @param in 入力ストリーム
	 * @return 読み取り結果
	 * @throws IOException IO例外発生
	 */
	public static String loadStream(InputStream in) throws IOException {

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		while ((len = in.read(buff)) > 0) {
			bout.write(buff, 0, len);
		}

		bout.close();

		return bout.toString(Constant.ENCODE_NAME);
	}

	/**
	 *
	 * URIを先頭要素とそれ以外の2つの文字列に分割する。
	 *
	 * @param uri URI
	 * @return 3要素の文字列配列(コンテキストパス、設定分割用パス、資源指定用パス)
	 */
	public static String[] splitURI(String uri) {

		Pattern p = Pattern.compile("(/[^/]+)(/[^/]+)(/[\\S]+)");
		Matcher m = p.matcher(uri);
		m.matches();
		String[] retvalue = new String[3];
		retvalue[0] = m.group(1);
		retvalue[1] = m.group(2);
		retvalue[2] = m.group(3);

		return retvalue;
	}

	/**
	 *
	 * 引数Listから先頭要素を得ます。
	 * 存在しなければnullを返します。
	 *
	 * @param list List
	 * @return 先頭要素
	 */
	public static <T> T findFirstObjectOrNull(List<T> list) {

		return (list == null || list.isEmpty()) ? null : list.get(0);
	}

	/**
	 *
	 * リストに含まれるスネークケース文字列をキャメルケース文字列に変換する。
	 *
	 *
	 * @param src スネークケース文字列リスト
	 * @return キャメルケース文字列リスト
	 * @see #convertSnakeToCamel(String)
	 */
	public static List<String> convertSnakeToCamel(List<String> src) {
		List<String> retvalue = new ArrayList<String>();
		for (String element : src) {
			retvalue.add(convertSnakeToCamel(element));
		}
		return retvalue;
	}

	/**
	 *
	 * スネークケース文字列をキャメルケース文字列に変換する。
	 *
	 * <pre>
	 * 文字列構成要素は半角英大小文字、半角アンダースコアのみ。
	 * (データベースのオブジェクトシンボル名を想定)
	 * "TEST_FOO_BAR"->"TestFooBar"
	 * <pre>
	 *
	 * @param src スネークケース文字列
	 * @return キャメルケース文字列
	 */
	public static String convertSnakeToCamel(String src) {

		Pattern p = Pattern.compile("_([a-z])");
		Matcher m = p.matcher(src.toLowerCase());

		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, m.group(1).toUpperCase());
		}
		m.appendTail(sb);

		if (sb.length() > 0) {
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	/**
	 *
	 * キャメルケース文字列リストをスネークケース文字列リストに変換する。
	 *
	 * @param src キャメルケース文字列リスト
	 * @return スネークケース文字列リスト
	 * @see #convertCamelToSnake(String)
	 */
	public static List<String> convertCamelToSnake(List<String> src) {
		List<String> retvalue = new ArrayList<String>();
		for (String element : src) {
			retvalue.add(convertCamelToSnake(element));
		}
		return retvalue;
	}

	/**
	 *
	 * キャメルケース文字列をスネークケース文字列に変換する。
	 *
	 * <pre>
	 * 文字列構成要素は半角英大小文字、英数字のみ
	 * (JSONの属性名を想定)
	 * "TestFooBar"->"TEST_FOO_BAR"
	 * "TESTFooBar"->"TEST_FOO_BAR"
	 * <pre>
	 *
	 * @param src キャメルケース文字列
	 * @return スネークケース文字列
	 */
	public static String convertCamelToSnake(String src) {

		String convertedStr = src
				.replaceAll("([A-Z0-9]+)([A-Z0-9][a-z0-9])", "$1_$2")
				.replaceAll("([a-z0-9])([A-Z0-9])", "$1_$2");
		return convertedStr.toUpperCase();

	}

	/**
	 *
	 * URIパストークンを連結して一つのURIにします。
	 *
	 * @param root ルートパストークン
	 * @param tokens パストークン
	 * @return URI文字列
	 */
	public static String concatPathTokens(String root, List<String> tokens) {

		String[] array = new String[tokens.size() + 1];
		array[0] = root;
		int i = 1;
		for (String token : tokens) {
			array[i++] = token;
		}

		return concatPathTokens(array);
	}

	/**
	 *
	 * URIパストークンを連結して一つのURIにします。
	 *
	 * @param tokens パストークン
	 * @return URI文字列
	 */
	public static String concatPathTokens(String... tokens) {

		StringBuilder sb = new StringBuilder("/");

		for (String token : tokens) {
			sb.append(token);
			sb.append("/");
		}

		sb.deleteCharAt(sb.length() - 1);
		return sb.toString().replaceAll("[/]+", "/");
	}

	public static String[] toStringArray(int[] data) {

		String[] retvalue = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			retvalue[i] = String.valueOf(data[i]);
		}
		return retvalue;
	}
}

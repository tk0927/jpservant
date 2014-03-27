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
package com.jpservant.google.module.gss;

import static com.jpservant.google.Constant.ConfigurationName.*;

import com.google.gdata.client.authn.oauth.GoogleOAuthHelper;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.jpservant.core.exception.ConfigurationException;
import com.jpservant.core.kernel.KernelContext;
import com.jpservant.core.module.spi.ModuleConfiguration;
import com.jpservant.core.module.spi.ModulePlatform;

/**
 *
 * Google Spreadsheetモジュールのエントリポイント。
 *
 * @author Toshiaki.Kamoshida <toshiaki.kamoshida@gmail.com>
 * @version 0.1
 *
 */
public class GSSModulePlatform implements ModulePlatform {

	private SpreadsheetService service;

	private ModuleConfiguration config;

	@Override
	public void initialize(ModuleConfiguration config) throws ConfigurationException {

		try{
			this.config = config;
			this.service = new SpreadsheetService(
					this.config.getValue(ApplicationName));

			//OAuth setup
			GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
			oauthParameters.setOAuthConsumerKey(config.getValue(OAuthConsumerKey));
			oauthParameters.setOAuthConsumerSecret(config.getValue(OAuthConsumerSecret));
			oauthParameters.setScope(config.getValue(OAuthScope));
			OAuthSigner signer = new OAuthHmacSha1Signer();
			GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);
			oauthHelper.getUnauthorizedRequestToken(oauthParameters);

			String requestUrl = oauthHelper.createUserAuthorizationUrl(oauthParameters);

			String token = oauthHelper.getAccessToken(oauthParameters);

			service.setOAuthCredentials(oauthParameters, signer);

		}catch(OAuthException e){
			throw new ConfigurationException(e);
		}

	}

	@Override
	public void execute(KernelContext context) throws Exception {

	}

}

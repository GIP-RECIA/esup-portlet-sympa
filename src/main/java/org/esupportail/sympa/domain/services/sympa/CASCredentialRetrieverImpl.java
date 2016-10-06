/**
 * Licensed to ESUP-Portail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * ESUP-Portail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.sympa.domain.services.sympa;

import org.esupportail.sympa.domain.services.cas.ICASPtGenerator;

public class CASCredentialRetrieverImpl implements ICredentialRetriever {
	private ICASPtGenerator casPtGeneratorService;
	
	public SympaCredential getCredentialForService(String targetService) {
		String password = casPtGeneratorService.getCasServiceToken(targetService);
		if ( password != null ) {
			SympaCredential sc = new SympaCredential();
			sc.setId(targetService);
			sc.setPassword(password);
			return sc;
		}
		return null;
	}

	public TYPE getType() {
		return TYPE.cas;
	}

	/**
	 * @return the casPtGeneratorService
	 */
	public ICASPtGenerator getCasPtGeneratorService() {
		return casPtGeneratorService;
	}

	/**
	 * @param casPtGeneratorService the casPtGeneratorService to set
	 */
	public void setCasPtGeneratorService(ICASPtGenerator casPtGeneratorService) {
		this.casPtGeneratorService = casPtGeneratorService;
	}
	
}

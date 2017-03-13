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
package org.esupportail.sympa.domain.services;

import java.util.List;
import java.util.Map;

import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.sympa.AbstractSympaServer;


public abstract interface IDomainService {
	public enum SympaListFields {
		address,
		owner,
		editor,
		subscriber
	};
	/**
	 * When the application layer needs to tell implementors of this interface that any results that
	 * may have been cached are now invalid.  This method should be called after data on the sympa servers
	 * could have been changed, such as after creation of a list for example.
	 */
	public void invalidateCache();
	
	public List<UserSympaListWithUrl> getWhich();
	public List<UserSympaListWithUrl> getWhich(List<SympaListCriterion>criterion,boolean mathAll);
	public List<CreateListInfo> getCreateListInfo();
	public Map<String, AbstractSympaServer> getServerList();
	public String getHomeUrl();
}

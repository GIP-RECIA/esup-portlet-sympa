/**
 * Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
 * @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
 * @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
 * @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 * @Contributor (C) 2013 Maxime BOSSARD (GIP-RECIA) <mxbossard@gmail.com>
 *
 * Licensed under the GPL License, (please see the LICENCE file)
 */
package org.esupportail.sympa.domain.listfinder;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.esupportail.sympa.domain.listfinder.model.Model;
import org.esupportail.sympa.domain.listfinder.model.ModelRequest;
import org.esupportail.sympa.domain.listfinder.model.ModelSubscribers;
import org.esupportail.sympa.domain.listfinder.model.PreparedRequest;

public interface IDaoService {


	/**
	 * @return all model subscribers
	 */
	public List<ModelSubscribers> getAllModelSubscribers();



	/**
	 * @return all models
	 */
	public List<Model> getAllModels();

	/**
	 * @param id
	 * @return
	 */
	public Model getModel(BigInteger id);

	/**
	 * @param preparedRequest
	 * @param model
	 * @return The corresponding ModelRequest
	 */
	public ModelRequest getModelRequest(PreparedRequest preparedRequest, Model model);

	/**
	 * @param model
	 * @return
	 */
	public ModelSubscribers getModelSubscriber(Model model);

	/**
	 * @return
	 */
	public List<PreparedRequest> getAllPreparedRequests();

	/**
	 * Convert hibernate objects into IMailingListModel objects in order to work with the available list finder component
	 * @param listModels
	 * @param userInfo
	 * @return
	 */
	public List<IMailingListModel> getMailingListModels(List<Model> listModels, Map<String, String> userInfo);
}

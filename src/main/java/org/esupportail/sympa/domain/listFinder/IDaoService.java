package org.esupportail.sympa.domain.listFinder;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.esco.sympa.portlet.commondata.IMailingListModel;
import org.esupportail.sympa.domain.listFinder.model.Model;
import org.esupportail.sympa.domain.listFinder.model.ModelRequest;
import org.esupportail.sympa.domain.listFinder.model.ModelSubscribers;
import org.esupportail.sympa.domain.listFinder.model.PreparedRequest;

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

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
package org.esupportail.sympa.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents an email list in the create list screen in the jsTree.
 * 
 *
 */
public class JsList {
	private String data;
	private Map<String, String> attr;
	private Map<String, String> metadata;
	private List<JsList> children;
	
	JsList() {
		children = new ArrayList<JsList>();
		attr = new HashMap<String, String>();
		metadata = new HashMap<String, String>();
	}
	
	/**
	 * @return the metadata
	 */
	public Map<String, String> getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the attr
	 */
	public Map<String, String> getAttr() {
		return attr;
	}
	/**
	 * @param attr the attr to set
	 */
	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}
	/**
	 * @return the children
	 */
	public List<JsList> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<JsList> children) {
		this.children = children;
	}
	
	public static JsList getMatchingNode(List<JsList> listNodes, String node) {
		for(JsList l : listNodes) {
			if (l.getData().equals(node)) {
				return l;
			}
		} 
		
		return null;
	}
	
	
}
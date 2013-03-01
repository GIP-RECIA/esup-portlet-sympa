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
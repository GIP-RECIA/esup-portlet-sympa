package org.esco.sympa.domain.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.log4j.Logger;
import org.esco.sympa.util.LdapUtils;
import org.esupportail.sympa.domain.listfinder.model.PreparedRequest;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LdapFilterSourceRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6561990436374565291L;




	/** LOGGER. */
	private static final Logger LOG = Logger.getLogger(LdapFilterSourceRequest.class);
	
	
	
	
	private static String UAIregex = "\\{UAI\\}";
	private static String SIRENregex = "\\{SIREN\\}";
	
	// les valeurs pour former la requette ldap de recherche le siren d'un etab connaissant son uai.
	// Ici on a les valeurs par défauts elles peuvent êtres modifiées via les setters.
	// declaration du bean dans escoLdapContext.
	private String objectClassEtab= "ENTStructure";
	private String ldapAttrSiren = "ENTStructureSiren";
	private String ldapAttrUai="ENTStructureUAI";
	private String ldapBaseRdnEtab = "ou=structures";
	private String filtreLdapSearchSirenByUaiFormat="(&(objectClass=%s)(%s=%s))";
	
	/*
	 * Le bean est de session on memorise ici les requettes ldap de cette session: 
	 */
	private transient HashMap<String, String> request2name = new HashMap<String, String>();
	
	private String functionSources;
	
	

	private transient Set<String> sourceSet = new HashSet<String>();
	

	private LdapTemplate ldapTemplate;

	static private String replaceAll(String in, String uai, String siren) {
		String res = in;
		if (uai != null) {
			res = res.replaceAll(UAIregex, uai);
		}
		if (siren != null) {
			res = res.replaceAll(SIRENregex, siren);
		}
		return res;
	}
	
	/**
	 * Recherche dans ldap du siren d'un etablissement connaissant son uai
	 * @param uai
	 * @return siren
	 */
	public String findSirenByUai(String uai){
		String siren=null;
		if (uai != null) {
			siren = request2name.get(uai);
			if (siren == null) {
				String filtre = String.format(filtreLdapSearchSirenByUaiFormat, objectClassEtab, ldapAttrUai, uai);
				Collection<String> colLdap= LdapUtils.ldapSearch(	
									ldapTemplate,  
									filtre, 
									ldapBaseRdnEtab, 
									new AttributesMapper() {
										@Override
										public Object mapFromAttributes(Attributes attrs) throws NamingException {
											Attribute attr = attrs.get(ldapAttrSiren);
											return attr.get();
										}
									}
								);
				if (colLdap != null && !colLdap.isEmpty()) {
					siren = colLdap.iterator().next();
					request2name.put(uai, siren);
				}
			}
		}
		return siren;
	}
	
	public String getObjectClassEtab() {
		return objectClassEtab;
	}

	public void setObjectClassEtab(String objectClassEtab) {
		this.objectClassEtab = objectClassEtab;
	}

	public String getLdapAttrSiren() {
		return ldapAttrSiren;
	}

	public void setLdapAttrSiren(String ldapAttrSiren) {
		this.ldapAttrSiren = ldapAttrSiren;
	}

	public String getLdapAttrUai() {
		return ldapAttrUai;
	}

	public void setLdapAttrUai(String ldapAttrUai) {
		this.ldapAttrUai = ldapAttrUai;
	}

	public String getLdapBaseRdnEtab() {
		return ldapBaseRdnEtab;
	}

	public void setLdapBaseRdnEtab(String ldapBaseRdnEtab) {
		this.ldapBaseRdnEtab = ldapBaseRdnEtab;
	}

	public String getFiltreLdapSearchSirenByUaiFormat() {
		return filtreLdapSearchSirenByUaiFormat;
	}

	public void setFiltreLdapSearchSirenByUaiFormat(
			String filtreLdapSearchSirenByUaiFormat) {
		this.filtreLdapSearchSirenByUaiFormat = filtreLdapSearchSirenByUaiFormat;
	}

	/**
	 * Test si on doit afficher les adresses mail correspondant à la  preparedRequest.
	 * Si non alors donne le displayName de la preparedRequest
	 * Si oui alors interoge ldap pour recuperer l'adresse correspondante:
	 * 	La premiere adresse trouvé sera ajoutée au displayName.
	 * 	si aucunne adresse trouvé renvoie null;
	 * @param preparedRequest
	 * @param uai
	 * @param siren
	 * @return
	 */
	public String makeDisplayName(PreparedRequest preparedRequest, String uai, String siren) {
		String name = null;
		if (preparedRequest != null) {
			String source = preparedRequest.getDataSource();
			if (getSourceSet().contains(source)) {
					// si c'est une source pour laquelle il faut afficher le mail:
					// on calcul le filtre ldap
				String filter = replaceAll(preparedRequest.getLdapFilter(), uai, siren);
				String base = replaceAll(preparedRequest.getLdapSuffix(),uai, siren);
				base = base.substring(0, base.indexOf(",dc="));
				
					// on test si on a déjà fait la requette 
				String request = String.format("%s:%s", filter, base);
				if (request2name.containsKey(request)) {
						// si oui on redonne la meme reponse (elle peut etre vide)
					name = request2name.get(request);
				} else {
						// sinon on interroge le ldap
					if (LOG.isDebugEnabled()) {
						LOG.debug("PreparedRequest source="  + source+";");
						LOG.debug("filter =" + filter + "; base =" + base+";");
					}
					Collection<String> mails =LdapUtils.ldapSearch(ldapTemplate, filter, base, 
							new AttributesMapper() {
								
								@Override
								public Object mapFromAttributes(Attributes attrs) throws NamingException {
									Attribute attr = attrs.get("mail");
									return attr.get();
								}
							}
							
							);
						// si le resultat n'est pas vide
					if (mails != null && ! mails.isEmpty()) {
						Iterator<String> it = mails.iterator();			
						String mail =  it.next();
						String autre = "";
						// on affiche  le premier mail a la suite du nom 
						// et on indique s'il y en a d'autre
						if (it.hasNext()) {
							autre = "...";
						}
						name = String.format("%s: %s%s", preparedRequest.getDisplayName(), mail, autre );
					} 
						// le nom calcule peut etre vide
					request2name.put(request, name);
				} 
			} else {
				name = preparedRequest.getDisplayName();
			}
			
		}
		return name;
	}
	
	private Set<String> getSourceSet(){
		if (sourceSet.isEmpty() && functionSources != null) {
			for (String src : functionSources.split("\\s+")) {
				sourceSet.add(src);
			}
		}
		return sourceSet;
	}
	
	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
	
	public String getFunctionSources() {
		return functionSources;
	}

	public void setFunctionSources(String sources) {
		this.functionSources = sources;
		sourceSet.clear();
	}
}

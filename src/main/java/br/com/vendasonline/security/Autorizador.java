package br.com.vendasonline.security;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.security.Authorizer;

public class Autorizador implements Authorizer {

	private static final long serialVersionUID = 1L;

	@Inject
	private Credentials credential;
	
	@Override
	public boolean hasRole(String role) throws Exception {
		if (credential != null && role.equals(credential.getLogin())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasPermission(String resource, String operation)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}

package br.com.vendasonline.security;

import javax.inject.Inject;

import lombok.Getter;
import br.com.vendasonline.business.EmpresaBC;
import br.com.vendasonline.domain.Empresa;
import br.gov.frameworkdemoiselle.security.AuthenticationException;
import br.gov.frameworkdemoiselle.security.Authenticator;
import br.gov.frameworkdemoiselle.security.User;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

public class Autenticador implements Authenticator {

	private static final long serialVersionUID = 1L;

	@Inject
	private Credentials credentials;
	
	@Inject
	private EmpresaBC empresaBC;
	
	@Inject
	private ResourceBundle bundle;
	
	@Getter
	private static boolean authenticated;
	
	@Override
	public void authenticate() throws Exception {
		authenticated = false;
		
		Empresa empresa = empresaBC.getPorLoginPorSenha(credentials.getLogin(), credentials.getSenha());
		
		if (empresa == null) {
			throw new AuthenticationException(bundle.getString("usuarioNaoAutenticado"));
		} else {
			authenticated = true;
		}
		credentials.setEmpresa(empresa);
		credentials.setLogado(authenticated);
	}

	@Override
	public void unauthenticate() throws Exception {
		authenticated = false;
		credentials.clear();
		credentials.setEmpresa(null);
		credentials.setLogado(authenticated);
	}

	@Override
	public User getUser() {
		if (authenticated) {
			return new User() {

				private static final long serialVersionUID = 1L;

				@Override
				public String getId() {
					return credentials.getLogin();
				}

				@Override
				public void setAttribute(Object key, Object value) {
				}

				@Override
				public Object getAttribute(Object key) {
					return null;
				}
			};
		} else {
			return null;
		}
	}

}

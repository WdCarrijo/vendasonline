package br.com.vendasonline.business;

import br.com.vendasonline.domain.Login;
import br.com.vendasonline.persistence.LoginDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class LoginBC extends DelegateCrud<Login, Long, LoginDAO> {
	
	private static final long serialVersionUID = 1L;

	public Login getPorLoginPorSenha(String login, String senha) {
		return getDelegate().getPorLoginPorSenha(login, senha);
	}
	
}

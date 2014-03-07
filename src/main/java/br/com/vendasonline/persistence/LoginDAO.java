package br.com.vendasonline.persistence;

import br.com.vendasonline.domain.Login;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class LoginDAO extends JPACrud<Login, Long> {

	private static final long serialVersionUID = 1L;

	public Login getPorLoginPorSenha(String login, String senha) {
		try {
			return (Login)createQuery("SELECT login FROM Login login WHERE login.login = :login AND login.senha = :senha")
					.setParameter("login", login)
					.setParameter("senha", senha)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	

}

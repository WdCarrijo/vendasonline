package br.com.vendasonline.persistence;

import java.util.List;

import br.com.vendasonline.domain.Empresa;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class EmpresaDAO extends JPACrud<Empresa, Long> {

	private static final long serialVersionUID = 1L;
	
	public Empresa getPorLoginPorSenha(String login, String senha) {
		try {
			return (Empresa)createQuery("SELECT empresa FROM Empresa empresa WHERE empresa.login = :login AND empresa.senha = :senha AND empresa.situacao = 'A'")
					.setParameter("login", login)
					.setParameter("senha", senha)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Empresa> listPorSituacao(String situacao) {
		try {
			return (List<Empresa>)createQuery("SELECT empresa FROM Empresa empresa")
					//.setParameter("situacao", situacao)
					.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
}

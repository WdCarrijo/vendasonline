package br.com.vendasonline.persistence;

import javax.inject.Inject;

import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.security.Credentials;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class ClienteDAO extends JPACrud<Cliente, Long> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Credentials credential;
	
	public Cliente getByTelefone(String telefone) {
		try {
			return (Cliente)createQuery("SELECT cliente FROM Cliente cliente WHERE cliente.telefone = :telefone AND cliente.empresa.id = :idEmpresa ")
					.setParameter("telefone", telefone)
					.setParameter("idEmpresa", credential.getEmpresa().getId())
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}

package br.com.vendasonline.persistence;

import java.util.List;

import javax.inject.Inject;

import br.com.vendasonline.domain.Item;
import br.com.vendasonline.security.Credentials;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class ItemDAO extends JPACrud<Item, Long> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Credentials credential;

	@SuppressWarnings("unchecked")
	public List<Item> listItemByIdClienteAndSituacao(Long idCliente, String situacao) {
		String ql = " SELECT item FROM Item item WHERE item.cliente.id = :idCliente AND item.situacao = :situacao"
				+ " AND item.empresa.id = :idEmpresa ";
		
		try {
			return (List<Item>)createQuery(ql)
					.setParameter("idCliente", idCliente)
					.setParameter("situacao", situacao)
					.setParameter("idEmpresa", credential.getEmpresa().getId())
					.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

}

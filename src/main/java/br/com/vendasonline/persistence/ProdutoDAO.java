package br.com.vendasonline.persistence;

import java.util.List;

import javax.inject.Inject;

import br.com.vendasonline.domain.Produto;
import br.com.vendasonline.security.Credentials;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class ProdutoDAO extends JPACrud<Produto, Long> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Credentials credential;
	
	@SuppressWarnings("unchecked")
	public List<Produto> listByEmpresaAndSituacao(String situacao) {
		String ql = " SELECT produto FROM Produto produto WHERE produto.situacao = :situacao"
				+ " AND produto.empresa.id = :idEmpresa ";
		
		try {
			return (List<Produto>)createQuery(ql)
					.setParameter("situacao", situacao)
					.setParameter("idEmpresa", credential.getEmpresa().getId())
					.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

}

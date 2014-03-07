package br.com.vendasonline.business;

import java.util.List;

import javax.inject.Inject;

import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Produto;
import br.com.vendasonline.persistence.ProdutoDAO;
import br.com.vendasonline.security.Credentials;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class ProdutoBC extends DelegateCrud<Produto, Long, ProdutoDAO> {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoDAO dao;
	
	@Inject
	private Credentials credential;
	
	@Override
	@Transactional
	public Produto insert(Produto bean) {
		try {
			bean.setId(null);
			bean.setSituacao(Constantes.STATUS_ATIVO);
			bean.setEmpresa(credential.getEmpresa());
			dao.insert(bean);
		} catch (Exception e) {
			return bean;
		}
		return new Produto();
	}
	
	@Transactional
	public List<Produto> list() {
		try {
			return dao.listByEmpresaAndSituacao(Constantes.STATUS_ATIVO);
		} catch (Exception e) {
			return null;
		}
	}
	
}

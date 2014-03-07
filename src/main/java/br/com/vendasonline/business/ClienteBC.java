package br.com.vendasonline.business;

import javax.inject.Inject;

import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.persistence.ClienteDAO;
import br.com.vendasonline.security.Credentials;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class ClienteBC extends DelegateCrud<Cliente, Long, ClienteDAO> {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClienteDAO dao;
	
	@Inject
	private Credentials credential;
	
	@Transactional
	public Cliente insert(Cliente bean) {
		bean.setId(null);
		bean.setEmpresa(credential.getEmpresa());
		bean.setSituacao(Constantes.STATUS_ATIVO);
		if (bean.validaDadosCliente()) {
			dao.insert(bean);
			return new Cliente();
		}
		return bean;
	}
	
	@Transactional
	public Cliente getByTelefone(String telefone) {
		return dao.getByTelefone(telefone);
	}
	
}

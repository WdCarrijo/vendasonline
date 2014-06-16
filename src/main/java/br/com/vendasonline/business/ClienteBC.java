package br.com.vendasonline.business;

import javax.inject.Inject;

import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.persistence.ClienteDAO;
import br.com.vendasonline.security.Credentials;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class ClienteBC extends DelegateCrud<Cliente, Long, ClienteDAO> {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClienteDAO dao;
	
	@Inject
	private Credentials credential;
	
	public Cliente insert(Cliente bean) {
		bean.setId(null);
		bean.setEmpresa(credential.getEmpresa());
		bean.setSituacao(Constantes.STATUS_ATIVO);
		try {
			dao.insert(bean);
			return new Cliente();
		} catch (Exception e) {
			return bean;
		}
	}
	
	public Cliente getByTelefone(String telefone) {
		return dao.getByTelefone(telefone);
	}
	
}

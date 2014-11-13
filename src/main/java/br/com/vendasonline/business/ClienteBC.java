package br.com.vendasonline.business;

import javax.inject.Inject;

import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.exception.ClienteJaExistenteException;
import br.com.vendasonline.exception.ErroGenericoException;
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
	
	public Cliente salvar(Cliente cliente) throws ClienteJaExistenteException, ErroGenericoException {
		cliente.setId(null);
		cliente.setEmpresa(credential.getEmpresa());
		cliente.setSituacao(Constantes.STATUS_ATIVO);
		Cliente clienteIdentico = getByTelefone(cliente.getTelefone().getNumero()); 
		if (clienteIdentico == null) {
			dao.insert(cliente);
			return new Cliente();
		} else {
			throw new ClienteJaExistenteException();
		}
	}
	
	public Cliente getByTelefone(String telefone) {
		return dao.getByTelefone(telefone);
	}
	
}

package br.com.vendasonline.business;

import java.util.List;

import javax.inject.Inject;

import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.domain.Item;
import br.com.vendasonline.domain.Produto;
import br.com.vendasonline.domain.Venda;
import br.com.vendasonline.persistence.VendaDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class VendaBC extends DelegateCrud<Venda, Long, VendaDAO> {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClienteBC clienteBC;

	@Inject
	private ItemBC itemBC;
	
	@Inject
	private ProdutoBC produtoBC;

	@Transactional
	public Cliente getClienteByTelefone(String telefone) {
		return clienteBC.getByTelefone(telefone);
	}

	@Transactional
	public List<Item> listItemByIdClienteAndSituacao(Long idCliente,
			String situacao) {
		return itemBC.listItemByIdClienteAndSituacao(idCliente, situacao);
	}

	@Transactional
	public List<Produto> listProdutos() {
		return produtoBC.list();
	}

	@Transactional
	public void salvarItem(Item item) {
		itemBC.insert(item);
	}

	@Transactional
	public void atualizarItem(Item item) {
		itemBC.update(item);
	}
	
	@Transactional
	public void removerItem(Item item) {
		item.setSituacao(Constantes.STATUS_INATIVO);
		itemBC.update(item);
	}
	
	
	
}

package br.com.vendasonline.business;

import java.util.List;

import javax.inject.Inject;

import br.com.vendasonline.domain.Item;
import br.com.vendasonline.persistence.ItemDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class ItemBC extends DelegateCrud<Item, Long, ItemDAO> {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private ItemDAO dao;
	
	public List<Item> listItemByIdClienteAndSituacao(Long idCliente, String situacao) {
		return dao.listItemByIdClienteAndSituacao(idCliente, situacao);
	}
	
}

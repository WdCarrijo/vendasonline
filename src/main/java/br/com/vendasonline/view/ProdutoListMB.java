package br.com.vendasonline.view;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.com.vendasonline.business.ProdutoBC;
import br.com.vendasonline.domain.Produto;

@ViewController
@NextView("./produto_edit.jsf")
@PreviousView("./produto_list.jsf")
public class ProdutoListMB extends AbstractListPageBean<Produto, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoBC produtoBC;
	
	@Override
	protected List<Produto> handleResultList() {
		return this.produtoBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				produtoBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}

}
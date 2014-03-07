package br.com.vendasonline.view;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import br.com.vendasonline.business.VendaBC;
import br.com.vendasonline.domain.Venda;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
public class VendaListMB extends AbstractListPageBean<Venda, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private VendaBC vendaBC;
	
	@Override
	protected List<Venda> handleResultList() {
		return vendaBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				vendaBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}

}
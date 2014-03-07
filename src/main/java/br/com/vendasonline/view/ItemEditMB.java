package br.com.vendasonline.view;

import javax.inject.Inject;

import br.com.vendasonline.business.ItemBC;
import br.com.vendasonline.domain.Item;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
@PreviousView("./item_list.jsf")
public class ItemEditMB extends AbstractEditPageBean<Item, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private ItemBC itemBC;
	
	@Override
	@Transactional
	public String delete() {
		this.itemBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.itemBC.insert(getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.itemBC.update(getBean());
		return getPreviousView();
	}
	
	protected void handleLoad() {
		setBean(this.itemBC.load(getId()));
	}

	@Override
	protected Item handleLoad(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
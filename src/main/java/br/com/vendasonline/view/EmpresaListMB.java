package br.com.vendasonline.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import br.com.vendasonline.business.EmpresaBC;
import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Empresa;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.security.RequiredRole;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
@NextView("./empresa_edit.jsf")
@PreviousView("./empresa_list.jsf")
@RequiredRole(value = "rof20004")
public class EmpresaListMB extends AbstractListPageBean<Empresa, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaBC empresaBC;
	
	@Getter
	@Setter
	private List<Empresa> empresas = new ArrayList<Empresa>();
	
	@Override
	protected List<Empresa> handleResultList() {
		return this.empresaBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				empresaBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}
	
	@PostConstruct
	public void getListEmpresa() {
		empresas = empresaBC.listPorSituacao(Constantes.STATUS_ATIVO);
	}
	
}
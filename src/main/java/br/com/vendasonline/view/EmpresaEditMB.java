package br.com.vendasonline.view;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.RollbackException;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import br.com.vendasonline.business.EmpresaBC;
import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Empresa;
import br.com.vendasonline.util.Mensagens;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.exception.ExceptionHandler;
import br.gov.frameworkdemoiselle.security.RequiredRole;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
@PreviousView("./empresa_list.jsf")
public class EmpresaEditMB extends AbstractEditPageBean<Empresa, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;
	
	@Inject
	private EmpresaBC empresaBC;
	
	@Override
	@Transactional
	public String delete() {
		empresaBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	@RequiredRole(value = "rof20004")
	public String insert() {
		RequestContext context = RequestContext.getCurrentInstance();
		empresaBC.insert(getBean());
		context.addCallbackParam("resultado", "sucesso");
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		empresaBC.update(getBean());
		return getPreviousView();
	}
	
	protected void handleLoad() {
		setBean(empresaBC.load(getId()));
	}

	@Override
	protected Empresa handleLoad(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void limparDialog() {
		RequestContext.getCurrentInstance().reset("cadastroClienteFormDlg");
		setBean(new Empresa());
	}
	
	@ExceptionHandler
	public void excecoes(RollbackException e) {
		RequestContext context = RequestContext.getCurrentInstance();
		Throwable t = e.getCause();
		while ((t != null) && !(t instanceof ConstraintViolationException)) {
	        t = t.getCause();
	    }
		if (t instanceof ConstraintViolationException) {
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Já existe empresa cadastrada com os dados informados.");
		} else {
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Erro ao salvar empresa. Contate o administrador.");
		}
		context.addCallbackParam("resultado", "erro");
		logger.error("Ocorreu um erro: " + e.getCause());
	}
	
	@Transactional
	public String remover(Empresa empresa) {
		empresa.setSituacao(Constantes.STATUS_INATIVO);
		empresaBC.update(empresa);
		return getCurrentView();
	}
	
}
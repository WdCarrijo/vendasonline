package br.com.vendasonline.view;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.RollbackException;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import br.com.vendasonline.business.LoginBC;
import br.com.vendasonline.domain.Login;
import br.com.vendasonline.util.Mensagens;
import br.gov.frameworkdemoiselle.exception.ExceptionHandler;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
public class LoginEditMB extends AbstractEditPageBean<Login, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;
	
	@Inject
	private LoginBC loginBC;
	
	@Override
	@Transactional
	public String delete() {
		loginBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	public String insert() {
		RequestContext context = RequestContext.getCurrentInstance();
		getBean().setId(null);
		setBean(loginBC.insert(getBean()));
		context.addCallbackParam("resultado", "sucesso");
		return getCurrentView();
	}
	
	@Override
	@Transactional
	public String update() {
		loginBC.update(getBean());
		return getPreviousView();
	}
	
	@Override
	protected Login handleLoad(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@ExceptionHandler
	public void excecoes(RollbackException e) {
		RequestContext context = RequestContext.getCurrentInstance();
		Throwable t = e.getCause();
		while ((t != null) && !(t instanceof ConstraintViolationException)) {
	        t = t.getCause();
	    }
		if (t instanceof ConstraintViolationException) {
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Já existe usuário cadastrado com o login informado.");
		} else {
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Erro ao salvar usuário. Contate o administrador.");
		}
		context.addCallbackParam("resultado", "erro");
		logger.error("Ocorreu um erro: " + e.getCause());
	}
	
	public void limparDialog() {
		RequestContext.getCurrentInstance().reset("cadastroClienteFormDlg");
		setBean(new Login());
	}

}
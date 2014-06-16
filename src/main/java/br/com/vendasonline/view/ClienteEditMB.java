package br.com.vendasonline.view;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.RollbackException;

import lombok.Getter;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import br.com.vendasonline.business.ClienteBC;
import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.util.Mensagens;
import br.com.vendasonline.validators.ClienteValidationBean;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.exception.ExceptionHandler;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
@PreviousView("./cliente_list.jsf")
public class ClienteEditMB extends AbstractEditPageBean<Cliente, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;
	
	@Inject
	private ClienteBC clienteBC;
	
	@Getter
	private ClienteValidationBean clienteDTO = new ClienteValidationBean();
	
	@Override
	@Transactional
	public String delete() {
		clienteBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		RequestContext context = RequestContext.getCurrentInstance();
		
		Cliente cliente = new Cliente();
		cliente.setNome(clienteDTO.getNome());
		cliente.setEndereco(clienteDTO.getEndereco());
		cliente.setTelefone(clienteDTO.getTelefone());
		
		if (clienteDTO.getComplemento() != null && !clienteDTO.getComplemento().equals(Constantes.STRING_VAZIA)) {
			cliente.setComplemento(clienteDTO.getNome());
		}
		
		setBean(clienteBC.insert(cliente));
		context.addCallbackParam("resultado", "sucesso");
		return getCurrentView();
	}
	
	@Override
	@Transactional
	public String update() {
		clienteBC.update(getBean());
		return getPreviousView();
	}
	
	@Override
	protected Cliente handleLoad(Long arg0) {
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
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Já existe cliente cadastrado com o telefone informado.");
		} else {
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Erro ao salvar cliente. Contate o administrador.");
		}
		context.addCallbackParam("resultado", "erro");
		logger.error("Ocorreu um erro: " + e.getCause());
	}
	
	public void limparDialog() {
		RequestContext.getCurrentInstance().reset("cadastroClienteFormDlg");
		setBean(new Cliente());
	}

}
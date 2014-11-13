package br.com.vendasonline.view;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import lombok.Getter;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import br.com.vendasonline.business.ClienteBC;
import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.exception.ClienteJaExistenteException;
import br.com.vendasonline.exception.ErroGenericoException;
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
		try {
			BeanUtils.copyProperties(cliente, clienteDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (clienteDTO.getComplemento() != null && !clienteDTO.getComplemento().trim().equals(Constantes.STRING_VAZIA)) {
			cliente.setComplemento(clienteDTO.getNome());
		}
		
		clienteBC.salvar(cliente);
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
	public void excecoes(ClienteJaExistenteException e) {
		RequestContext context = RequestContext.getCurrentInstance();
		Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Já existe cliente cadastrado com o telefone informado.");
		context.addCallbackParam("resultado", "erro");
		logger.error("Ocorreu um erro: " + e.getCause());
	}
	
	@ExceptionHandler
	public void excecoes(ErroGenericoException e) {
		RequestContext context = RequestContext.getCurrentInstance();
		Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Erro ao salvar cliente. Contate o administrador.");
		context.addCallbackParam("resultado", "erro");
		logger.error("Ocorreu um erro: " + e.getCause());
	}
	
	public void limparDialog() {
		RequestContext.getCurrentInstance().reset("cadastroClienteFormDlg");
		clienteDTO = new ClienteValidationBean();
	}

}
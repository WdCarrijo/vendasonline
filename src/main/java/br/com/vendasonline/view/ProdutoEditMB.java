package br.com.vendasonline.view;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.RollbackException;

import lombok.Getter;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import br.com.vendasonline.business.ProdutoBC;
import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Produto;
import br.com.vendasonline.util.Mensagens;
import br.com.vendasonline.validators.ProdutoValidationBean;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.exception.ExceptionHandler;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
@PreviousView("./produto_list.jsf")
public class ProdutoEditMB extends AbstractEditPageBean<Produto, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;
	
	@Inject
	private ProdutoBC produtoBC;

	@Getter
	private ProdutoValidationBean produtoDTO = new ProdutoValidationBean();
	
	@Override
	@Transactional
	public String delete() {
		produtoBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	public String insert() {
		RequestContext context = RequestContext.getCurrentInstance();
		
		Produto produto = new Produto();
		produto.setDescricao(produtoDTO.getDescricao());
		produto.setPreco(produtoDTO.getPreco());
		produto = produtoBC.insert(produto);
		
		if (produto.getDescricao() == null && produto.getPreco() == null) {
			produtoDTO = new ProdutoValidationBean();
		}
		
		context.addCallbackParam("resultado", "sucesso");
		return Constantes.STRING_VAZIA;
	}
	
	@Override
	@Transactional
	public String update() {
		produtoBC.update(getBean());
		return getPreviousView();
	}

	@Override
	protected Produto handleLoad(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@ExceptionHandler
	public void excecoes(RollbackException e) {
		RequestContext context = RequestContext.getCurrentInstance();
		Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, "Erro ao salvar produto. Contate o administrador.");
		context.addCallbackParam("resultado", "erro"); 
		logger.error("Ocorreu um erro: " + e.getCause());
	}
	
	public void limparDialog() {
		RequestContext.getCurrentInstance().reset("cadastroProdutoFormDlg");
		setBean(new Produto());
	}
	
}
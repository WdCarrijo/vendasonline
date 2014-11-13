package br.com.vendasonline.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import br.com.vendasonline.business.VendaBC;
import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.domain.Item;
import br.com.vendasonline.domain.Produto;
import br.com.vendasonline.domain.Venda;
import br.com.vendasonline.security.Credentials;
import br.com.vendasonline.util.Mensagens;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
@PreviousView("./venda_list.jsf")
public class VendaEditMB extends AbstractEditPageBean<Venda, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private VendaBC vendaBC;
	
	@Inject
	private Credentials credential;

	@Getter
	@Setter
	private Cliente cliente = new Cliente();

	@Getter
	@Setter
	private List<Item> itens = new ArrayList<Item>();

	@Getter
	@Setter
	private List<Produto> produtos = new ArrayList<Produto>();

	@Getter
	@Setter
	private String telefone;

	@Setter
	private Double totalGeral = 0.00;

	@Override
	@Transactional
	public String delete() {
		vendaBC.delete(getId());
		return getPreviousView();
	}

	@Override
	@Transactional
	public String insert() {
		vendaBC.insert(getBean());
		return getPreviousView();
	}

	@Override
	@Transactional
	public String update() {
		vendaBC.update(getBean());
		return getPreviousView();
	}

	protected void handleLoad() {
		setBean(vendaBC.load(getId()));
	}

	@Override
	protected Venda handleLoad(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void pesquisarCliente() {
		if (!Constantes.STRING_VAZIA.equals(telefone)) {
			cliente = vendaBC.getClienteByTelefone(telefone.replace("-", Constantes.STRING_VAZIA));
		}

		if (cliente == null || cliente.getId() == null) {
			cliente = new Cliente();
		} else {
			carregaItensPorCliente(cliente.getId(), Constantes.STATUS_ATIVO);
		}
	}

	public void limparCliente() {
		cliente = new Cliente();
		telefone = "";
		if (!itens.isEmpty()) {
			Venda venda = new Venda();
			venda.setDataConclusao(Calendar.getInstance());
			venda.setSituacao(Constantes.STATUS_VENDA_CANCELADA);
			venda.setEmpresa(credential.getEmpresa());
			vendaBC.insert(venda);
			for (Item item : itens) {
				item.setVenda(venda);
				item.setSituacao(Constantes.STATUS_INATIVO);
				vendaBC.atualizarItem(item);
			}
			carregaItensPorCliente(cliente.getId(), Constantes.STATUS_ATIVO);
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_INFO, "Venda cancelada com sucesso!");
		}
	}

	public void carregaItensPorCliente(Long idCliente, String situacao) {
		itens = vendaBC.listItemByIdClienteAndSituacao(idCliente, Constantes.STATUS_ATIVO);
	}

	public void carregaProdutos() {
		produtos = vendaBC.listProdutos();
	}

	public void adicionarProduto(Produto produto) {
		if (produto.getQuantidade() == null || produto.getQuantidade() <= 0) {
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_INFO, "Informe a quantidade: " + produto.getDescricao());
		} else if (produto.getQuantidade() > 100) {
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_INFO, "Quantidade permitida é 100");
		} else {
			Item item = new Item();
			item.setProduto(produto);
			item.setDataPedido(Calendar.getInstance());
			item.setSituacao(Constantes.STATUS_ATIVO);
			item.setEmpresa(credential.getEmpresa());
			item.setCliente(cliente);
			item.setQuantidade(produto.getQuantidade());
			vendaBC.salvarItem(item);
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_INFO, "Produto Adicionado: " + produto.getDescricao());
			carregaItensPorCliente(cliente.getId(), Constantes.STATUS_ATIVO);
		}
	}
	
	public void removerItem(Item item) {
		vendaBC.removerItem(item);
		carregaItensPorCliente(cliente.getId(), Constantes.STATUS_ATIVO);
		Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_INFO, "Produto removido: " + item.getProduto().getDescricao());
	}

	public Double getTotalGeral() {
		totalGeral = 0.00;

		if (itens != null && !itens.isEmpty()) {
			for (Item item : itens) {
				totalGeral += item.getTotalProduto();
			}
		}

		return totalGeral;
	}
	
	public void concluirVenda() {
		if (!itens.isEmpty()) {
			Venda venda = new Venda();
			venda.setDataConclusao(Calendar.getInstance());
			venda.setSituacao(Constantes.STATUS_VENDA_FINALIZADA);
			venda.setEmpresa(credential.getEmpresa());
			vendaBC.insert(venda);
			for (Item item : itens) {
				item.setVenda(venda);
				item.setSituacao(Constantes.STATUS_INATIVO);
				vendaBC.atualizarItem(item);
			}
			carregaItensPorCliente(cliente.getId(), Constantes.STATUS_ATIVO);
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_INFO, "Venda concluída com sucesso!");
		}
	}
	
}
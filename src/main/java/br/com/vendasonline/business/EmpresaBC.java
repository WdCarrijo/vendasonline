package br.com.vendasonline.business;

import java.util.List;

import javax.inject.Inject;

import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Empresa;
import br.com.vendasonline.persistence.EmpresaDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class EmpresaBC extends DelegateCrud<Empresa, Long, EmpresaDAO> {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaDAO dao;
	
	public Empresa insert(Empresa bean) {
		try {
			bean.setId(null);
			bean.setSituacao(Constantes.STATUS_ATIVO);
			dao.insert(bean);
		} catch (Exception e) {
			return bean;
		}
		return new Empresa();
	}
	
	public Empresa getPorLoginPorSenha(String login, String senha) {
		return getDelegate().getPorLoginPorSenha(login, senha);
	}
	
	public List<Empresa> listPorSituacao(String situacao) {
		return getDelegate().listPorSituacao(situacao);
	}
	
}

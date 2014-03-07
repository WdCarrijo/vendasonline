package br.com.vendasonline.view;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import br.com.vendasonline.security.Credentials;
import br.com.vendasonline.util.Mensagens;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPageBean;

@ViewController
public class LoginMB extends AbstractPageBean {
	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String login;
	
	@Getter
	@Setter
	private String senha;

	@Inject
	private Credentials credentials;

	@Inject
	private SecurityContext securityContext;

	public void doLogin() {
		try {
			credentials.setLogin(login);
			credentials.setSenha(senha);
			securityContext.login();
		} catch (Exception e) {
			Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR,
					e.getMessage());
		}
	}
	
	public void doLogout(){         
        try {
        	securityContext.logout();
        	FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } catch (Exception e) {
        	Mensagens.exibeMensagemGrowlSemDetalhe(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
	}

}

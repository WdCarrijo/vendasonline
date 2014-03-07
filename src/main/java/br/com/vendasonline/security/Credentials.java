package br.com.vendasonline.security;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;
import br.com.vendasonline.domain.Empresa;

@SessionScoped
@Named("credential")
public class Credentials implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String login;

	@Getter
	@Setter
	private String senha;

	@Getter
	@Setter
	private Empresa empresa;
	
	@Getter
	@Setter
	private boolean isLogado;
	
	public void clear() {
		login = null;
		senha = null;
	}

}

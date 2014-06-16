package br.com.vendasonline.validators;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

import br.com.vendasonline.domain.Telefone;

@Getter
@Setter
public class ClienteValidationBean {
	
	@NotNull
	@NotBlank
	private String nome;

	@NotNull
	@NotBlank
	private String endereco;

	@NotNull
	@NotBlank
	private Telefone telefone;

	private String complemento;

	public ClienteValidationBean() {
		this.telefone = new Telefone();
	}

}

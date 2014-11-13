package br.com.vendasonline.validators;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

import br.com.vendasonline.domain.Telefone;

@Getter
@Setter
public class ClienteValidationBean {
	
	@NotBlank(message = "Nome obrigatório")
	private String nome;

	@NotBlank(message = "Endereço obrigatório")
	private String endereco;

	private Telefone telefone;

	private String complemento;

	public ClienteValidationBean() {
		this.telefone = new Telefone();
	}

}

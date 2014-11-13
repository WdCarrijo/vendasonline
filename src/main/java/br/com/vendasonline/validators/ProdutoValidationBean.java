package br.com.vendasonline.validators;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class ProdutoValidationBean {

	@NotBlank(message = "Descrição obrigatório")
	private String descricao;
	
	@NotNull(message = "Preço obrigatório")
	private Double preco;
	
}

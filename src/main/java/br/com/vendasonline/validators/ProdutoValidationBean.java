package br.com.vendasonline.validators;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoValidationBean {

	@NotNull(message = "Descrição é obrigatório")
	private String descricao;
	
	@NotNull(message = "Preço é obrigatório")
	private Double preco;
	
}

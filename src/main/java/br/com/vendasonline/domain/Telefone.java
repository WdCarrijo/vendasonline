package br.com.vendasonline.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotBlank;

import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.util.Util;

@Embeddable
public class Telefone implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Telefone obrigatório")
	@Column(name = "telefone", nullable = false)
	private String numero;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero.replace(" ", Constantes.STRING_VAZIA).replace("(", Constantes.STRING_VAZIA).replace(")", Constantes.STRING_VAZIA).replace("-", Constantes.STRING_VAZIA);
	}
	
	/**
	 * Nova formatação para o telefone com 4 ou 5 dígitos no prefixo. <li>
	 * Parâmetro com o Telefone XXXXXXXXXX ou XXXXXXXXXXX <li>Retorno com o
	 * Telefone (XX) XXXX-XXXX ou (XX) XXXXX-XXXX
	 */
	public String getNumeroFormatado() {
		String telefone = Constantes.STRING_VAZIA;
		
		if (Util.isNull(this.numero)) {
			return null;
		}
		
		telefone = this.numero;

		try {
			telefone = Util.unformat(telefone);
			if (telefone.length() == 11) {
				telefone = "(" + telefone.substring(0, 2) + ")" + " "
						+ telefone.substring(2, 7) + "-"
						+ telefone.substring(7, 11);
			} else if (telefone.length() == 10) {
				telefone = "(" + telefone.substring(0, 2) + ")" + " "
						+ telefone.substring(2, 6) + "-"
						+ telefone.substring(6, 10);
			} else {
				telefone = telefone.substring(0, 4) + "-"
						+ telefone.substring(4, telefone.length());
			}
			return telefone;
		} catch (Exception e) {
			return null;
		}
	}
	
}

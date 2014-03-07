package br.com.vendasonline.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ForeignKey;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of={"id"})
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "endereco", nullable = false)
	private String endereco;

	@Column(name = "telefone", nullable = false, length = 8)
	private String telefone;

	@Column(name = "complemento")
	private String complemento;

	@ManyToOne
	@JoinColumn(name = "id_empresa", nullable = false)
	@ForeignKey(name = "empresa_id_fk")
	private Empresa empresa;
	
	@Column(name = "situacao", nullable = false)
	private String situacao;

	public boolean validaDadosCliente() {
		boolean retorno = true;
		
		if (nome == null || "".equals(nome)) {
			retorno = false;
		}
		
		if (endereco == null || "".equals(endereco)) {
			retorno = false;
		}
		
		if (telefone == null || "".equals(telefone)) {
			retorno = false;
		}
		
		return retorno;
	}

}

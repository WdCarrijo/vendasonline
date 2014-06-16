package br.com.vendasonline.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "cliente")
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

	@Embedded
	private Telefone telefone;

	@Column(name = "complemento")
	private String complemento;

	@ManyToOne
	@JoinColumn(name = "id_empresa", nullable = false)
	@ForeignKey(name = "empresa_id_fk")
	private Empresa empresa;
	
	@Column(name = "situacao", nullable = false, length = 1)
	private String situacao;

}

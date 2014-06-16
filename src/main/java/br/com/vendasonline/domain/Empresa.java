package br.com.vendasonline.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "empresa")
@Getter
@Setter
@EqualsAndHashCode(of={"id"})
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "razao_social", nullable = false, unique = true)
	private String razaoSocial;

	@Column(name = "cnpj", nullable = true, unique = true, length = 14)
	private String cnpj;

	@Embedded
	private Telefone telefone;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "login", nullable = false, unique = true)
	private String login;

	@Column(name = "senha", nullable = false)
	private String senha;

	@Column(name = "situacao", nullable = false, length = 1)
	private String situacao;

}

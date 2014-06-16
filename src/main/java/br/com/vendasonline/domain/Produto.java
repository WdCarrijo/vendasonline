package br.com.vendasonline.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "produto")
@Getter
@Setter
@EqualsAndHashCode(of={"id"})
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "descricao", nullable = false)
	private String descricao;

	@Column(name = "preco", nullable = false)
	private Double preco;

	@ManyToOne
	@JoinColumn(name = "id_empresa", nullable = false)
	@ForeignKey(name = "empresa_id_fk")
	private Empresa empresa;

	@Transient
	private Integer quantidade;

	@Column(name = "situacao", nullable = false, length = 1)
	private String situacao;

}

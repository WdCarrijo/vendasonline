package br.com.vendasonline.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "venda")
@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
public class Venda implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data_conclusao")
	@Temporal(TemporalType.DATE)
	private Calendar dataConclusao = Calendar.getInstance();

	@ManyToOne
	@JoinColumn(name = "id_empresa", nullable = false)
	@ForeignKey(name = "empresa_id_fk")
	private Empresa empresa;

	@Column(name = "situacao", length = 1)
	private String situacao;

}
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
import javax.persistence.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "item")
@Getter
@Setter
@EqualsAndHashCode(of={"id"})
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_produto", nullable = false)
	@ForeignKey(name = "produto_id_fk")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Produto produto;

	@ManyToOne
	@JoinColumn(name = "id_venda", nullable = true)
	@ForeignKey(name = "venda_id_fk")
	private Venda venda;

	@Column(name = "quantidade")
	private Integer quantidade;

	@Column(name = "situacao", nullable = false, length = 1)
	private String situacao;

	@Column(name = "data_pedido")
	@Temporal(TemporalType.DATE)
	private Calendar dataPedido = Calendar.getInstance();

	@ManyToOne
	@JoinColumn(name = "id_cliente", nullable = false)
	@ForeignKey(name = "cliente_id_fk")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "id_empresa", nullable = false)
	@ForeignKey(name = "empresa_id_fk")
	private Empresa empresa;
	
	@Transient
	private Double totalProduto;

	public Double getTotalProduto() {
		totalProduto = 0.00;
		
		if (quantidade != null) {
			totalProduto = produto.getPreco() * quantidade;
		}
		
		return totalProduto;
	}

	public void setTotalProduto(Double totalProduto) {
		this.totalProduto = totalProduto;
	}

}

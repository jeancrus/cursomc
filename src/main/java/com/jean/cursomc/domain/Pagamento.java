package com.jean.cursomc.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jean.cursomc.domain.enums.EstadoPagamento;


/*
 * CLASSE PAGAMENTO ESTÁ SEM AUTO ID POIS PODE SER CONSIDERADA
 * UMA ENTIDADE FRACA E DEPENDE DA CLASSE PEDIDO
 * ENTÃO ELA NÃO EXISTIRÁ SEM O PEDIDO
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED) //Dois tipos de tabela, tabelas separadas ou tabelão e deve ser escrito na superclasse
public abstract class Pagamento implements Serializable {
/*
 * Abstract pois não queremos que o pagamento possa ser instanciada
 * Somente suas filhas que pode = pagamentocomcartao e comboleto
 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	private Integer estado; //alterado para Integer ao invés de EstadoPagamento para que possa controlar o numero no banco de dados
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "pedido_id")
	@MapsId //garante que o id do pagamento, seja o mesmo do pedido_id
	private Pedido pedido;

	public Pagamento() {
	}

	public Pagamento(Integer id, EstadoPagamento estado, Pedido pedido) {
		super();
		this.id = id;
		this.estado = estado.getCod();
		this.pedido = pedido;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EstadoPagamento getEstado() {
		return EstadoPagamento.toEnum(estado); //get e setter do EstadoPagamento alterado para controlar enum no banco de dados
	}

	public void setEstado(EstadoPagamento estado) {
		this.estado = estado.getCod();
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pagamento other = (Pagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}

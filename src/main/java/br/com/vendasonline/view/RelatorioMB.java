package br.com.vendasonline.view;

import javax.enterprise.context.RequestScoped;

import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Getter
@Setter
public class RelatorioMB {

	private boolean chkFinalizada;
	private boolean chkCancelada;
	
	public void imprimiChecks() {
		System.out.println("Valor do finalizada: " + chkFinalizada);
		System.out.println("Valor do cancelada: " + chkCancelada);
	}
	
}

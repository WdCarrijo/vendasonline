package br.com.vendasonline.persistence;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.com.vendasonline.domain.Venda;

@PersistenceController
public class VendaDAO extends JPACrud<Venda, Long> {

	private static final long serialVersionUID = 1L;
	

}

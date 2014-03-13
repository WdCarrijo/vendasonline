package br.com.vendasonline.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.vendasonline.domain.Empresa;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;

@RunWith(DemoiselleRunner.class)
public class EmpresaBCTest {

    @Inject
	private EmpresaBC empresaBC;

	@Before
	public void before() {
		for (Empresa empresa : empresaBC.findAll()) {
			empresaBC.delete(empresa.getId());
		}
	}	


	@Test
	public void testInsert() {

		Empresa empresa = getEmpresa();
		empresaBC.insert(empresa);
		List<Empresa> listOfEmpresa = empresaBC.findAll();
		assertNotNull(listOfEmpresa);
		assertEquals(1, listOfEmpresa.size());
	}	

	@Test
	public void testDelete() {

		Empresa empresa = getEmpresa();
		empresaBC.insert(empresa);

		List<Empresa> listOfEmpresa = empresaBC.findAll();
		assertNotNull(listOfEmpresa);
		assertEquals(1, listOfEmpresa.size());

		empresaBC.delete(empresa.getId());
		listOfEmpresa = empresaBC.findAll();
		assertEquals(0, listOfEmpresa.size());
	}

	@Test
	public void testUpdate() {

		Empresa empresa = getEmpresa();
		empresaBC.insert(empresa);

		List<Empresa> listOfEmpresa = empresaBC.findAll();
		Empresa empresa2 = (Empresa)listOfEmpresa.get(0);
		assertNotNull(listOfEmpresa);

		try {
			empresa2.setRazaoSocial("novo valor");
			empresaBC.update(empresa2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		listOfEmpresa = empresaBC.findAll();
		Empresa empresa3 = (Empresa)listOfEmpresa.get(0);

		assertEquals("novo valor", empresa3.getRazaoSocial());
	}

	private Empresa getEmpresa() {
		Empresa empresa  = new Empresa();
		empresa.setRazaoSocial("Razão Social");
		empresa.setCnpj(null);
		empresa.setTelefone("88320387");
		empresa.setEmail("rof20004@gmail.com");
		empresa.setLogin("teste");
		empresa.setSenha("123");
		return empresa;
	}
	
}

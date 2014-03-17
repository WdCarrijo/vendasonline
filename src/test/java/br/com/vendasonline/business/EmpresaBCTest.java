package br.com.vendasonline.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;

import br.com.vendasonline.domain.Empresa;

public class EmpresaBCTest {

    @Inject
	private EmpresaBC empresaBC;

	@Before
	public void before() {
		for (Empresa empresa : empresaBC.findAll()) {
			empresaBC.delete(empresa.getId());
		}
	}	

	public void testInsert() {

		Empresa empresa = getEmpresaAdd();
		empresaBC.insert(empresa);
		List<Empresa> listOfEmpresa = empresaBC.findAll();
		assertNotNull(listOfEmpresa);
		assertEquals(1, listOfEmpresa.size());
	}	

	public void testDelete() {

		Empresa empresa = getEmpresaAdd();
		empresaBC.insert(empresa);

		List<Empresa> listOfEmpresa = empresaBC.findAll();
		assertNotNull(listOfEmpresa);
		assertEquals(1, listOfEmpresa.size());

		empresaBC.delete(empresa.getId());
		listOfEmpresa = empresaBC.findAll();
		assertEquals(0, listOfEmpresa.size());
	}

	public void testUpdate() {

		Empresa empresa = getEmpresaAdd();
		empresaBC.insert(empresa);

		List<Empresa> listOfEmpresa = empresaBC.findAll();
		Empresa empresa2 = (Empresa)listOfEmpresa.get(0);
		assertNotNull(listOfEmpresa);

		empresa2.setRazaoSocial("novo valor");
		empresaBC.update(empresa2);

		listOfEmpresa = empresaBC.findAll();
		Empresa empresa3 = (Empresa)listOfEmpresa.get(0);

		assertEquals("novo valor", empresa3.getRazaoSocial());
	}

	private Empresa getEmpresaAdd() {
		Empresa empresa  = new Empresa();
		empresa.setRazaoSocial("Razão Social");
		empresa.setCnpj("123");
		empresa.setTelefone("88320387");
		empresa.setEmail("rof20004@gmail.com");
		empresa.setLogin("teste");
		empresa.setSenha("123");
		return empresa;
	}
	
}

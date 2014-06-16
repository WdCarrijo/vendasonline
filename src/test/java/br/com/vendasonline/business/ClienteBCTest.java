package br.com.vendasonline.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.vendasonline.domain.Cliente;
import br.com.vendasonline.domain.Empresa;
import br.com.vendasonline.domain.Telefone;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;

@RunWith(DemoiselleRunner.class)
public class ClienteBCTest {

	@Inject
	private ClienteBC clienteBC;
	
	@Inject
	private EmpresaBC empresaBC;

	@Before
	public void before() {
 		List<Cliente> clienteList = clienteBC.findAll(); 
		for (Cliente cliente : clienteList) {
			clienteBC.delete(cliente.getId());
		}
	}	

	@Test
	public void testInsert() {

		Empresa empresa = getEmpresaAdd();
		empresaBC.insert(empresa);
		
		Cliente cliente = getClienteAdd();
		clienteBC.insert(cliente);
		List<Cliente> listOfCliente = clienteBC.findAll();
		assertNotNull(listOfCliente);
		assertEquals(1, listOfCliente.size());
	}	

	/*public void testDelete() {

		Empresa empresa = getEmpresaAdd();
		clienteBC.insert(empresa);

		List<Empresa> listOfEmpresa = clienteBC.findAll();
		assertNotNull(listOfEmpresa);
		assertEquals(1, listOfEmpresa.size());

		clienteBC.delete(empresa.getId());
		listOfEmpresa = clienteBC.findAll();
		assertEquals(0, listOfEmpresa.size());
	}

	public void testUpdate() {

		Empresa empresa = getEmpresaAdd();
		clienteBC.insert(empresa);

		List<Empresa> listOfEmpresa = clienteBC.findAll();
		Empresa empresa2 = (Empresa)listOfEmpresa.get(0);
		assertNotNull(listOfEmpresa);

		empresa2.setRazaoSocial("novo valor");
		clienteBC.update(empresa2);

		listOfEmpresa = clienteBC.findAll();
		Empresa empresa3 = (Empresa)listOfEmpresa.get(0);

		assertEquals("novo valor", empresa3.getRazaoSocial());
	}*/
	
	private Empresa getEmpresaAdd() {
		Empresa empresa  = new Empresa();
		empresa.setRazaoSocial("Razão Social");
		empresa.setCnpj("123");
		empresa.setTelefone(new Telefone());
		empresa.getTelefone().setNumero("8832-0387");
		empresa.setEmail("rof20004@gmail.com");
		empresa.setLogin("teste");
		empresa.setSenha("123");
		return empresa;
	}

	private Cliente getClienteAdd() {
		Cliente cliente  = new Cliente();
		cliente.setNome("Teste 1");;
		cliente.setEndereco("Av. Comendador José Cruz");
		cliente.setTelefone(new Telefone());
		cliente.getTelefone().setNumero("8832-0387");
		cliente.setComplemento("Cd. Cidade Paraíso");
		cliente.setEmpresa(new Empresa());
		cliente.getEmpresa().setId(1L);
		return cliente;
	}
	
}

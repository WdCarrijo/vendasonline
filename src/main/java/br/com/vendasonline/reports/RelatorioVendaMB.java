package br.com.vendasonline.reports;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.vendasonline.business.ItemBC;
import br.com.vendasonline.constant.Constantes;
import br.com.vendasonline.domain.Item;

@ManagedBean(name = "relatorioVendaMB")
public class RelatorioVendaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String tipoImpressao;
	
	@Getter
	@Setter
	private Date dataInicio;
	
	@Getter
	@Setter
	private Date dataFim;
	
	@Inject
	private ItemBC itemBC;

	public void gerarRelatorio() throws IOException, SQLException {
		List<Item> itemList = new ArrayList<Item>();
		Map<String, Object> parameters = new HashMap<String, Object>();

		itemList = itemBC.listItemByIdClienteAndSituacao(0L, Constantes.STATUS_ATIVO);
		
		try {
			
			ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
			
			String caminhoRelatorio = servletContext.getRealPath("/reports/venda/relatorioVendasPorPeriodo.jasper");
			
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(itemList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(caminhoRelatorio, parameters, beanCollectionDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "relatorio_vendas.pdf");
			
			HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();  
		    httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");  
		    
		    ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();  
		    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);  
		    
		    FacesContext.getCurrentInstance().responseComplete();  
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}

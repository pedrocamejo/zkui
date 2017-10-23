package cpc.zk.componente.ventanas;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JasperRunManager;

import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;


public class CompPdfReport extends Window implements Serializable {	

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6483972094221380428L;
	//private List<Component> 	componentes;
	//Center	centro;
	Iframe	contenedor;
	
	@SuppressWarnings("unchecked")
	public CompPdfReport(String titulo, String reporte, Map params, Connection conn) throws IOException{
		this.setTitle(titulo);
		this.setWidth("80%");
		this.setHeight("80%");
		inicio();
		doReport(titulo, reporte, params, conn);
	}
	
		
	protected void inicio(){
		/*centro = new Center();
		centro.setBorder("none");
		centro.setFlex(true);
		contenedor = new Iframe();*/
		contenedor = new Iframe();
		this.appendChild(contenedor);
		//this.appendChild(centro);
	}
	
	public void dibujar(AMedia reporte){
		contenedor.setContent(reporte);
	}
	
	@SuppressWarnings("unchecked")
	public void doReport(String titulo, String reporte, Map params, Connection conn) throws IOException {
        InputStream is = null;
        try {
            //generate report pdf stream
        	System.out.println("reporte "+reporte+ " titulo "+titulo+" con  "+conn.getCatalog()+" ");
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(reporte);
            final byte[] buf = JasperRunManager.runReportToPdf(is, params, conn);
            final InputStream mediais = new ByteArrayInputStream(buf);
            final AMedia amedia = new AMedia(titulo+".pdf", "pdf", "application/pdf", mediais);
            dibujar(amedia);
        } catch (Exception ex) {
        	ex.printStackTrace();
            throw new  IOException("Error generando reporte");
        }
    }
	
	
}

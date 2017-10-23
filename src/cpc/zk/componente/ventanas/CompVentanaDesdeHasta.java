package cpc.zk.componente.ventanas;

import java.util.List;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

import cpc.zk.componente.listas.CompCombobox;

public class CompVentanaDesdeHasta<T> extends CompVentana{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8835533962971040202L;
	private CompGrupoDatos	gbGeneral;	
	private Datebox			fechaInicio;
	private Datebox			fechaFinal;
	private Label			textoOtro, textoHasta, textoDesde, textoTipoReporte, textoFormatoRpt;
	private CompCombobox<T>	otro;
	private Radiogroup 		tipoReporte;
	private Radio 			resumido;
	private Radio 			detallado;
	private CompCombobox<T>	cmbFormatoRpt;
	public static final String PDF = "pdf";
	public static final String XLS = "xls";
	
	
	public CompVentanaDesdeHasta(EventListener controlador){
		super();
		aceptar.addEventListener(Events.ON_CLICK, controlador);
		cancelar.addEventListener(Events.ON_CLICK, this);
		super.dibujarVentana("Impresion",320);
	}
	

	public void inicializar() {
		gbGeneral		=	new CompGrupoDatos("Intervalo", 2);
		fechaInicio 	= 	new Datebox();
		fechaFinal 		= 	new Datebox();
		textoOtro		= 	new Label();
		textoDesde		= 	new Label("Desde:");
		otro			=   new CompCombobox<T>();
		cmbFormatoRpt   =   new CompCombobox<T>(); // Formato de Reporte
		textoHasta 		=  	new Label("Hasta:");
		tipoReporte		= 	new Radiogroup();
		resumido		= 	new Radio("Resumido");
		detallado		= 	new Radio("Detallado");
		textoTipoReporte =  new Label("Tipo"); 
		textoFormatoRpt =   new Label("Formato");
		cargarFormatos();
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 200);
		gbGeneral.addComponente(textoDesde);
		gbGeneral.addComponente(fechaInicio);
		gbGeneral.addComponente(textoHasta);
		gbGeneral.addComponente(fechaFinal);
		gbGeneral.addComponente(textoOtro);
		gbGeneral.addComponente(otro);
		tipoReporte.appendChild(resumido);
		tipoReporte.appendChild(detallado);
		gbGeneral.addComponente(textoTipoReporte); 
		gbGeneral.addComponente(tipoReporte);
		gbGeneral.addComponente(textoFormatoRpt);
		gbGeneral.addComponente(cmbFormatoRpt);
		// El tipo de Reporte esta Invisible por Defecto
		setVisibleOtro(false);
		setVisibleTipoReporte(false);
		setVisibleFormatoRpt(false);
		gbGeneral.dibujar(this);
		addBotonera();
	}

	
	public Datebox getFechaInicio() {
		return fechaInicio;
	}

	public Datebox getFechaFinal() {
		return fechaFinal;
	}

	public CompCombobox<T> getOtro() {
		return otro;
	}

	public void setOtro(CompCombobox<T> otro) {
		this.otro = otro;
	}
	
	public Label getTextoOtro() {
		return textoOtro;
	}

	public void setTextoOtro(Label textoOtro) {
		this.textoOtro = textoOtro;
	}

	public void setTexto(String valor) {
		textoOtro.setValue(valor);
	}
	
	public Radiogroup getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(Radiogroup tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public Radio getResumido() {
		return resumido;
	}

	public void setResumido(Radio resumido) {
		this.resumido = resumido;
	}

	public Radio getDetallado() {
		return detallado;
	}

	public void setDetallado(Radio detallado) {
		this.detallado = detallado;
	}

	public void setVisibleOtro(boolean visible) {
		this.textoOtro.setVisible(visible);
		this.otro.setVisible(visible);
	}
	
	public void setVisibleTipoReporte(boolean visible) {
		this.textoTipoReporte.setVisible(visible);
		this.tipoReporte.setVisible(visible);
		this.resumido.setVisible(visible);
		this.detallado.setVisible(visible);
	}
	
	public void setVisibleFormatoRpt(boolean visible) {
		this.textoFormatoRpt.setVisible(visible);
		this.cmbFormatoRpt.setVisible(visible);
	}
	
	public void setSoloFecha(){
		setVisibleOtro(false);
		textoHasta.setVisible(false);
		textoDesde.setVisible(true);
		fechaFinal.setVisible(false);
	}
	
	public void setOtro(String texto, List<T> modelo){
		setVisibleOtro(true);
		textoOtro.setValue(texto);
		otro.setModelo(modelo);
	}

	public void setSoloOtro(String texto, List<T> modelo){
		setOtro(texto, modelo);
		textoDesde.setVisible(false);
		fechaFinal.setVisible(false);
		textoHasta.setVisible(false);
		fechaInicio.setVisible(false);
	}


	public Label getTextoFormatoRpt() {
		return textoFormatoRpt;
	}


	public void setTextoFormatoRpt(Label textoFormatoRpt) {
		this.textoFormatoRpt = textoFormatoRpt;
	}


	public CompCombobox<T> getCmbFormatoRpt() {
		return cmbFormatoRpt;
	}


	public void setCmbFormatoRpt(CompCombobox<T> cmbFormatoRpt) {
		this.cmbFormatoRpt = cmbFormatoRpt;
	}
	
	private void cargarFormatos(){
		cmbFormatoRpt.appendItem("PDF").setValue(PDF);
		cmbFormatoRpt.appendItem("EXCEL").setValue(XLS);
	}
	
}

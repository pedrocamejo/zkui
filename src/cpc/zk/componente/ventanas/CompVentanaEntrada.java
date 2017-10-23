package cpc.zk.componente.ventanas;

import java.io.Serializable;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class CompVentanaEntrada extends CompVentana implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6444202627888595904L;
	/**
	 * 
	 */

	private CompGrupoDatos gbGeneral;
	private Textbox entrada;

	private Label textopregunta;

	public CompVentanaEntrada(EventListener controlador) {
		super();
		aceptar.addEventListener(Events.ON_CLICK, controlador);
		cancelar.addEventListener(Events.ON_CLICK, this);
		super.dibujarVentana("Verificación del Nº de Control", 320);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Introduzca el Nº de Control de la Hoja", 2);
		entrada = new Textbox();
		textopregunta = new Label("Nº de Control");

	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 200);
		gbGeneral.addComponente(textopregunta);
		//entrada.setHeight("42px");
		entrada.setWidth("150px");
		entrada.setStyle("font-size: 18px; font-weigth: bold;");
		gbGeneral.addComponente(entrada);
		gbGeneral.dibujar(this);
		addBotonera();
	}
	
	public Textbox getEntrada() {
		return entrada;
	}

	public void setEntrada(Textbox entrada) {
		this.entrada = entrada;
	}

	public String getentrada() {
		return entrada.toString();
	}

	public void setentrada(Textbox entrada) {
		this.entrada = entrada;
	}

	public Label gettextopregunta() {
		return textopregunta;
	}

	public void settextopregunta(String textopregunta) {
		this.textopregunta.setValue(textopregunta);
	}

	public CompGrupoDatos getGbGeneral() {
		return gbGeneral;
	}

	public void setGbGeneral(CompGrupoDatos gbGeneral) {
		this.gbGeneral = gbGeneral;
	}
}

package cpc.zk.componente.ventanas;


import java.io.Serializable;
import java.util.List;

import org.zkoss.zhtml.Form;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Captcha;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompEtiqueta;


public class CompLogin extends Window implements Serializable{
	
	/**
	 * 
	 */
	public static final String  BOTONACEPTAR = "AL";
	public static final String  BOTONRECETEAR = "RL";
	public static final String  BOTONREGENERAR = "GL";
	
	private static final long serialVersionUID = 3814894452894360166L;
	private Textbox  		j_username;
	private Textbox  		j_password;
	private CompCombobox	cmbSedes;
	private Label			lblSedes;

	private EventListener  	controlador;
	private Captcha			captcha;
	private Textbox			j_captcha;
	
	private Row				rcapcha1, rcapcha2;
	

	private Form   			forma;
	private Button			submit;
	private Button			regenerar;
	private Button			reset;
	private Rows 			rows;
	
	public CompLogin(){
		inicializar();
		dibujar();
		//setVisibleCapcha(false);
	}
	
	public void setVisibleCapcha(boolean visible){
		if (visible){
			rcapcha1 = new Row();
			rcapcha1.appendChild(new Label("Verificacion"));
			rcapcha1.appendChild(j_captcha);
			rows.appendChild(rcapcha1);
			
			rcapcha2 = new Row();
			rcapcha2.appendChild(new Label(""));
			rcapcha2.appendChild(captcha);
			rows.appendChild(rcapcha2);
		}
	}
	
	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
		submit.addEventListener(Events.ON_CLICK, controlador);
		reset.addEventListener(Events.ON_CLICK, controlador);
		captcha.addEventListener(Events.ON_CLICK, controlador);
		j_username.addEventListener(Events.ON_BLUR, controlador);
		this.addEventListener(Events.ON_OK, controlador);
	}
	
	
	public final EventListener getControlador() {
		return controlador;
	}
	
	
	public void quitarModal(){
		try {
			this.setMode("popup");
			this.setVisible(false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//overlapped, popup, embedded
	}
	
	public void activarModal(){
		try {
			this.setMode("modal");
			this.setVisible(true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//overlapped, popup, embedded
	}
	
	public void inicializar(){
		
		
		//this.setStyle("background:url('imagenes/fondoBotonV.png') repeat-x #2D2D2D; color:white; text-align: left; cursor:pointer; padding-top: 5px; padding-left: 1% ");
		//this.setStyle("background-color : #005800; background-image : none ");
		this.setTitle("Acceso");
		this.setBorder("normal");
		this.setWidth("330px");
		try {
			this.setMode("modal");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		forma = new Form();
		forma.setStyle("background-color:white;background-image : none");
		forma.setId("f");
		//forma.setVariable("method", "POST", true);
		//forma.setVariable("xmlns:h", "http://www.w3.org/1999/xhtml", false);
		//forma.setVariable("action", "j_spring_security_check", false);
		
		captcha = new Captcha();
		
		j_username = new Textbox();
		j_username.setId("u");
		
		j_password = new Textbox();
		j_password.setId("p");
		j_password.setType("password");
		
		cmbSedes = new CompCombobox();
		cmbSedes.setId("s");
		
		j_captcha = new Textbox();
		j_captcha.setId("c");
		
		lblSedes = new Label("Sedes");
		lblSedes.setId("lblSedes");
		
		submit = new Button("Aceptar");
		submit.setId(BOTONACEPTAR);

		reset = new Button("Resetear");
		reset.setId(BOTONRECETEAR);

		regenerar = new Button("regenerar");
		regenerar.setId(BOTONREGENERAR);
		
		//reset.setVariable("type", "reset", true);
		
	}
	
	public void dibujar(){
		Grid grid = new Grid();
		grid.setStyle(" border-style : hidden; border-color :white");
		rows = new Rows();
		Row row = new Row();
		row.setStyle("background-color:white; color:black;border-style : hidden; border-color :white");
		row.appendChild(new Label("Usuario"));
		row.appendChild(j_username);
		rows.appendChild(row);
		
		row = new Row();
		row.setStyle("background-color:white; color:black;border-style : hidden; border-color :white");
		row.appendChild(new Label("Clave"));
		row.appendChild(j_password);
		rows.appendChild(row);
		
		row = new Row();
		row.setStyle("background-color:white; color:black;border-style : hidden; border-color :white");
		row.appendChild(lblSedes);
		row.appendChild(cmbSedes);
		rows.appendChild(row);
		
		grid.appendChild(rows);
		
		forma.appendChild(grid);
		
		Groupbox grupo = new Groupbox();	
		grupo.appendChild(forma);
		grupo.setMold("3d");
		grupo.setStyle("background-color:white; color:#c6e1af");
		this.appendChild(grupo);
		Hbox caja = new Hbox();
		caja.setStyle("background-color:white; color:black; position:relative; width:100%; height:100%; margin-top:0px; margin-botton:0px");
		caja.appendChild(submit);
		caja.appendChild(reset);
		this.appendChild(caja);

		
	}
	
	public void setVisibleSedes(boolean visible) {
		this.lblSedes.setVisible(visible);
		this.cmbSedes.setVisible(visible);
	}
	
	public final Captcha getCaptcha() {
		return captcha;
	}

	public final void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}

	public final Textbox getJ_username() {
		return j_username;
	}

	public final Textbox getJ_password() {
		return j_password;
	}

	public final Textbox getJ_captcha() {
		return j_captcha;
	}

	public CompCombobox getCmbSedes() {
		return cmbSedes;
	}
	
	public void setCmbSedes(CompCombobox cmbSedes) {
		this.cmbSedes = cmbSedes;
	}

	public final void setCmbSedesModelo(List listaSedes) {
		this.cmbSedes.setModelo(listaSedes);
	}

	

}

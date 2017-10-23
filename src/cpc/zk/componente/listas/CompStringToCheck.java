package cpc.zk.componente.listas;

import java.io.Serializable;
import java.util.List;

 
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Vbox;

public class CompStringToCheck extends Vbox implements EventListener, Cloneable, Serializable{

	private static final long serialVersionUID = 9215796474306229559L;
	private char[]	mascara; 
	
	public void setMascara(char[] mascara) {
		this.mascara = mascara;
		dibujarIconos();
	}

	public void setMascara(String mascara) {
		if (mascara.length()==0)
			this.mascara = "0000000000000000".toCharArray();
		else
			this.mascara = mascara.toCharArray(); 
		dibujarIconos();
	}

	public void setMascara() {
		this.mascara = "0000000000000000".toCharArray(); 
		dibujarIconos();
	}
	
	public String getMascara() {
		return new String(mascara);
	}


	public CompStringToCheck() {
		super();		
	}

	public CompStringToCheck(String cadena) {
		super();
		mascara = cadena.toCharArray();
		dibujarIconos();
	}


	
	@SuppressWarnings("unchecked")
	private void dibujarIconos(){
		Checkbox activo;		
		Hbox caja = new Hbox();
		Hbox cajab = new Hbox();
		Image boton;
		if (mascara == null)
			this.mascara = "0000000000000000".toCharArray(); 
		List<String> iconos = (List<String>) SpringUtil.getBean("iconosAcceso");
		int i = 0;
		for (String acciones : iconos) {
			activo = new Checkbox();
			activo.setAttribute("ID", i);
			activo.setStyle("border: 2px solid white");
			activo.setChecked(mascara[i] == '1');
			activo.addEventListener(Events.ON_CHECK, this);
			caja.appendChild(activo);
			boton = new Image();
			boton.setSrc(acciones);
			cajab.appendChild(boton);
			i++;
		}
		this.appendChild(caja);
		this.appendChild(cajab);
	}

	public void onEvent(Event arg0) throws Exception {
		int i = (Integer) arg0.getTarget().getAttribute("ID");
		if (((Checkbox)arg0.getTarget()).isChecked())
			mascara[i] = '1';
		else	
			mascara[i] = '0';
	}
}
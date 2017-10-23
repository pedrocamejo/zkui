package cpc.zk.componente.menus;

import java.io.Serializable;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class CompMenuCRUD extends Menupopup implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7740344975387246432L;
	private Menuitem  item;
	private EventListener controlador;
	
	
	public CompMenuCRUD(){
		crear();
	}
	
	public CompMenuCRUD(EventListener controlador){
		this.controlador = controlador;
		crear();
	}
	
	
	
	public void crear(){
		item = new Menuitem("Agregar");
		item.setId("ADD");
		item.addEventListener(Events.ON_CLICK, controlador);
		this.appendChild(item);
		item = new Menuitem("editar");
		item.setId("EDI");
		item.addEventListener(Events.ON_CLICK, controlador);
		this.appendChild(item);
		item = new Menuitem("eliminar");
		item.setId("DEL");
		item.addEventListener(Events.ON_CLICK, controlador);
		this.appendChild(item);
		item = new Menuitem("consultar");
		item.setId("VER");
		item.addEventListener(Events.ON_CLICK, controlador);
		this.appendChild(item);
	}

	public EventListener getControlador() {
		return controlador;
	}

	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}
	
}

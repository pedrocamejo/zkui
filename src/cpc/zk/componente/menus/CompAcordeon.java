package cpc.zk.componente.menus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.Funcionalidad;
import cpc.ares.modelo.Modulo;
import cpc.zk.componente.interfaz.IZkAplicacion;

public class CompAcordeon extends Tabbox implements EventListener, Serializable{
	
	
	private static final long serialVersionUID = 7299910844658890878L;
	private List<Modulo> 			modelo;
	//private EventListener 	controlador; 
	private GenericAutowireComposer	app;
	private IZkAplicacion			sistema;
	
	public CompAcordeon(){
		super();
		this.modelo = new ArrayList<Modulo>();
		inicializar();
	}
	
	public CompAcordeon(List<Modulo> modelo) {
		super();
		this.modelo = modelo;
		inicializar();
		dibujar();
	}
	
	public void inicializar(){
		this.setMold("accordion");
		this.setWidth("195px");
		this.setHeight("500px");
		this.setStyle("overflow:auto; position:relative; width:100%; height:100%; margin-top:0px");
		this.setSclass("menu_principal");
	}
	
	/*
	this.setStyle("width:100%; margin:0%;  padding: 4% 4% 4% 4%; border: solid 1px #B4B4B4;background: white;"); 
    
	this.divModulo = new Div();		    
    this.divModulo.setStyle("padding: 0% 0% 0% 0%; border: solid 1px #2D2D2D; border-radius: 5px; -moz-border-radius: 5px; -webkit-border-radius: 5px;background: #C1C1C1;");
    
	this.divTitulo = new Div();			
	this.divTitulo.setStyle("background:url('imagenes/fondoBotonV.png') repeat-x #2D2D2D; color:white; text-align: left; cursor:pointer; padding-top: 5px; padding-left: 1% ");
	this.divTitulo.setHeight("18px");	


	*/
	
	public void dibujar(){
		Tabs menuaco = new Tabs();
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setHeight("300");
		tabpanels.setWidth("194px");
		//tabpanels.setStyle("overflow:auto; background-color:#020601");
		tabpanels.setStyle("overflow:auto; position:relative; width:100%; height:100%; margin-top:0px");
		Tabpanel 		tabpanel;
		Listbox			marco;
		Listitem		item;
		Tab 			tab; 
		 
		
		for (Modulo modulo : modelo) {
			tab = new Tab();
			tab.setLabel(modulo.getNombre());
			tab.setId(""+modulo.getIdmodulo());
			//tab.setStyle("background-color:green; repeat-x #2D2D2D; background-image:none;  color:white; text-align: left; cursor:pointer; padding-top: 5px; padding-left: 1% ");
			tabpanel = new Tabpanel();
			tabpanel.setHeight("300");
			tabpanel.setStyle("overflow:auto; position:relative; width:100%; height:100%; margin-top:0px");
			//tabpanel.setStyle("overflow:auto; background-color:white");
			marco =  new Listbox();
			marco.setStyle("background-color:white");
			marco.setWidth("190x");
			if (modulo.getFuncionalidades().size()<7)
				marco.setRows(modulo.getFuncionalidades().size());
			else
				marco.setRows(7);
			marco.addEventListener(Events.ON_SELECTION, this);
			marco.addEventListener(Events.ON_SELECT, this);
			for (Funcionalidad funcionalidad : modulo.getFuncionalidades()) {
				item =  new Listitem();
				item.setStyle("background-color:white; color:black");
				item.setImage(funcionalidad.getIcono());
				item.setLabel(funcionalidad.getNombre());
				
				item.setAttribute("Id",funcionalidad.getIdservicio() );
				item.setAttribute("funcionalidad", funcionalidad.getComando());
				marco.appendChild(item);
			}
			tabpanel.appendChild(marco);
			menuaco.appendChild(tab);
			tabpanels.appendChild(tabpanel);
		}
		this.appendChild(menuaco);
		this.appendChild(tabpanels);
	}
	
	public List<Modulo> getModelo() {
		return modelo;
	}

	public void setModelo(List<Modulo> modelo) {
		this.modelo = modelo;
	}

	public void onEvent(Event event) throws Exception {
		Listbox componente = (Listbox) event.getTarget();
		Listitem item =  componente.getSelectedItem();
		IComando comandoActivo = (IComando)item.getAttribute("funcionalidad");
		comandoActivo.setApp(sistema);
		comandoActivo.agregarParametro("root", event.getTarget().getRoot());
		comandoActivo.ejecutar();
		// Desmarcar para que se ejecute nuevamente el comando on select
		componente.setSelectedItem(null);
	}

	public GenericAutowireComposer getApp() {
		return app;
	}

	public void setApp(GenericAutowireComposer index) {
		this.app = index;
	}

	public IZkAplicacion getSistema() {
		return sistema;
	}

	public void setSistema(IZkAplicacion sistema) {
		this.sistema = sistema;
	}
	
	
	
}

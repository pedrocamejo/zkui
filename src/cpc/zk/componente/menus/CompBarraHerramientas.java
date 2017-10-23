package cpc.zk.componente.menus;

import java.io.Serializable;
import java.util.List;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Listbox;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;

public class CompBarraHerramientas extends Toolbar implements Serializable{

	private static final long   serialVersionUID = 3376098668903746408L;	
	private AccionFuncionalidad funcionalidad;
	private EventListener       controlador;
	private Listbox				tipoReporte;
	private int 				tipo;
	
	
	public CompBarraHerramientas(AccionFuncionalidad seguridad, int tipo, EventListener controlador) {
		super();
		this.funcionalidad 	= seguridad;
		this.tipo 			= tipo;
		this.controlador 	= controlador;
		CrearComboReporte();
		inicializar();
		dibujar();
	}
	
	private void CrearComboReporte(){
		tipoReporte = new Listbox();
		tipoReporte.setMold("select");
		Listitem	item = new Listitem();
		item.setLabel("PDF");
		item.setValue("pdf");
		item.setSelected(true);
		tipoReporte.appendChild(item);
		item = new Listitem();
		item.setLabel("HTML");
		item.setValue("html");
		tipoReporte.appendChild(item);
		item = new Listitem();
		item.setLabel("Word (RTF)");
		item.setValue("rtf");
		tipoReporte.appendChild(item);
		item = new Listitem();
		item.setLabel("Excel");
		item.setValue("xls");
		tipoReporte.appendChild(item);
		item = new Listitem();
		item.setLabel("Excel (JXL)");
		item.setValue("jxl");
		tipoReporte.appendChild(item);
		item = new Listitem();
		item.setLabel("CSV");
		item.setValue("csv");
		tipoReporte.appendChild(item);
		item = new Listitem();
		item.setLabel("OpenOffice (ODT)");
		item.setValue("odt");
		tipoReporte.appendChild(item);
	}
	
	private void dibujar() {		
		this.setHeight("30px");
	}

	@SuppressWarnings("unchecked")
	private void inicializar(){
		boolean imprime=false;
		this.setSclass("vista");

		List<String> iconos = (List<String>) SpringUtil.getBean("iconosbarra");
		Toolbarbutton boton;
		
		for (Accion accion : funcionalidad.extraerAcciones(tipo)) {
			if (accion.isEstado()){
				System.out.println("Id " +accion.getIdaccion());
				boton = new Toolbarbutton();
				if (accion.getIdaccion() <= iconos.size()){
					System.out.println(iconos.get(accion.getIdaccion()));
					boton.setImage(iconos.get(accion.getIdaccion()));
				}	
				if (accion.getIdaccion() == 6 ||accion.getIdaccion() == 7)
					imprime =true;
				boton.setTooltiptext(accion.getDescripcion());
				boton.setId("B"+accion.getIdaccion()+controlador.hashCode());
				boton.setAttribute("pos", accion.getIdaccion());				
				if (accion.getIdaccion() <12){
					//boton.setCtrlKeys("#f"+(accion.getIdaccion()+1)+"</dt>" );
				}
				boton.addEventListener("onClick", controlador);
				this.appendChild(boton);	
			}
		}
		if (imprime)
			this.appendChild(tipoReporte);
	}
	
	public AccionFuncionalidad getFuncionalidad() {
		return funcionalidad;
	}
	
	public void desactivarTipoReporte(){
		tipoReporte.setDisabled(true);
	}

	public Listbox getTipoReporte() {
		return tipoReporte;
	}
	
}

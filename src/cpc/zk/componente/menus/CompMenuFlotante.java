package cpc.zk.componente.menus;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.Funcionalidad;
import cpc.ares.modelo.Modulo;
import cpc.zk.componente.interfaz.IZkAplicacion;

public class CompMenuFlotante extends Div implements EventListener{
	private static final long serialVersionUID = -2232583733960798687L;
	
	private List<Modulo> modulos;	
	private Div divModulo;
	private Div divTituloModulo; 
	private Hbox boxCuerpo;
	private IZkAplicacion			app;

	public CompMenuFlotante(){
		super();		
		this.modulos = new ArrayList<Modulo>();
		inicializar();
	}
	
	public CompMenuFlotante(List<Modulo> modelo, IZkAplicacion app) {
		super();		
		this.modulos = modelo;
		this.app = app;
		inicializar();
		dibujar();
	}
	
	public void inicializar(){		
		this.setStyle("margin:5px;width:200px; padding: 7px; border: solid 1px #8c8585; background: url('imagenes/fondotitulomenu.png') repeat-x #E5E3E4; position: fixed;border-radius: 5px; -moz-border-radius: 5px; -webkit-border-radius: 5px;");		
	}
	
	
	public void dibujar(){
		int i = 0;			
		for (Modulo modulo : modulos) {			 
		    
			this.divModulo = new Div();
			this.divModulo.setStyle("border: solid 1px #2D2D2D; border-radius: 5px; -moz-border-radius: 5px; -webkit-border-radius: 5px;background: #C1C1C1;");
			if (i==0)				
				this.divModulo.setStyle("margin-top:25px; border: solid 1px #2D2D2D; border-radius: 5px; -moz-border-radius: 5px; -webkit-border-radius: 5px;background: #C1C1C1;");
			
		    
			this.divTituloModulo = new Div();			
			this.divTituloModulo.setStyle("background:url('imagenes/fondomodulo.png') repeat-x #2D2D2D; color:white; text-align: left; cursor:pointer; padding-top: 5px; padding-left: 1% ");
			this.divTituloModulo.setHeight("18px");		
			this.divTituloModulo.appendChild(new Label(modulo.getNombre()));
			this.divTituloModulo.setId("titulo");
			this.divTituloModulo.addEventListener(Events.ON_CLICK, this);
			
			this.divModulo.appendChild(this.divTituloModulo);			
			Listbox lstBox = new Listbox();
			lstBox.setStyle("border: solid 0px #B4B4B4; border-radius: 10px; -moz-border-radius: 10px; -webkit-border-radius: 10px; font-weight:bold; font-size:7pt;");
			lstBox.setRows(5);
			lstBox.setWidth("190px");
			if (modulo.getFuncionalidades().size()<5)
				lstBox.setRows(modulo.getFuncionalidades().size());
			
			for (Funcionalidad funcionalidad : modulo.getFuncionalidades()) {
				this.boxCuerpo = new Hbox();
				this.boxCuerpo.setStyle("width:80%; padding: 5px; font-weight: 700; color:black; ");
				 
				Listitem item = new Listitem(funcionalidad.getNombre());	
				item.setStyle("background-color: #C1C1C1; ");
				item.setImage(funcionalidad.getIcono());		
				
				item.setId(""+funcionalidad.getIdservicio() );
				item.setAttribute("funcionalidad", funcionalidad.getComando());
				
				item.addEventListener(Events.ON_CLICK, this);
			
				
				lstBox.appendChild(item);				
				this.boxCuerpo.appendChild(lstBox);
				
				
				
			}
			if (i==0)
				this.boxCuerpo.setVisible(true);
			else
				this.boxCuerpo.setVisible(false);
			i++;
			this.divModulo.appendChild(this.boxCuerpo);
			
			
			appendChild(divModulo);
			Separator sep = new Separator();
			sep.setHeight("5px");
			appendChild(sep);
			
			
		} 
		
	}
	
	public List<Modulo> getModelo() {
		return modulos;
	}

	public void setModelo(List<Modulo> modelo) {
		this.modulos = modelo;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		try{			
			if (event.getName() == Events.ON_CLICK){
				
				if (event.getTarget() instanceof Listitem){					
					Listitem item =  (Listitem) event.getTarget();
					IComando comandoActivo = (IComando)item.getAttribute("funcionalidad");
					comandoActivo.setApp(app);
					comandoActivo.agregarParametro("root", event.getTarget().getRoot());
					comandoActivo.ejecutar();
				}
				
				if (event.getTarget() instanceof Div){				
					for (Object hijo :event.getTarget().getParent().getChildren()) {					
						if (hijo instanceof Hbox)
							((Hbox) hijo).setVisible(!((Hbox) hijo).isVisible());						
						if (hijo instanceof Separator)
							((Separator) hijo).setVisible(!((Separator) hijo).isVisible());					
					}
				}
				
			}
			
			
			/*IComando comandoActivo = (IComando)event.getTarget().getAttribute("funcionalidad");
			comandoActivo.agregarParametro(1, event.getTarget().getRoot());
			comandoActivo.ejecutar();*/
			
			
			
		} catch (java.lang.NullPointerException e){
			
		}
	}

	public IZkAplicacion getApp() {
		return app;
	}

	public void setApp(IZkAplicacion app) {
		this.app = app;
	}
	
	
	

}


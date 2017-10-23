package cpc.zk.componente.ventanas;


import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.api.InputElement;
import java.io.Serializable;

public abstract class CompVentana extends Window implements Serializable, EventListener {	
private static final long serialVersionUID = 3363395307745620300L;

	public abstract void inicializar();
	public abstract void dibujar();
	
	protected Button				aceptar, cancelar;
	private EventListener 			controlador;
	
	private String					titulo;
	private int						ancho;	
	protected Label					opciones;
	
	//private List<Component> 	componentes;
	
	
	public CompVentana(){
		init();
	}
	
	public CompVentana(String titulo){
		this.setTitle(titulo);
		this.titulo = titulo;
		this.ancho = 700;
		init();
		dibujarVentana();
	}
	
	public CompVentana(EventListener controlador,String titulo, int ancho){
		this.setTitle(titulo);
		this.setWidth(ancho+"px");
		init();
		setControlador(controlador);
		dibujarVentana(titulo,ancho);
		/*this.titulo = titulo;
		this.ancho = ancho;*/
		
	}
	
		
	protected void init(){
		opciones = new Label("(F4) Aceptar, (Esc) Cancelar ");
		this.setBorder("normal");
		this.setClosable(true);
		aceptar = new Button("Aceptar");
		this.addEventListener(Events.ON_CANCEL, this);
		this.addEventListener(Events.ON_OK, this);
		cancelar = new Button("Cancelar");
		try {
			this.setMode("modal");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public void dibujarVentana(String titulo, int ancho){
		this.setTitle(titulo);
		this.setWidth(ancho+"px");
		inicializar();
		dibujar();
	}
	
	public void dibujarVentana(){
		this.setTitle(titulo);
		this.setWidth(ancho+"px");
		inicializar();
		dibujar();
	}
	
	public void validar(InputElement elemento, String cadenaValida, String mensaje){
		elemento.setConstraint(cadenaValida+": "+mensaje);
	}
	
	
	public void close(){
		try {
			this.setMode("popup");
			this.onClose();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
		aceptar.addEventListener(Events.ON_CLICK, controlador);
		cancelar.addEventListener(Events.ON_CLICK, this);
	}


	public Button getAceptar() {
		return aceptar;
	}


	public Button getCancelar() {
		return cancelar;
	}


	public EventListener getControlador() {
		return controlador;
	}

	public void addBotonera(){
		Hbox horizontal = new Hbox();
		horizontal.appendChild(aceptar);
		horizontal.appendChild(cancelar);
		horizontal.appendChild(opciones);
		this.appendChild(horizontal);
	}
	
	
	public void onEvent(Event event) throws Exception {
		if (event.getName() == Events.ON_OK)
			new Event(Events.ON_CLICK, getAceptar());
		else if (event.getName() == Events.ON_CANCEL)
			close();
		else if (event.getTarget()==cancelar)
			close();
	}
	
	public void agregarGrupoDatos(CompGrupoDatos grpDatos){
		this.appendChild(grpDatos);
		
	}
	
}

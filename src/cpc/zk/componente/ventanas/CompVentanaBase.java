package cpc.zk.componente.ventanas;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.api.InputElement;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;

public abstract class CompVentanaBase<T> extends Window implements Serializable, EventListener {	
private static final long serialVersionUID = 3363395307745620300L;
	
	protected Button				aceptar, cancelar;
	private EventListener 			controlador;
	
	private T 						modelo;
	private DataBinder				binder;
	
	private String					titulo;
	private int						ancho;	
	protected Label					opciones;
	
	//private List<Component> 	componentes;
	
	
	public CompVentanaBase(){
		init();
	}
	
	public CompVentanaBase(String titulo){
		this.setTitle(titulo);
		this.titulo = titulo;
		this.ancho = 700;
		init();
	}
	
	public CompVentanaBase(String titulo, int ancho){
		this.setTitle(titulo);
		this.setWidth(ancho+"px");
		this.titulo = titulo;
		this.ancho = ancho;
		init();
	}
	
		
	protected void init(){
		opciones = new Label("(F4 - Enter) Aceptar, (Esc) Cancelar ");
		binder = new AnnotateDataBinder(this);
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
	
	public void dibujarVentana(String titulo, int ancho) throws ExcDatosInvalidos{
		this.setTitle(titulo);
		this.setWidth(ancho+"px");
		inicializar();
		dibujar();
	}
	
	public void dibujarVentana() throws ExcDatosInvalidos{
		this.setTitle(titulo);
		this.setWidth(ancho+"px");
		inicializar();
		dibujar();
	}
	
	public void cargar() throws ExcDatosInvalidos{
		cargarValores(modelo);
		binder.loadAll();
	}
	
	
	public void validar(InputElement elemento, String cadenaValida, String mensaje){
		elemento.setConstraint(cadenaValida+": "+mensaje);
	}
	
	
	public DataBinder getBinder(){
		return binder;
	}
	
	public void actualizarModelo(){
		binder.saveAll();
		binder.loadAll();
	}
	
	public void close(){
		try {
			try{
				binder.saveAll();
			}catch (NullPointerException e) {
			
			}
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

	public T getModelo() {
		return modelo;
	}

	public void setModelo(T modelo) {
		this.modelo = modelo;
		binder.bindBean(getNombreModelo(), modelo);
	}
	
	public String getNombreModelo(){
		String cadena = modelo.getClass().getName().toLowerCase();
		return cadena.substring(cadena.lastIndexOf(".")+1);
	}

	public void addBotonera(){
		Hbox horizontal = new Hbox();
		horizontal.appendChild(aceptar);
		horizontal.appendChild(cancelar);
		horizontal.appendChild(opciones);
		this.appendChild(horizontal);
	}
	
	
	public  void enriqueserComponente(InputElement componente, int ancho) {
		try {
			componente.setWidth(ancho+"px");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enriqueserComponente(InputElement componente, int ancho, String validador, String mensaje) {
		try {
			componente.setWidth(ancho+"px");
			componente.setConstraint(validador+": "+mensaje);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void crearMetodo(InputElement componente, String metodo, Object argumento){
		try{
			String metodo2 = "set"+metodo.substring(0, 1).toUpperCase()+metodo.substring(1);
			Class clase = componente.getClass();
			Method metodoEjecutable;
			if (metodo == "value")
				metodoEjecutable = clase.getMethod(metodo2, String.class);
			else if (metodo.indexOf("Model")>=0)
				metodoEjecutable = clase.getMethod(metodo2, ArrayList.class);
			else
				metodoEjecutable = clase.getMethod(metodo2, Object.class);
			metodoEjecutable.invoke(componente, argumento);
		}
		catch ( Exception e )
		{
		    System.out.println("fallo invocando function " + e.getMessage());
		}
	}
	
	public  void enriquecerComponente(InputElement componente, int ancho, String metodo, Object metodoGet, String nombreCampo) {
		try {
			componente.setWidth(ancho+"px");
			binder.addBinding(componente, metodo,  nombreCampo, null, null, "save", null, null, null, null);
			crearMetodo(componente, metodo, metodoGet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enriqueserComponente(InputElement componente, int ancho,  String metodo, Object metodoGet, String nombreCampo, String validador, String mensaje) {
		try {
			componente =  (InputElement) Class.forName(componente.getClass().getName()).newInstance();
			componente.setWidth(ancho+"px");
			componente.setConstraint(validador+": "+mensaje);
			binder.addBinding(componente, metodo,  getNombreModelo()+nombreCampo, null, null, "save", null, null, null, null);
			crearMetodo(componente, metodo, metodoGet);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void onEvent(Event event) throws Exception {
		if (event.getName() == Events.ON_OK)
			new Event(Events.ON_CLICK, getAceptar());
		else if (event.getName() == Events.ON_CANCEL)
			close();
		else if (event.getTarget()==cancelar)
			close();
		else if (event.getName() == Events.ON_CTRL_KEY){
			System.out.println(((KeyEvent)event).getKeyCode());
			if (((KeyEvent)event).getKeyCode() == 115)
				new Event(Events.ON_CLICK, getAceptar());
		}
	}
	
	public abstract void inicializar();
	public abstract void dibujar();
	public abstract void cargarValores(T dato) throws ExcDatosInvalidos;
}

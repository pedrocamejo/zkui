package cpc.zk.componente.ventanas;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Vbox;

import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ui.ComponentesAutomaticos;


public class CompGrupoBusqueda<T> extends CompGrupoDatos implements EventListener, Serializable, ICompCatalogo<T>{

	
	private static final long serialVersionUID = -4093182236897194883L;
	private CompBuscar<T>			buscador;
	private Button					crear;
	private Button					editar;
	private Button					consultar;
	private T						modelo;
	
	private List<Label>				etiquetas;
	private List<Component>			valores;
	private List<CompEncabezado>	catalogo;
	private ComponentesAutomaticos	tipoComponente;
	private T						vacio;
	
	
	public CompGrupoBusqueda(String stitulo, List<CompEncabezado> catalogo, T modelo) throws ExcAccesoInvalido{		
		super(stitulo,4);
		this.catalogo= catalogo;
		this.modelo = modelo;
		tipoComponente = ComponentesAutomaticos.LABEL;
		inicializarBusqueda();
		
	}
	
	public CompGrupoBusqueda(String stitulo, int columnas, List<CompEncabezado> catalogo, T modelo) throws ExcAccesoInvalido{
		super(stitulo);
		if (columnas <2)
			columnas = 2;
		super.nroColumna = columnas;
		this.catalogo= catalogo;
		this.modelo = modelo;
		tipoComponente = ComponentesAutomaticos.LABEL;
		inicializarBusqueda();
	}
	
	public CompGrupoBusqueda(String stitulo, List<CompEncabezado> catalogo, T modelo, ComponentesAutomaticos tipoComponente) throws ExcAccesoInvalido{		
		super(stitulo,4);
		this.catalogo= catalogo;
		this.modelo = modelo;
		this.tipoComponente = tipoComponente;
		inicializarBusqueda();
		
	}
	
	public CompGrupoBusqueda(String stitulo, int columnas, List<CompEncabezado> catalogo, T modelo, ComponentesAutomaticos tipoComponente) throws ExcAccesoInvalido{
		super(stitulo);
		if (columnas <2)
			columnas = 2;
		super.nroColumna = columnas;
		this.catalogo= catalogo;
		this.modelo = modelo;
		this.tipoComponente = tipoComponente;
		inicializarBusqueda();
	}
	
	
	private void inicializarBusqueda() throws ExcAccesoInvalido{		
		etiquetas = new ArrayList<Label>();
		valores = new ArrayList<Component>();
		buscador = new CompBuscar<T>(catalogo, 0);
		crear	 = new Button();
		editar	 = new Button();
		consultar= new Button();
		String icono1 = (String) SpringUtil.getBean("iconoCrear");
		String icono2 = (String) SpringUtil.getBean("iconoEditar");
		String icono3 = (String) SpringUtil.getBean("iconoConsulta");
		crear.setImage(icono1);
		editar.setImage(icono2);
		consultar.setImage(icono3);
		buscador.setWidth("120px");
		buscador.addEventListener(CompBuscar.ON_SELECCIONO, this);
		cargarValores();
	}
	
	public void addListener(EventListener controlador){
		crear.addEventListener(Events.ON_CLICK, controlador);
		editar.addEventListener(Events.ON_CLICK, controlador);
		consultar.addEventListener(Events.ON_CLICK, controlador);
	}

	private void cargarValores() throws ExcAccesoInvalido{
		for (CompEncabezado item : catalogo) {
			switch (tipoComponente) {
				case LABEL:
					Label componente = new Label();
					componente.setWidth("150px");
					agregarValor(item.getDescripcion()+" :", componente, item.getMetodoBinder(), ComponentesAutomaticos.LABEL);
					break;
				case TEXTBOX:
					Textbox componente2 = new Textbox();
					componente2.setReadonly(true);
					componente2.setWidth("150px");
					agregarValor(item.getDescripcion()+" :",componente2, item.getMetodoBinder(), ComponentesAutomaticos.TEXTBOX);
					break;
				default:
					agregarValor(item.getDescripcion()+" :", new Label(), item.getMetodoBinder(), ComponentesAutomaticos.LABEL);
					break;
			}
		}
	}
	
	public void setAnchoCatalogo(int ancho){
		buscador.setAncho(ancho);
	}
	
	public void setAnchoValores(int ancho){
		for (Component item : valores) {
			switch (tipoComponente) {
				case LABEL:
					((Label)item).setWidth(ancho+"px");
					break;
				case TEXTBOX:
					((Textbox)item).setWidth(ancho+"px");
					break;
			}
		}
	}
	

	
	public void setModeloCatalogo(List<T> modelos){
		buscador.setModelo(modelos);
	}
	
	public void dibujar(){
		super.dibujar();
		dibujarBuscador();
		rellenarColumnas();
		for (int i=0; i<etiquetas.size(); i++) {
			this.addComponente(etiquetas.get(i));
			this.addComponente(valores.get(i));
		}
	}
	
	private void dibujarBuscador(){
		Hbox contenedor = new Hbox();
		contenedor.appendChild(buscador);
		contenedor.appendChild(crear);
		contenedor.appendChild(editar);
		contenedor.appendChild(consultar);
		Vbox salida = new org.zkoss.zul.Vbox();
		salida.appendChild(contenedor);
		this.addComponente(new Label("Busqueda:"));
		this.addComponente(salida);
	}
	
	
	private void rellenarColumnas(){
		if (nroColumnas>2){
			int total = nroColumnas-2;
			for (int i=0; i<total;i++){
				this.addComponente(new Label());
			}
		}
	}
	

	
	public void agregarValor(String etiqueta, Component componente, String metodoGet, ComponentesAutomaticos  tipoComponente ){
		etiquetas.add(new Label(etiqueta));
		valores.add(cargarDato(componente, metodoGet, tipoComponente));
	}
	
	private Component cargarDato(Component componente, String metodoGet, ComponentesAutomaticos  tipoComponente) {
		componente.setAttribute("metodo", metodoGet);
		componente.setAttribute("tipo", tipoComponente);
		return componente;
	}

	public void actualizarValores() throws ExcAccesoInvalido {
		actualizarValores(modelo);
	}
	
	@SuppressWarnings("unchecked")
	public void actualizarValores(Object modelo) throws ExcAccesoInvalido {
		Object modeloTmp = null;
		if (modelo == null)
			if (vacio == null)
				throw new ExcAccesoInvalido("Modelo Nulo");
			else
				modeloTmp = vacio;
		else
			modeloTmp = modelo;
		try{
			Class<T> clase = (Class<T>) modeloTmp.getClass();
			Method metodoEjecutable;
			for (Component item : valores) {
				metodoEjecutable = clase.getMethod((String) item.getAttribute("metodo"), null);
				switch ((ComponentesAutomaticos)item.getAttribute("tipo")) {
					case TEXTBOX:
						((Textbox)item).setValue((String) metodoEjecutable.invoke(modelo, null));
						break;
					case LABEL:
						((Label)item).setValue((String) metodoEjecutable.invoke(modelo, null));
						break;
					case LISTA:
						((CompLista<T>)item).setModelo((List<T>) metodoEjecutable.invoke(modelo, null));
						break;
					default:
						break;
				} 
			}	
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public T getModelo(){
		if (buscador.getSeleccion() != null)
			return buscador.getSeleccion();
		return modelo;
	}
	
	public void setModelo(T modelo){
		this.modelo =  modelo;
	}
	
	public void onEvent(Event evento) throws Exception {
		if (evento.getTarget() == buscador){			
			if (buscador.getSeleccion() != null)
				actualizarValores(buscador.getSeleccion());	
		}		
	}

	public void agregarDato(Object dato) {
		setModelo( (T) dato);
		try {
			actualizarValores((T) dato);
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void cargarVentanaModal(IZkAplicacion aplicacion, CompVentanaBase<T> ventana) {
		aplicacion.agregarHija(ventana);
	}
		

	public void editarDato(Object dato) {
		setModelo( (T) dato);
		try {
			actualizarValores((T) dato);
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminarDato() {
		// TODO Auto-generated method stub
		
	}

	public Integer getIdposicion() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNuevo(T nuevo){
		vacio = nuevo;
	}

	public Button getCrear() {
		return crear;
	}

	public Button getEditar() {
		return editar;
	}
	
	public void setVisibleCrear(boolean visible) {
		crear.setVisible(visible);
	}

	public void setVisibleEditar(boolean visible) {
		editar.setVisible(visible);
	}
	
	public void setVisibleConsultar(boolean visible) {
		consultar.setVisible(visible);
	}
	
	public void setDisabled(boolean disabled){
		crear.setDisabled(disabled);
		editar.setDisabled(disabled);
		buscador.setDisabled(disabled);
	}
	
	
	public void setModoEdicion(boolean edicion){
		consultar.setDisabled(edicion);
		crear.setDisabled(!edicion);
		editar.setDisabled(!edicion);
		buscador.setDisabled(!edicion);
	}

	public Button getConsultar() {
		return consultar;
	}

	public CompBuscar<T> getBuscador() {
		return buscador;
	}

	public void setBuscador(CompBuscar<T> buscador) {
		this.buscador = buscador;
	}

	public List<Component> getValores() {
		return valores;
	}

	public void setValores(List<Component> valores) {
		this.valores = valores;
	}
	
	
}

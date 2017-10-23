package cpc.zk.componente.listas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Vbox;

import cva.pc.componentes.CompEncabezado;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class CompListaBusca<T> extends Vbox implements EventListener, Serializable, ICompCatalogo<T>{

	
	private static final long serialVersionUID = 4503293555616304112L;
	private CompBuscar<T>		buscar;
	private	CompLista<T> 		lista;
	private Hbox 				botonera;
	private CompGrupoDatos		grpBusqueda,grpResultado;	
	private List<CompEncabezado> encabezados;
	private Integer				indice;
	
	private Button				botonAgregar;
	private Button				botonCrear;
	
	private EventListener		controlador = null;
	private boolean 			puedeCrear = true;
	
	
	private List<T> 				modeloSelect;
		
	public CompListaBusca(List<CompEncabezado>  encabezados, int indice, EventListener controlador){
		super();
		this.controlador	= controlador;
		this.encabezados 	=  encabezados;
		this.indice 		= indice;
		iniciar();
		dibujar1();
	}
	
	public CompListaBusca(List<CompEncabezado>  encabezados, int indice, boolean puedeCrear,EventListener controlador){
		super();
		this.controlador	= controlador;
		this.encabezados 	= encabezados;
		this.indice 		= indice;
		this.puedeCrear     = puedeCrear;
		iniciar();
		dibujar();
	}

	public CompListaBusca(List<CompEncabezado>  encabezados, int indice, EventListener controlador, Component padre){
		super();
		this.controlador	= controlador;
		this.encabezados 	= encabezados;
		this.indice 		= indice;		
		iniciar();
		lista = new CompLista<T>(encabezados,padre);
		lista.setHeight("150px");
		dibujar();		
	}
	
	private void iniciar(){		
		grpBusqueda     	= new CompGrupoDatos("Busqueda",3);
		grpBusqueda.setAnchoColumna(0,200);
		
		grpResultado     	= new CompGrupoDatos("Resultado",1);
		
				
		botonAgregar 	= new Button();
		botonCrear 		= new Button();
		buscar 			= new CompBuscar<T>(encabezados, indice);
		String icono = (String) SpringUtil.getBean("iconoAdd");
		String icono2 = (String) SpringUtil.getBean("iconoCrear");
		botonera = new Hbox();
		botonAgregar.setImage(icono);
		botonCrear.setImage(icono2);
		botonAgregar.setContext("Agregar");
		botonAgregar.setContext("Crear");
		botonAgregar.addEventListener(Events.ON_CLICK, this);
		botonCrear.addEventListener(Events.ON_CLICK, controlador);

		lista = new CompLista<T>(encabezados);
	}
	
	private void dibujar1(){
		botonera.appendChild(buscar);
		botonera.appendChild(botonAgregar);
		botonera.appendChild(botonCrear);
		this.appendChild(botonera);
		this.appendChild(lista);
	}
	
	private void dibujar(){		
		grpBusqueda.addComponente("Buscar: ", buscar);		
		botonera.appendChild(botonAgregar);
		if (this.puedeCrear)			
			botonera.appendChild(botonCrear);
		grpBusqueda.addComponente(botonera);
		grpBusqueda.dibujar(this);		
		grpResultado.addComponente(lista);
		grpResultado.dibujar(this);
	}
	
	public void onEvent(Event arg0) throws Exception {
		if (buscar.getSeleccion() != null){	
			lista.agregar(buscar.getSeleccion());			
		}
	}
	
	
	public List<T> getModelo() {	
		return lista.getModelo();
	}
	
	public void setModelo(List<T> datos){
		buscar.setModelo(datos);
	}
	
	public void setAncho(int Ancho){
		int ancho = Ancho-5;
		this.setWidth(Ancho+"px");
		lista.setWidth(ancho+"px");
		buscar.setWidth((ancho-70)+"px");
		buscar.setAncho(Ancho);
		botonCrear.setWidth(25+"px");
		botonAgregar.setWidth(25+"px");
	}

	public boolean isPuedeCrear() {
		return puedeCrear;
	}

	public void setPuedeCrear(boolean puedeCrear) {
		this.puedeCrear = puedeCrear;
		if (botonCrear!=null){
			botonCrear.setVisible(false);
		}
			
	}	
	public void limpiarLista() {
		this.lista.getChildren().clear();
		lista.getModelo().clear();
		lista.crearEncabezado(this.encabezados);
		lista.agregarMenuContextual();
	}
	
	public void setRows(int rows){
		lista.setRows(rows);
	}
	
	public void setHeigth(int alto){
		lista.setHeight(alto+"px");
	}
	

	@SuppressWarnings("unchecked")
	public List getModeloSelect(){
		try{
			modeloSelect = new ArrayList();
			lista.refrescar();
			for (Object  celda : lista.getItems()) {
				System.out.println(((Listitem) celda).getAttribute("dato"));
				modeloSelect.add((T) ((Listitem) celda).getAttribute("dato"));
			}
			return modeloSelect;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void setModeloSelect(ArrayList<T> objetos){
		modeloSelect = objetos;
		lista.getItems().clear();
		if (objetos != null)
			for (T item: objetos) {
				lista.agregar(item);
			}
	}

	public void setModeloSelect(List<T> objetos){
		modeloSelect = objetos;
		lista.getItems().clear();
		if (objetos != null)
			for (T item: objetos) {
				lista.agregar(item);
			}
	}
	
	@SuppressWarnings("unchecked")
	public void setModeloSelect(Object objetos){
		modeloSelect = (List<T>) objetos;
		lista.getItems().clear();
		if (objetos != null)
			for (T item: modeloSelect) {
				lista.agregar(item);
			}
	}

	public void agregarDato(Object dato) {
		lista.agregar((T) dato);	
	}

	public void editarDato(Object dato) {
	}

	public void eliminarDato() {
	}

	public Integer getIdposicion() {
		return null;
	}

	public Button getBotonCrear() {
		return botonCrear;
	}

	public void cargarVentanaModal(IZkAplicacion aplicacion, CompVentanaBase<T> ventana) {
		aplicacion.agregarHija(ventana);
	}
	
	public void agragarMenuContextualItem(){
		this.lista.agregarMenuContextual();
	}
	
	public void setDisabled(boolean value){
		this.botonAgregar.setDisabled(value);
		this.botonCrear.setDisabled(value);
		this.lista.setDisabled(value);
		this.buscar.setDisabled(value);
	}

	public CompBuscar<T> getBuscar() {
		return buscar;
	}

	public void setBuscar(CompBuscar<T> buscar) {
		this.buscar = buscar;
	}
}

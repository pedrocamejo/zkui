package cpc.zk.componente.listas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cva.pc.componentes.CompEncabezado;
import cpc.zk.componente.modelo.ColumnaAuxiliar;

public class CompGrillaConBoton<T> extends Vbox implements EventListener, Serializable{
	
	private static final long serialVersionUID = -7653783859880846587L;
	private CompGrilla<T>	grilla;					
	private Hbox 			botonera,contPreBotonera,contPostBotonera, contPreGrid;
	private	 List<T> 		coleccion;
	private Button			boton;
	private T				vacio;
	
	
	public CompGrillaConBoton(EventListener controlador){
		super();
		iniciar(controlador);
	}
	
	public CompGrillaConBoton(ArrayList<Component> componentes, List<T> coleccion,  List<CompEncabezado>  encabezados, EventListener controlador){
		super();
		iniciar(controlador);
		setComponentes(componentes);
		setEncabezados(encabezados);
		if (coleccion==null)
			coleccion = new ArrayList<T>();
		setColeccion(coleccion);
		cargar();
	}
	
	private void iniciar(EventListener controlador){		
		boton = new Button("Agregar Detalle");
		contPreBotonera = new Hbox();
		contPostBotonera = new Hbox();		
		botonera = new Hbox();
		contPreGrid = new Hbox();
		boton.setImage((String) SpringUtil.getBean("iconoAdd"));
		boton.addEventListener(Events.ON_CLICK, this);
		//this.setAlign("end");
		this.setAlign("begin");
		grilla = new CompGrilla<T>(controlador);
	}
	
	
	public List<T> getColeccion() {
		return grilla.getColeccion();
	}
	
	public void cargar(){
		grilla.cargar(coleccion);
	}
	
	public Window findVentana(){
		return grilla.findVentana();
	}
	
	public void setColeccion(List<T> coleccion){
		this.coleccion = coleccion;
	}
	
	public void setEncabezados(List<CompEncabezado> encabezados){
		grilla.setEncabezados(encabezados);
	}
	
	public void addMenuAutomatico(){
		grilla.addMenuAutomatico();
	}
	
	public void refrescar(){
		grilla.refrescar();
	}
	
	public void dibujar(){		
		
		this.botonera.appendChild(boton);
		this.contPreGrid.appendChild(contPreBotonera);
		this.contPreGrid.appendChild(botonera);
		this.contPreGrid.appendChild(contPostBotonera);
		this.setWidth("100%");
		grilla.dibujar();
		this.appendChild(contPreGrid);
		this.appendChild(grilla);
	}
	
	public Row getUltimo(){
		return grilla.getUltimo();
	}
	
	public void actualizarEncabezados(){
		grilla.actualizarEncabezados();
	}
	
	@SuppressWarnings("unchecked")
	public Row setFila(ArrayList componentes, T modelo, DataBinder binder){
		return grilla.setFila(componentes, modelo, binder);
	}
	
	public Row setFila(){
		return grilla.setFila();
	}
	
	public void agregarBoton(Button nuevoBoton){
		botonera.appendChild(nuevoBoton);
	}
	
	public void agregar(T modelo){
		grilla.agregar(modelo);
	}

	@SuppressWarnings("unchecked")
	public void setComponentes(ArrayList componentes){
		grilla.setComponentes(componentes);
	}
	
	public void setModelo(T modelo){
		grilla.setModelo(modelo);
	}
	
	public void setBinder(DataBinder binder){
		grilla.setBinder(binder);
	}
	
	public void obtener(Rows fila){
		grilla.obtener(fila);
	}
	
	
	public void  crearMenu(List<Boolean> CRUD){
		grilla.crearMenu(CRUD);
	}	
	
	public void addEncabezadoAuxiliar(List<ColumnaAuxiliar> columnas) {
		grilla.addEncabezadoAuxiliar(columnas);
	}

	public Menupopup getMenu() {
		return grilla.getMenu();
	}

	@SuppressWarnings("unchecked")
	public void setNuevo(T nuevo){
		vacio = nuevo;
		grilla.setClase((Class<T>) vacio.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public T getNuevo(){
		T salida = null;
		try {
			salida = (T)  Class.forName(vacio.getClass().getName()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salida;
	}
	
	
	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == boton){
			grilla.agregar(getNuevo());			
		}
	}
	
	public void refrescarColeccion(){
		grilla.refrescarColeccion();
	}
	
	public void agregarRow() throws Exception {
		grilla.agregar(getNuevo());
	}
	
	public void setListenerBorrar(EventListener controlador){
		grilla.setListenerBorrar(controlador);
	}
	
	public void setDisabled(boolean valor) {
		boton.setDisabled(valor);
	}
	
	public Rows getFilas() {
		return grilla.getFilas();
	}
	
	public void setDisable(boolean desactivar ){
		setDisabled(desactivar);
		grilla.setDisable(desactivar);
	}
	
	public void clear(){
		grilla.clear();
	}
	
	public void setWidthGrid(int ancho){		
		this.grilla.setWidth(ancho+"px");
	}
	
	public void setHeightGrid(int alto){
		this.grilla.setHeight(alto+"px");
	}
	
	public void agregarComponente(boolean antesDelBoton, Component componente){
		if (antesDelBoton)
			this.contPreBotonera.appendChild(componente);
		else
			this.contPostBotonera.appendChild(componente);
		
	}

	public Button getBoton() {
		return boton;
	}

	public CompGrilla<T> getGrilla() {
		return grilla;
	}	
	
	public void setClase(Class<T> clase){
		grilla.setClase(clase);
	}
	
}

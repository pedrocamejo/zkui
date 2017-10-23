package cpc.zk.componente.ventanas;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Div;
import org.zkoss.zul.Row;

import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompGrillaConBoton;


public abstract class CompVentanaMaestroDetalle<T,E> extends CompVentanaBase<T> implements Serializable, EventListener{

	private static final long serialVersionUID = 8699251777046643713L;
	private CompGrupoDatos 			gbEncabezado;
	private CompGrupoDatos 			gbPie;	
	private Div		 				gbDetalle;
	private CompGrillaConBoton<E> 	detalle;
	private int						ancho;
	//private List<E> 				modeloDetalle;
	
	
	public CompVentanaMaestroDetalle(String titulo, int ancho){
		super(titulo, ancho);
		if (gbDetalle ==  null){ 
			gbDetalle = new Div();
		}
		gbDetalle.setWidth((ancho-30)+"px");
		this.ancho = ancho;
	}
	
	public CompVentanaMaestroDetalle(String titulo){
		super(titulo);
		if (gbDetalle ==  null) {
			gbDetalle = new Div();
		}
			
	}
	
	public void setWidth(int ancho){
		super.setWidth(ancho+"px");
		//gdDetalle.setWidth((ancho-30)+"px");
		if (gbEncabezado != null)
			gbEncabezado.setWidth((ancho-30)+"px");
		if (gbPie != null)
			gbPie.setWidth((ancho-30)+"px");
		this.ancho = ancho;
	}

	public void setWidth(String ancho){
		super.setWidth(ancho);
		int anchoi = Integer.parseInt(ancho.toLowerCase().substring(0, ancho.indexOf('p')));
		if (gbDetalle ==  null) 
			gbDetalle = new Div();
		gbDetalle.setWidth((anchoi-30)+"px");
		if (gbEncabezado != null)
			gbEncabezado.setWidth((anchoi-30)+"px");
		if (gbPie != null)
			gbPie.setWidth((anchoi-30)+"px");
		this.ancho = anchoi;
	}
	
	public CompGrupoDatos getGbEncabezado() {
		return gbEncabezado;
	}
	public void setGbEncabezado(CompGrupoDatos gbEncabezado) {
		this.gbEncabezado = gbEncabezado;
	}
	public CompGrupoDatos getGbPie() {
		return gbPie;
	}
	public void setGbPie(CompGrupoDatos gbPie) {
		this.gbPie = gbPie;
	}
	public CompGrillaConBoton<E> getDetalle() {
		return detalle;
	}
	
	public void clearDetalle() {
		detalle.clear();
	}
	
	public void setDetalle(CompGrillaConBoton<E> detalle) {
		this.detalle = detalle;
	}
	
	public void addGbEncabezado(Component componente) {
		this.gbEncabezado.addComponente(componente);
	}
	
	public void addGbEncabezado(String titulo, Component componente) {
		this.gbEncabezado.addComponente(titulo,componente);
	}
	
	public void addGbPie(Component componente) {
		this.gbPie.addComponente(componente);
	}
	
	public void addGbPie(String titulo, Component componente) {
		this.gbPie.addComponente(titulo, componente);
	}
	
	public void dibujarEstructura(){
		if (gbEncabezado != null)
			gbEncabezado.dibujar(this);
		detalle.dibujar();
		gbDetalle.appendChild(detalle);
		detalle.setWidthGrid(this.ancho-40);
		this.appendChild(gbDetalle);

		if (gbPie != null)
			gbPie.dibujar(this);
		addBotonera();
	}
	
	public void dibujarEstructura(Component enCualComponente){
		if (gbEncabezado != null)
			gbEncabezado.dibujar(enCualComponente);
		this.appendChild(detalle);
		detalle.dibujar();
		
		gbDetalle.appendChild(detalle);
		enCualComponente.appendChild(gbDetalle);
		
		if (gbPie != null)
			gbPie.dibujar(enCualComponente);
		addBotonera();
	}

	public void setGbEncabezado(int columnas) {
		gbEncabezado = new CompGrupoDatos(columnas);
		gbEncabezado.setWidth((ancho-30)+"px");
	}
	
	public void setGbEncabezado(int columnas, String titulo) {
		gbEncabezado = new CompGrupoDatos(titulo, columnas);
		gbEncabezado.setWidth((ancho-30)+"px");
	}
	
	public void setGbPie(int columnas) {
		gbPie = new CompGrupoDatos(columnas);
		gbPie.setWidth((ancho-30)+"px");
	}
	
	public void setGbPie(int columnas, String titulo) {
		gbPie = new CompGrupoDatos(titulo, columnas);
		gbPie.setWidth((ancho-30)+"px");
	}	
	
	
	public void setDetalles(ArrayList<Component> componentes, List<E> coleccion, List<CompEncabezado>  encabezados){
		detalle.setComponentes(componentes);
		detalle.setEncabezados(encabezados);
		if (coleccion==null)
			coleccion = new ArrayList<E>();
		detalle.setColeccion(coleccion);
		detalle.cargar();
	}
		
	public void cargarValores(List<E> coleccion) throws ExcDatosInvalidos {
		detalle.setColeccion(coleccion);
	}
	
	@SuppressWarnings("unused")
	private void setEncabezado(List<CompEncabezado>  encabezados){
		detalle.setEncabezados(encabezados);
	}
	
	@SuppressWarnings("unused")
	private void setComponentes(ArrayList<Component> componentes){
		detalle.setComponentes(componentes);
	}
		
	protected void init(){
		super.init();
		opciones = new Label("(Enter o F4) Aceptar, (Esc) Cancelar, (F2) Agregar detalle ");
		detalle = new CompGrillaConBoton<E>(getControlador());
		this.setCtrlKeys("^a#f2#f4");
		this.addEventListener(Events.ON_CTRL_KEY, this);
	}
	
	public void cargar() throws ExcDatosInvalidos{
		super.cargar();
	}
	
	public void actualizarModelo(){
		getBinder().saveAll();
		getBinder().loadAll();
		detalle.refrescar();
	}
	
	public void close(){
		try {
			getBinder().saveAll();
			this.setMode("popup");
			this.onClose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Div getGbDetalle() {
		return gbDetalle;
	}

	public void setGbDetalle(String titulo) {
		
	}

	@SuppressWarnings("unchecked")
	public List<E> getModeloDetalle() throws ExcAgregacionInvalida {
		try{
			List<E> detalles = new ArrayList<E>();
			actualizarModelo();
			List<Row> filas = detalle.getFilas().getChildren();
			E detalle;
			for (Row item : filas) {
				detalle = (E) item.getAttribute("dato");
				detalles.add(detalle);
			}
			return detalles;
		}catch (Exception e) {
			e.printStackTrace();
			throw new ExcAgregacionInvalida("Problema Actualizado equipos, "+e.getMessage()); 
		}
	}
	public void activarDetalle(){
		getDetalle().setDisable(false);
	}
	public void desactivarDetalle(){
		getDetalle().setDisable(true);
	}
	
	public void onEvent(Event event) throws Exception {
		if (event.getName() == Events.ON_OK)
			new Event(Events.ON_CLICK, getAceptar());
		else if (event.getName() == Events.ON_CANCEL)
			close();
		if (event.getName() == Events.ON_CTRL_KEY){
			System.out.println(((KeyEvent)event).getKeyCode());
			if (((KeyEvent)event).getKeyCode() == 113)
				detalle.agregarRow();
			if (((KeyEvent)event).getKeyCode() == 115)
				new Event(Events.ON_CLICK, getAceptar());
		}
		else if (event.getTarget()==cancelar)
			close();
	}
	
	public void setAltoDetalle(int alto){
		this.detalle.setHeightGrid(alto);
	}
	
	public List<E> getColeccionGrilla(){
		detalle.refrescarColeccion();
		return detalle.getColeccion();
	}

	
	public void agregarCompAntesBotonAgregar(Component componente){
		this.detalle.agregarComponente(true, componente);
	}
	
	public void agregarCompDespuesBotonAgregar(Component componente){
		this.detalle.agregarComponente(false, componente);
	}
}

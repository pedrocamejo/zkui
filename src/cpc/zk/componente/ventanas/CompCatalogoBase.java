package cpc.zk.componente.ventanas;

import java.io.Serializable;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbar;

import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcPosicionNoValida;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.listas.CompCatalogo;
import cpc.zk.componente.menus.CompBarraHerramientas;



public class CompCatalogoBase<T> extends Panel implements EventListener, Serializable{
	
private static final long serialVersionUID = -7410904635392247012L;
	
	
	private Combobox 				cmbBuscar;
	private Combobox 				txtValor;
	private Button	 				btnBuscar;	
	private Toolbar	 				busqueda;
	private CompCatalogo<T>			tablaDatos;
	private EventListener 			controlador;
	private CompBarraHerramientas 	rowAcciones;
	
	
	
	public CompCatalogoBase(AccionFuncionalidad seguridad, String nombre, List<CompEncabezado> encabezado, EventListener controlador){
		super();
		this.controlador = controlador;
		this.setTitle(nombre);
		this.inicializar(seguridad, encabezado);
		dibujar();
	}
	
	@SuppressWarnings("unchecked")
	public CompCatalogoBase(AccionFuncionalidad seguridad, String nombre, List<CompEncabezado> encabezado, List<List> filas, List objetos, EventListener controlador) throws ExcColumnasInvalidas{
		super();
		this.controlador = controlador;
		this.setTitle(nombre);
		this.inicializar(seguridad, encabezado);
		if (filas == null);
		tablaDatos.cargarDatos(filas, objetos);
		dibujar();
	}
	
	public void dibujar(){
		this.setStyle("border:1px solid #a7a7a7;  width:100%; ");
		this.appendChild(rowAcciones);
		Panelchildren panel = new Panelchildren();
		panel.appendChild(tablaDatos);
	    panel.setClass("panel_tabla");
		this.appendChild(panel);
		this.appendChild(busqueda);
	}
	
	public void getMaximoPagina(long total){ 
	}
	
	public void getTamanoPagina(){ 

	}
	
	public void addMenuCRUD(EventListener controlador){
		tablaDatos.addMenuCrud(controlador);
	}
	
	private void inicializar(AccionFuncionalidad seguridad, List<CompEncabezado> encabezado){		
		cmbBuscar   			= new Combobox();
		txtValor    			= new Combobox();		
		tablaDatos				= new CompCatalogo<T>(encabezado);
		btnBuscar   			= new Button("Buscar"); 
		rowAcciones 	        = new CompBarraHerramientas(seguridad, 0, controlador); 
		busqueda				= new Toolbar();
		
		txtValor.setAutocomplete(true);
		txtValor.setAutodrop(true);
		rowAcciones.setMold("panel");
		busqueda.setMold("panel");
		busqueda.appendChild(cmbBuscar);
		busqueda.appendChild(txtValor);
		busqueda.appendChild(btnBuscar);
		
		cmbBuscar.setModel(new SimpleListModel(encabezado.toArray()));
		
		cmbBuscar.addEventListener("onSelect", this);
		btnBuscar.addEventListener("onClick", this);
		cmbBuscar.setReadonly(true);
	}
	
	
	public String obtenerIdRegistro(int id) throws NullPointerException{
		return tablaDatos.obtenerSeleccion(id);
	}

	public int obtenerIndiceSeleccionado(){
		return tablaDatos.getSelectedIndex();
	}
	
	public Object obtenerSeleccion(){	
		return tablaDatos.obtenerObjeto();
	}
	
	@SuppressWarnings("unchecked")
	public void refrescarAgrega(List item,  Object objeto){
		tablaDatos.agregar(item, objeto);
	}
	
	@SuppressWarnings("unchecked")
	public void refrescarModifica(List item, int fila, Object objeto){
		tablaDatos.actualizar(fila, item, objeto);
	}
	
	public void refrescarElimina(int fila){
		tablaDatos.eliminarItem(fila);
	}


	public void limpiarTablaDatos(){		
	}
	
	

	public String getTxtValor() {
		return txtValor.getText();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void  cargarValoresTabla(List<List> filas, List objetos) throws ExcColumnasInvalidas{
		tablaDatos.cargarDatos(filas, objetos);
		
	}


	public void onEvent(Event event) throws Exception{
		if (event.getName() == Events.ON_CLICK){
			if (event.getTarget() == btnBuscar){
				filtrar();
			}
		}
		if (event.getName() == Events.ON_SELECT){
			if (event.getTarget() == cmbBuscar){  
				nuevaLista();
			}
		}
		
	}
	
	public void filtrar() throws ExcPosicionNoValida{ 
		try {
			tablaDatos.refrescar(cmbBuscar.getSelectedIndex(), txtValor.getValue().trim());
		}catch (IndexOutOfBoundsException e) {
		 try {
			Messagebox.show("Seleccione un Criterio de Busqueda","Error en Seleccion",Messagebox.OK,Messagebox.ERROR);
		} catch (InterruptedException e1) {			
			e1.printStackTrace();
		}
		}
	}
	
	public void nuevaLista(){
		txtValor.setModel(new SimpleListModel(tablaDatos.getColumnas(cmbBuscar.getSelectedIndex()).toArray()));
	}

	@SuppressWarnings("unchecked")
	public void refresh(List item, Object objeto, int modo, int index){
		if (modo == Accion.AGREGAR)
			refrescarAgrega(item, objeto);
		else if (modo == Accion.EDITAR)
			refrescarModifica(item, index, objeto);
		else if (modo == Accion.ELIMINAR)
			refrescarElimina(index);
	}

	@SuppressWarnings("unchecked")
	public CompCatalogo getTablaDatos() {
		return tablaDatos;
	}

	public void setModelo(List<T> modelo) {
		tablaDatos.setModelo(modelo);
	}
	
	public void agregarDato(T objeto){
		tablaDatos.agregar(objeto);
	}
	
	public void editarDato( T objeto){
		tablaDatos.editar(objeto);
	}
	
	public void eliminarDato(){
		tablaDatos.eliminarItem();
	}
	
	public void desactivarTipoReporte(){
		rowAcciones.desactivarTipoReporte();
	}
	
	public String getTipoReporte(){
		return (String) rowAcciones.getTipoReporte().getSelectedItem().getValue();
	}
	
	public void setTipoReporte(int i){
		rowAcciones.getTipoReporte().setSelectedIndex(i);
	}
}

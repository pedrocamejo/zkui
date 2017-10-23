package cpc.zk.componente.controlador;

import java.io.Serializable;
import java.util.List;

import org.zkoss.zk.ui.event.EventListener;

import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompCatalogoBase;
import cpc.zk.componente.ventanas.CompVentanaBase;


public abstract class  ContCatalogo<T> implements EventListener, Serializable, ICompCatalogo<T>{
	
	/**   
	 * 
	 */
	private static final long serialVersionUID = -4821835568508419865L;

	private int modoOperacion;

	@SuppressWarnings("unused")
	private AccionFuncionalidad accionesValidas;
	
	private CompCatalogoBase<T>	interfaz;
	
	private Object 					dato;
	private List<T> 			datos;
	private int 				idFuncionalidad;
	private int 				idposi;
	
	
	
	public ContCatalogo(int modoOperacion) {
		super();
		this.modoOperacion = modoOperacion;
	}

	public ContCatalogo() {
		super();		
	}
	
		
	public int getIdFuncionalidad() {
		return idFuncionalidad;
	}

	public void setIdFuncionalidad(int idFuncionalidad) {
		this.idFuncionalidad = idFuncionalidad;
	}
	
	@SuppressWarnings("unchecked")
	public void actualizar(List items){
		interfaz.refresh(items, dato, modoOperacion, idposi);
	}

	public abstract List<CompEncabezado> cargarEncabezado();
	
	@SuppressWarnings("unchecked")
	public abstract List cargarDato(T dato);
	
	@SuppressWarnings("unchecked")
	protected abstract List<List> cargarDatos() throws ExcArgumentoInvalido;
	
	public CompCatalogoBase<T> getInterfaz() {
		return interfaz;
	}

	public void setInterfaz(CompCatalogoBase<T> interfaz) {
		this.interfaz = interfaz;
	}

	public Object getDato() {
		return dato;
	}

	@SuppressWarnings("unchecked")
	public T getDatoSeleccionado() {		
		return (T) interfaz.obtenerSeleccion();
	}
	
	public void setDato(T dato) {
		this.dato = dato;
	}

	@SuppressWarnings("unchecked")
	public List getDatos() {
		return datos;
	}

	public void setDatos(List<T> datos) {
		this.datos = datos;
	}

	public int getIdposi() {
		idposi = interfaz.obtenerIndiceSeleccionado();
		return idposi;
	}

	public void setIdposi(int idposi) {
		interfaz.getTablaDatos().setSelectedIndex(idposi);
		getIdposi();
	}
	
	
	public void dibujar(AccionFuncionalidad accionesValidas, String titulo, IZkAplicacion app) throws ExcColumnasInvalidas, ExcArgumentoInvalido{
		this.accionesValidas = accionesValidas;
		setInterfaz(new CompCatalogoBase<T>(accionesValidas,titulo, cargarEncabezado(), cargarDatos(), getDatos(),this));		
		app.agregarAEscritorio(getInterfaz());
	}
	
	
	public void dibujar(AccionFuncionalidad accionesValidas, String titulo, List<T> modelos, IZkAplicacion app) throws ExcColumnasInvalidas, ExcArgumentoInvalido{
		this.accionesValidas = accionesValidas;
		setInterfaz(new CompCatalogoBase<T>(accionesValidas,titulo, cargarEncabezado(),this));
		getInterfaz().setModelo(modelos);
		setDatos(modelos);
		app.agregarAEscritorio(getInterfaz());
	}	
	
	
	
	
	public int getModoOperacion() {
		return modoOperacion;
	}

	public void setModoOperacion(int modoOperacion) {
		this.modoOperacion = modoOperacion;
	}

	public void desactivarTipoReporte(){
		interfaz.desactivarTipoReporte();
	}
	
	public String getTipoReporte(){
		return interfaz.getTipoReporte();
	}
	
	public void editarDato(Object dato){
		getInterfaz().editarDato( (T) dato);
	}
	
	@SuppressWarnings("unchecked")
	public void agregarDato(Object dato){
		getInterfaz().agregarDato( (T) dato);	
	}
	public void eliminarDato(){
		getInterfaz().eliminarDato();
	}
	
	public void setIdposicionReporte(int posicion){
		getInterfaz().setTipoReporte(posicion);
	}
	
	public Integer getIdposicion(){
		idposi = interfaz.obtenerIndiceSeleccionado();
		return idposi;
	}
	
	public void cargarVentanaModal(IZkAplicacion aplicacion, CompVentanaBase<T> ventana) {
		aplicacion.agregar(ventana);
	}
	
}
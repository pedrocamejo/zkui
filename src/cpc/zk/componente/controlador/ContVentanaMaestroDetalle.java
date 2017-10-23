package cpc.zk.componente.controlador;

import java.io.Serializable;

import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cpc.ares.modelo.Accion;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.controlador.ContControlGeneral;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;

public abstract class ContVentanaMaestroDetalle<T, E> extends ContControlGeneral implements EventListener, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5930234507661201376L;
	private T 								dato;
	private ContCatalogo<T>  				llamador;
	private CompVentanaMaestroDetalle<T, E> vista;
	private IZkAplicacion appa;  
	public ContVentanaMaestroDetalle(int i) {
		super(i);
	}

	
	public void setear(T dato, CompVentanaMaestroDetalle<T, E> vista, ContCatalogo<T> llamador, IZkAplicacion app) throws ExcDatosInvalidos{
		try {
			this.llamador = llamador;
			this.dato = dato;
			this.vista = vista;
			this.appa = app;
			this.vista.setModelo(this.dato);
			this.vista.setControlador(this);
			this.vista.dibujarVentana();
			this.vista.cargar();			
			app.agregar(this.vista);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcDatosInvalidos(e.getMessage());
		}
		
	}

	public void procesarCRUD(Event event) throws WrongValuesException, ExcEntradaInconsistente, ExcFiltroExcepcion {
		if (event.getName() == Events.ON_CLICK){
			IZkAplicacion appaaaa = this.appa; 
	
			IZkAplicacion a = (IZkAplicacion) TransactionSynchronizationManager.getResource("obj");
			//TransactionSynchronizationManager.isSynchronizationActive();
			boolean b = TransactionSynchronizationManager.hasResource("obj");
			boolean igual = appaaaa.equals(a);
			if (!igual){
				if (b){
					TransactionSynchronizationManager.unbindResource("obj");
				}
					
			TransactionSynchronizationManager.bindResource("obj",appaaaa);
			}
			
		
			if (event.getTarget() == vista.getAceptar()){
				procesarCRUD();
			}else if(event.getTarget() == vista.getCancelar()){
				vista.close();
			}
		}
	}
	
	@SuppressWarnings("static-access") 
	public void procesarCRUD() throws WrongValuesException, ExcEntradaInconsistente, ExcFiltroExcepcion {
		if (modoAgregar() || modoEditar()){
			vista.actualizarModelo();
			validar();
			guardar();
		}else if(modoEliminar()){
			try{
				if (new Messagebox().show("Esta seguro de eliminar este dato", "Confirmacion", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK){
					eliminar();
				}
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}else if(modoAnular()){
			anular();}
		else if(modoCorregir()){
			correjir();}
		else if(modoProcesar()){
			procesar();}	
		try{
			refrescarCatalogo();
			if (modoCRUD()){
				vista.close();
			}
		}catch(NullPointerException e){
			
		}
		
	}

	public void refrescarCatalogo(){
		int fila = llamador.getIdposi();
		if (!modoEliminar()){
			if (fila != -1){ 
				llamador.getInterfaz().editarDato(dato);
			}else
				llamador.getInterfaz().agregarDato(dato);
		}else
			llamador.getInterfaz().eliminarDato();
	}
	
	@SuppressWarnings("unchecked")
	public boolean datoNulo(T dato) throws ExcSeleccionNoValida{
		if (dato == null){
			if (!modoAgregar())
				throw new ExcSeleccionNoValida("No selecciono ningun campo");
			return true;
		}else{
			if (modoAgregar()){
				try {
					dato = (T)  Class.forName(dato.getClass().getName()).newInstance();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	
	public T getDato() {
		return dato;
	}

	public void setDato(T dato) {
		this.dato = dato;
	}

	public ContCatalogo<T> getLlamador() {
		return llamador;
	}

	public void setLlamador(ContCatalogo<T> llamador) {
		this.llamador = llamador;
	}

	public CompVentanaBase<T> getVista() {
		return vista;
	}

	public void setVista(CompVentanaMaestroDetalle<T, E> vista) {
		this.vista = vista;
	}
	
	public Boolean getModoAgregar(){
		return getModoOperacion() == Accion.AGREGAR;
	}
	
	public abstract void guardar()  throws ExcFiltroExcepcion ;
	public abstract void eliminar()   throws ExcFiltroExcepcion ;
	public abstract void validar() throws WrongValuesException, ExcEntradaInconsistente;
	public abstract void anular()  throws ExcFiltroExcepcion ;
	public abstract void correjir()  throws ExcFiltroExcepcion ;
	public abstract void procesar() throws WrongValuesException, ExcEntradaInconsistente;
	
}

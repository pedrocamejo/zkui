package cpc.zk.componente.listas;

import java.io.Serializable;
import java.sql.SQLException;

import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcEntradaInvalida;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cpc.zk.componente.controlador.ContControlGeneral;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompVentanaBase;

public abstract class ContVentanaBase<T> extends ContControlGeneral implements EventListener, Serializable{
	
	private static final long serialVersionUID = -7510046357995394260L;
	private T 					dato;
	private ICompCatalogo<T>  	llamador;
	private CompVentanaBase<T> 	vista;	
	private IZkAplicacion appa;    
	//private SessionDao session;  
	public ContVentanaBase(int i) {
		super(i);
	}
	
	public void setear(T dato, CompVentanaBase<T> vista, ICompCatalogo<T> llamador, IZkAplicacion app) throws ExcDatosInvalidos{
		try {
			this.llamador = llamador;
			this.dato = dato;
			this.vista = vista;
			this.appa = app;
			this.vista.setMode("modal");
			this.vista.setModelo(this.dato);
			this.vista.setControlador(this);
			this.vista.dibujarVentana();
			this.vista.cargar();
			llamador.cargarVentanaModal(app, vista);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcDatosInvalidos(e.getMessage());
		}
	}
	
	public void procesarCRUD(Event event) throws ExcEntradaInvalida, ExcEntradaInconsistente, SQLException, ExcArgumentoInvalido, WrongValuesException, ExcFiltroExcepcion{
		if (event.getName() == Events.ON_CLICK){
			ICompCatalogo<T> az = getLlamador();
			IZkAplicacion appaaaa = this.appa; 
			IZkAplicacion a = (IZkAplicacion) TransactionSynchronizationManager.getResource("obj");
			boolean b = TransactionSynchronizationManager.hasResource("obj");
		boolean igual = appaaaa.equals(a);
			if (!igual){
				if (b){TransactionSynchronizationManager.unbindResource("obj");}
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
	public void procesarCRUD()  throws ExcFiltroExcepcion, WrongValuesException, ExcEntradaInconsistente {
		if (modoAgregar() || modoEditar()){
			vista.actualizarModelo();
			validar();
			guardar();
		}
		else if(modoEliminar())
		{
			try{
				if (new Messagebox().show("Esta seguro de eliminar este dato", "Confirmacion", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK){
					eliminar();
				}
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		else if(modoAnular()) {
			anular();
		}
		else if(modoCorregir()){
			correjir();
		}
		else if(modoProcesar()){
			procesar();
		}
		try{
			refrescarCatalogo();
			if (modoCRUD()){
				vista.close();
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void refrescarCatalogo(){
		try{
			System.out.println(llamador);
			Integer fila = llamador.getIdposicion();
			if (!modoEliminar()){
				if (modoAgregar()){
					llamador.agregarDato(dato);
				}else{
					if (fila != null)
						if (fila != -1 && !modoAgregar()){ 
							llamador.editarDato(dato);
						}
				}
			}
			else
				llamador.eliminarDato();
		}catch (Exception e) {
			e.printStackTrace();
		}
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

	public ICompCatalogo<T> getLlamador() {
		return llamador;
	}


	public CompVentanaBase<T> getVista() {
		return vista;
	}

	public IZkAplicacion getAppa() {
		return appa;
	}

	public void setAppa(IZkAplicacion appa) {
		this.appa = appa;
	}

	public void setVista(CompVentanaBase<T> vista) {
		this.vista = vista;
	}
	
	public abstract void guardar()  throws ExcFiltroExcepcion ;
	public abstract void eliminar()  throws ExcFiltroExcepcion ;
	public abstract void validar() throws WrongValuesException, ExcEntradaInconsistente;
	public abstract void anular()  throws ExcFiltroExcepcion ;
	public abstract void correjir()  throws ExcFiltroExcepcion ;
	public abstract void procesar() throws WrongValuesException, ExcEntradaInconsistente;

	
}

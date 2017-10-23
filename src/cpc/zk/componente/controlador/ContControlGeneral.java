package cpc.zk.componente.controlador;

import java.io.Serializable;

import cpc.ares.modelo.Accion;

public abstract class ContControlGeneral implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5236526582251114949L;
	private int modoOperacion;
	
	public ContControlGeneral(int i){
		modoOperacion = i;
	}
	
	public int getModoOperacion() {
		return modoOperacion;
	}

	public void setModoOperacion(int modoOperacion) {
		this.modoOperacion = modoOperacion;
	}
	
	public boolean modoAgregar(){
		return (modoOperacion == Accion.AGREGAR);
	}
	
	public boolean modoEditar(){
		return (modoOperacion == Accion.EDITAR);
	}
	
	public boolean modoEliminar(){
		return (modoOperacion == Accion.ELIMINAR);
	}
	
	public boolean modoConsulta(){
		return (modoOperacion == Accion.CONSULTAR);
	}
	
	public boolean modoCRUD(){
		return (modoOperacion <= Accion.CONSULTAR);
	}
	
	public boolean modoImprimirItem(){
		return (modoOperacion == Accion.IMPRIMIR_ITEM);
	}
	
	public boolean modoImprimirTodo(){
		return (modoOperacion == Accion.IMPRIMIR_TODO);
	}
	
	public boolean modoProcesar(){
		return (modoOperacion == Accion.PROCESAR);
	}
	
	public boolean modoAsociar(){
		return (modoOperacion == Accion.ASOCIAR);
	}
	
	public boolean modoCorregir(){
		return (modoOperacion == Accion.CORREGIR);
	}
	
	public boolean modoAnular(){
		return (modoOperacion == Accion.ANULAR);
	}

}

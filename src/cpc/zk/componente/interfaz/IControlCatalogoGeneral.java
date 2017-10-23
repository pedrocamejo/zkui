package cpc.zk.componente.interfaz;

import java.io.Serializable;
import java.util.List;

import org.zkoss.zk.ui.event.EventListener;

import cpc.zk.componente.ventanas.CompCatalogoBase;




//******Utilizando tipo generico T
public interface IControlCatalogoGeneral<T> extends EventListener, Serializable{
	public List<T> getListado();
	public void setListado(List<T> listado) ;
	public CompCatalogoBase<T> getInterfazServicio();
	public void setInterfazServicio(CompCatalogoBase<T> interfazServicio);
	@SuppressWarnings("unchecked")
	public void actualizar(List items);
	@SuppressWarnings("unchecked")
	public List cargarDato(T dato);
}

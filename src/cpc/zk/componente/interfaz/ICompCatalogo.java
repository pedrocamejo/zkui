package cpc.zk.componente.interfaz;

import cpc.zk.componente.ventanas.CompVentanaBase;


public interface ICompCatalogo<T> {
	public void editarDato(Object dato);
	public void agregarDato(Object dato);
	public void eliminarDato();
	public Integer getIdposicion();
	public void cargarVentanaModal(IZkAplicacion aplicacion, CompVentanaBase<T> ventana);
}

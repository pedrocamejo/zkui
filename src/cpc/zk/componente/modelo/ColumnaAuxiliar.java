package cpc.zk.componente.modelo;

import cva.pc.componentes.CompEncabezado;

public class ColumnaAuxiliar {
	private String 	descripcion;
	private int 	columnas;
	private int 	alineacion = CompEncabezado.IZQUIERDA;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getColumnas() {
		return columnas;
	}
	public void setColumnas(int columnas) {
		this.columnas = columnas;
	}
	public int getAlineacion() {
		return alineacion;
	}
	
	public String getStrAlineacion() {
		return CompEncabezado.ALINEACION[alineacion];
	}
	
	public void setAlineacion(int alineacion) {
		this.alineacion = alineacion;
	}
	
	
	
}

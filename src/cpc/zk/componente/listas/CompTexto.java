package cpc.zk.componente.listas;

import org.zkoss.zul.Label;

public class CompTexto<T> extends Label{

	private static final long serialVersionUID = 6148890423817879495L;
	
	private T 		modelo;

	public CompTexto(){
		super();
	}
	
	public CompTexto(T modelo){
		super();
		this.modelo = modelo;
		setValue(modelo.toString());
	}
	
	public T getModelo() {
		return modelo;
	}

	public void setModelo(T modelo) {
		this.modelo = modelo;
		setValue(modelo.toString());
	}
	
	
	
}

package cpc.zk.componente.menus;

import java.io.Serializable;

import org.zkoss.zk.ui.Component;


public class CompFilaObjetos implements Cloneable, Serializable{	
	private static final long serialVersionUID = -2364704536976081027L;
	private Object valor[];
	private int ultimo=0;  
	private int tamano;
		
	
	public CompFilaObjetos(int tamano) {
		super();
		this.valor = new Object[tamano];
		this.tamano = tamano;
		ultimo = 0;
		/*for (int i=0;i<tamano;i++)
			valor[i]="a";*/
	}
	
	public CompFilaObjetos(Object valor[]) {
		super();
		this.valor = valor;
		this.tamano = valor.length;
		ultimo = this.tamano;
	}

	public Object getValor(int pos) {
		return (Object) valor[pos];
	}

	public void setValor(Object[] valor) {
		this.valor[ultimo] = valor;
	}
	
	public void setValor(int i, Object valor) {
		this.valor[i] = valor;
	}
	
	
	public Object getvalorDetalle(int indice){
		return valor[indice];
	}
	
	/*public void agregarColumna(Object valorCol){
		if (ultimo <tamano){
			try{				
				valor[ultimo++] = valorCol;
			}catch (IndexOutOfBoundsException e){
				throw e;
			}
			
		}else
			System.out.println("Posicion no valida");
	}*/

	public int getTamano() {
		return tamano;
	}

	public void agregarColumna(Object setText) {
		if (ultimo <tamano){
			try{				
				valor[ultimo++] = setText;
			}catch (IndexOutOfBoundsException e){
				throw e;
			}
			
		}else
			System.out.println("Posicion no valida");
		
	}


	public void desactivar(){
		for(int i=0;i<valor.length;i++ ){
			if (valor[i] instanceof Component)
				System.out.println("desactivar componentes");
			//( (Component) valor[i]).setEnabled(false);
		}
	}
	
}

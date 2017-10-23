package cpc.zk.componente.listas;


import java.io.Serializable;
import org.zkoss.zul.Caption;



public class CompEtiqueta<T> extends Caption implements Serializable{

	private static final long serialVersionUID = 5482883940899582719L;
	
	
	public void setValor(T valor){
		this.setAttribute("valor", valor);
		this.setLabel(valor.toString());
	}
		

	@SuppressWarnings("unchecked")
	public T getValor(){
		return (T) getAttribute("valor");
	}

	
}   
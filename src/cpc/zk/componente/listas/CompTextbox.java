package cpc.zk.componente.listas;


import java.io.Serializable;
import org.zkoss.zul.Textbox;


public class CompTextbox<T> extends Textbox implements Serializable{

	private static final long serialVersionUID = 5482883940899582719L;
	public void setValor(T valor){
		this.setAttribute("valor", valor);
		this.setValue(valor.toString());
	}
		

	@SuppressWarnings("unchecked")
	public T getValor(){
		return (T) getAttribute("valor");
	}
}   
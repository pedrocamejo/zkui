package cpc.zk.componente.listas;


import java.io.Serializable;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import cpc.zk.componente.modelo.ListaOrdenada;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class CompCombobox<T> extends Combobox implements Serializable{

	private static final long serialVersionUID = 5482883940899582719L;
	private T	seleccion;
		
	
	public void setModelo(List<T> modelo){
		if (modelo != null){
			seleccion = null;
	  		Comboitem item;
	  		this.setValue("");
	  		this.getChildren().clear();
	  		ListaOrdenada.ordena(modelo, "toString");
			for (T dato : modelo) {
				item = new Comboitem();
				item.setLabel(dato.toString());
				item.setAttribute("dato", dato);
				this.appendChild(item);
			}
		}
	}
	
	public void setModelo(List<T> modelo, String descripcion){
		if (modelo != null){
			seleccion = null;
	  		Comboitem item = null;
	  		this.setValue("");
	  		this.getChildren().clear();
	  		ListaOrdenada.ordena(modelo, "toString");
			for (T dato : modelo) {
				item = new Comboitem();
				item.setLabel(dato.toString());
				item.setAttribute("dato", dato);
				// Get the Type for the class
				if (modelo!= null) {
					try {
						Class<T> clase = (Class<T>) dato.getClass();
						Method metodoEjecutableModelo;
						metodoEjecutableModelo = clase.getMethod(descripcion, null);
						Object parametro = metodoEjecutableModelo.invoke(dato, null);
						System.out.println("LA CLASE ES: " + clase);
						System.out.println("METODO: " + metodoEjecutableModelo);
						System.out.println("VALOR: " + parametro.toString());
						if (parametro != null){
								item.setDescription(parametro.toString().trim());
						}
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				this.appendChild(item);
			}
		}
	}
		
	@SuppressWarnings("unchecked")
	public T getSeleccion(){
		try{
			Comboitem item = this.getSelectedItem();
			if (item != null)
				seleccion =  (T) item.getAttribute("dato");
			else
				seleccion = null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return seleccion;
	}
		
	@SuppressWarnings("unchecked")
	public void setSeleccion(Object objeto){
		try{
			if (objeto != null){
				seleccion = (T) objeto;
				Comboitem item;
				for (int i=0; i< this.getItems().size();i++) {
					item = (Comboitem) getItems().get(i);
					if (((T)item.getAttribute("dato")).toString().equals(seleccion.toString())){
						setSelectedItem(item);
						break;
					}
				}
			}else{
				seleccion = null;
				this.setSelectedIndex(-1);
				this.setValue("");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setIndiceSeleccionado(Integer indice){
		this.setSelectedIndex(indice.intValue());
	}
	
	public Integer getIndiceSeleccionado(){
		return this.getSelectedIndex();
	}
	
}   
package cpc.zk.componente.listas;

import java.io.Serializable;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;


public class CompListaSimple extends Listbox implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4866287503844610646L;
	
	private Listhead 	encabezado 			= new Listhead();
	private Listheader 	datorListheader 	= new Listheader();
	private Object 		modeloSelect = null;;
	
	private Listitem items;
	
	public CompListaSimple(String etiqueta){
		datorListheader.setSort("auto");
		datorListheader.setLabel(etiqueta);
		encabezado.appendChild(datorListheader);
		this.appendChild(encabezado);
	}
	
	@SuppressWarnings("unchecked")
	public void setModelo(List modelo){
		for (Object dato : modelo) {
			items = new Listitem();
			items.setLabel(dato.toString());
			items.setAttribute("dato", dato);
			this.appendChild(items);
		}
	}

	public Object getModeloSelect(){
		for (Object  celda : getSelectedItems()) {
			modeloSelect = (((Listitem) celda).getAttribute("dato"));
		}
		return modeloSelect;
	}
	
	public void setModeloSelect(Object objeto){
		modeloSelect = objeto;
		if (objeto != null){
			for (int i=0; i< getItems().size();i++) {
				if (objeto.toString().equals(getItemAtIndexApi(i).getAttribute("dato").toString())){
					addItemToSelection(getItemAtIndex(i));
					break;
				}

			}
		}
	}
	
}

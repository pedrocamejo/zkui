package cpc.zk.componente.listas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;


public class CompListaMultiple extends Listbox implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4866287503844610646L;
	
	private Listhead 	encabezado 			= new Listhead();
	private Listheader 	datorListheader 	= new Listheader();
	@SuppressWarnings("unchecked")
	private List 		modeloSelect;
	
	
	Listitem items;
	
	public CompListaMultiple(String etiqueta){
		this.setFixedLayout(true);
		this.setMultiple(true);
		this.setCheckmark(true);
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

	@SuppressWarnings("unchecked")
	public List getModeloSelect(){
		modeloSelect = new ArrayList();
		for (Object  celda : getSelectedItems()) {
			System.out.println(((Listitem) celda).getAttribute("dato"));
			modeloSelect.add(((Listitem) celda).getAttribute("dato"));
		}
		return modeloSelect;
	}
	
	@SuppressWarnings("unchecked")
	public void setModeloSelect(ArrayList objetos){
		modeloSelect = objetos;
		if (objetos != null)
			for (int i=0; i< getItems().size();i++) {
				for (int j=0; j< objetos.size(); j++) {
					if (objetos.get(j).toString().equals(getItemAtIndexApi(i).getAttribute("dato").toString())){
						addItemToSelection(getItemAtIndex(i));
						break;
					}
				}
			}
	}

	@SuppressWarnings("unchecked")
	public void setModeloSelect(List objetos){
		modeloSelect = objetos;
		if (objetos != null)
			for (int i=0; i< getItems().size();i++) {
				for (int j=0; j< objetos.size(); j++) {
					if (objetos.get(j).toString().equals(getItemAtIndexApi(i).getAttribute("dato").toString())){
						addItemToSelection(getItemAtIndex(i));
						break;
					}
				}
			}
	}
	
	@SuppressWarnings("unchecked")
	public void setModeloSelect(Object objetos){
		modeloSelect = (List) objetos;
		if (objetos != null)
			for (int i=0; i< getItems().size();i++) {
				for (int j=0; j< modeloSelect.size(); j++) {
					if (modeloSelect.get(j).toString().equals(getItemAtIndexApi(i).getAttribute("dato").toString())){
						addItemToSelection(getItemAtIndex(i));
						break;
					}
				}
			}
	}

	
}

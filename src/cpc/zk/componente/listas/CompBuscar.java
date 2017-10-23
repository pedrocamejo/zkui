package cpc.zk.componente.listas;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Vbox;

import cva.pc.componentes.CompEncabezado;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;



public class CompBuscar<T> extends Bandbox implements EventListener, Cloneable{

	public static final String ON_SELECCIONO = "onEncontrar";
	
	private static final long serialVersionUID = 6811363633153471377L;
	private Bandpopup  				popup;
	private T  						seleccion;
	private CompCatalogo<T>			catalogo;
	private int						filaCatalogo;
	private List<CompEncabezado> 	encabezado;
	private int 					ancho;
	private List<T> 				modelos;
	private EventListener			controlador= null;
	
	
	private Toolbar	 				busqueda;
	private Combobox 				cmbBuscar;
	private Textbox 				txtValor;
	private Button	 				btnBuscar;
	
	/*public CompBuscar(List<CompEncabezado> encabezado, int i){
		caption  = null;
		filaCatalogo = i;
		catalogo = new CompCatalogo(encabezado);
		init();
	}*/
	
	public CompBuscar(List<CompEncabezado> encabezado, int i){
		//this.caption = caption;
		filaCatalogo = i;
		this.encabezado = encabezado;
		catalogo = new CompCatalogo<T>(encabezado);
		init();
	}
	
	
	private void init(){
		
		popup 					= new Bandpopup();
		Vbox cajaV				= new Vbox();
		cmbBuscar   			= new Combobox();
		txtValor    			= new Combobox();		
		btnBuscar   			= new Button("Buscar");
		busqueda				= new Toolbar();
		//this.setAutodrop(false);
		this.setAutodrop(true);
		this.setReadonly(false);
		busqueda.setMold("panel");
		busqueda.appendChild(cmbBuscar);
		busqueda.appendChild(txtValor);
		busqueda.appendChild(btnBuscar);
		catalogo.addEventListener(Events.ON_SELECT, this);
		//cmbBuscar.setModelo(new SimpleListModel(encabezado.toArray()));
		cmbBuscar.addEventListener("onSelect", this);
/*
		txtValor.setAutocomplete(true);
		txtValor.setAutodrop(true);
		txtValor.setButtonVisible(false);
*/
		txtValor.addEventListener(Events.ON_OK,this);
		btnBuscar.addEventListener("onClick", this);
		cajaV.appendChild(busqueda);
		cajaV.appendChild(catalogo);
		popup.appendChild(cajaV);
		this.addEventListener(Events.ON_CHANGE, this);
		this.appendChild(popup);
		cmbBuscar.setReadonly(true);
		setModeloBuscar(encabezado);
	}
	
	private void setModeloBuscar(List<CompEncabezado> modelo){
		if (modelo != null){
	  		Comboitem item;
	  		cmbBuscar.setValue("");
	  		cmbBuscar.getChildren().clear();
			for (CompEncabezado dato : modelo) {
				item = new Comboitem();
				item.setLabel(dato.getDescripcion());
				cmbBuscar.appendChild(item);
			}
		}
	}
	
	public void setListenerEncontrar(EventListener controlador){
		this.controlador = controlador;
		addEventListener(ON_SELECCIONO, controlador);
	}

	public void onEvent(Event event) throws Exception {
		System.out.println(event.getTarget()+"   "+event.getName());
		if (event.getTarget() == catalogo){ 
			getValor();
			System.out.println("cerrando buscar");
			this.close();
			Events.sendEvent(new Event(ON_SELECCIONO, this));
		}
		else{
			if (event.getName() == Events.ON_CLICK){
				if (event.getTarget() == btnBuscar){
					filtrar();
					catalogo.clearSelection();
				}
			}
			else if (event.getName() == Events.ON_OK){
				if (event.getTarget() == txtValor){
					filtrar();
					catalogo.clearSelection();
				}
			}
			else {
				busqueda();
			}
		}
	}
	
	public void busqueda(){
		System.out.println("buscando");
		cmbBuscar.setSelectedIndex(filaCatalogo);
		txtValor.setText(this.getText());
		txtValor.setText(this.getValue());
		System.out.println(catalogo.getVisibleItemCount());
		System.out.println("filtrando");
		filtrar();
		if (catalogo.getVisibleItemCount() == 1){
			/*catalogo.setSelectedIndex(catalogo.getVisibleBegin());*/
			getValor();
			System.out.println("cerrando buscar");
			this.close();
			Events.sendEvent(new Event(ON_SELECCIONO, this));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void  cargarValoresTabla(List<List> filas, List objetos) throws ExcColumnasInvalidas{
		catalogo.cargarDatos(filas, objetos);
	}
	
	@SuppressWarnings("unchecked")
	public void getValor() throws NullPointerException{
		this.setValue(catalogo.obtenerSeleccion(filaCatalogo));
		this.setAttribute("valor", catalogo.obtenerObjeto());
		seleccion = (T) catalogo.obtenerObjeto();
	}
	
	public void setModelo(List<T> modelos){
		if (modelos != null){
			this.modelos = modelos;
			catalogo.setModelo(modelos);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getValorObjeto(){
		return (T) this.getAttribute("valor");
	}
	
	

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
		catalogo.setWidth(ancho+"px");
	}


	public T getSeleccion() {
		return seleccion;
	}


	public void setSeleccion(T seleccion) {
		if (seleccion != null){
			try{
				this.setAttribute("valor", seleccion);
				this.seleccion = seleccion;
				this.setValue(catalogo.buscarObjeto(seleccion, filaCatalogo));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object clone(){
		CompBuscar obj=null;
		CompCatalogo catalogo; 
        try{
        	catalogo = new CompCatalogo(this.encabezado);
        	catalogo.setModelo(modelos);
        	
        	obj= new CompBuscar(this.encabezado, filaCatalogo);
            catalogo = obj.getCatalogo();
            catalogo.setModelo(modelos);
            obj.setAncho(this.ancho);
            obj.setWidth(this.getWidth());
            if (this.controlador != null)
            	obj.setListenerEncontrar(controlador);
       		Iterator it =  getAttributes().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                obj.setAttribute((String) pairs.getKey(), pairs.getValue());
            }
       
       
        }catch(Exception e){
        	e.printStackTrace();
            System.out.println(" no se puede duplicar");
        }
       return obj;
    }

	

	public void setPopup(Bandpopup popup) {
		this.popup = popup;
	}


	public CompCatalogo<T> getCatalogo() {
		return catalogo;
	}


	public void setCatalogo(CompCatalogo<T> catalogo) {
		this.catalogo = catalogo;
	}


	public int getFilaCatalogo() {
		return filaCatalogo;
	}


	public void setFilaCatalogo(int filaCatalogo) {
		this.filaCatalogo = filaCatalogo;
	}


	
	public void filtrar(){
		catalogo.refrescar(cmbBuscar.getSelectedIndex(), txtValor.getValue().trim());
	}
	

	public Textbox getTxtValor() {
		return txtValor;
	}


	public void setTxtValor(Combobox txtValor) {
		this.txtValor = txtValor;
	}

}

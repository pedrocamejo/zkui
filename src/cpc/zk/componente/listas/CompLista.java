package cpc.zk.componente.listas;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Window;

import cva.pc.componentes.CompEncabezado;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;


public class CompLista<T> extends Listbox implements Serializable, EventListener{
	
	private static final long serialVersionUID = 7130460898507667328L;
	private int  					nroColumnas;
	private List<CompEncabezado> 	encabezados;
	private List<T>     			modelo;
	private DataBinder				binder;
	private Menupopup 				menuContextual;
	private Menuitem				menuItem;
	private Component				padre = null;
	private Listitem  				items;
	
	public CompLista(){
		super();
		inicializar();		
	}
	
	public CompLista(List<CompEncabezado> encabezado){
		super();
		inicializar();
		crearEncabezado(encabezado);
	}
	public CompLista(List<CompEncabezado> encabezado, Component padre){
		super();
		inicializar();
		crearEncabezado(encabezado);
		this.padre = padre;
		agregarMenuContextual();// Agregar un Menu Contextual al Padre Tipo Ventana
		
	}
	
	public CompLista(List<CompEncabezado> encabezado, List<T> datos) throws ExcColumnasInvalidas{
		super();
		inicializar();
		crearEncabezado(encabezado);
		setModelo(datos);
		
	}
	
	public void inicializar(){
		binder = new AnnotateDataBinder(this);
	}
	
		
	public void setTipoSeleccion(){
		this.setCheckmark(true);
		this.setMultiple(true);
	}
	
	public void crearEncabezado(List<CompEncabezado> encabezado){
		encabezados = encabezado;
		Listhead encabezadoG = new Listhead();
		encabezadoG.setSizable(true);
		Listheader titulo;
		nroColumnas = 0;
		for (CompEncabezado compEncabezado : encabezado) {
			if (compEncabezado.isVisible()){
				titulo = new Listheader();
				titulo.setLabel(compEncabezado.getDescripcion());
				if (compEncabezado.isOrdenable())
					titulo.setSort("auto");
				titulo.setAlign(compEncabezado.getStrAlineacion());
				titulo.setWidth(compEncabezado.getAncho()+"px");
				encabezadoG.appendChild(titulo);
				nroColumnas++;
			}
		} 
		this.appendChild(encabezadoG);
	}


	
	public void eliminarItem(int i){
		removeItemFromSelection(getSelectedItem());
	}

	public void eliminarItem(){
		Listitem item= this.getSelectedItem();
		this.removeChild(item);
	}
	
	@SuppressWarnings("unchecked")
	public void actualizar(int i, List columnas, Object objeto){
		Listitem item= this.getItemAtIndex(i);
		List<Listcell>  celdas= item.getChildren();
		item.setAttribute("dato", objeto);
		Listcell celda;
		int j=0;
		for (Object columna : columnas) {
			celda = celdas.get(j++);
			if (celda.getAttribute("tipo").equals("imagen"))
				celda.setImage(columna.toString());
			else
				celda.setLabel(columna.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void actualizar(List columnas, Object objeto){
		Listitem item= this.getSelectedItem();
		List<Listcell>  celdas= item.getChildren();
		Listcell celda;
		item.setAttribute("dato", objeto);
		item.setAttribute("visible", true);
		int j=0;
		for (Object columna : columnas) {
			celda = celdas.get(j++);
			if (celda.getAttribute("tipo").equals("imagen"))
				celda.setImage(columna.toString());
			else
				celda.setLabel(columna.toString());
		}
	}
	
	public Object obtenerObjeto(){
		Listitem item= this.getSelectedItem();
		if (item == null)
			return null;
		else
			return  item.getAttribute("dato");
	}

	private List<Listcell> ordenarCeldas(List<Listcell> entrada){
		List<Listcell> salida = new ArrayList<Listcell>();
		for ( int j=0; j < encabezados.size();j++){
			for (Listcell celda : entrada) {
				if (celda.getAttribute("indice").equals(new Integer(j))){
					salida.add(celda);
					entrada.remove(celda);
					break;
				}
			}
		}
		return salida;
	}
	
	
	@SuppressWarnings("unchecked")
	public Listitem editDato(T dato){
		Listitem  itemnes = this.getSelectedItem();
		List<Listcell> celdas = ordenarCeldas(itemnes.getChildren());
		Listcell  celda;
		for (int i=0;i<encabezados.size();i++) {
			CompEncabezado encabe = encabezados.get(i);
			celda = (Listcell) celdas.get(i);
			try{
				celda.setAttribute("tipo", "texto");
				Class clase = dato.getClass();
				Method metodoEjecutable;
				metodoEjecutable = clase.getMethod(encabe.getMetodoBinder(), null);
				try{
					celda.setLabel((String) metodoEjecutable.invoke(dato, null));
					binder.addBinding(celda, "label",  encabe.getMetodoModelo(), null, null, "save", null, null, null, null);
				}catch (ClassCastException e){
					celda.setLabel(String.valueOf(metodoEjecutable.invoke(dato, null) ));
					binder.addBinding(celda, "label",  encabe.getMetodoModelo(), null, null, "save", null, null, null, null);
				}
				itemnes.appendChild(celda);
			}catch ( Exception e )
			{
			   e.printStackTrace();
			}
		}	
		itemnes.setAttribute("dato", dato);
		itemnes.setAttribute("visible", true);
		return itemnes;
	}
	
	
	@SuppressWarnings("unchecked")
	public Listitem cargarDato(T dato){
		items = new Listitem();
		Listcell  celda;
		int i=0;
		for (CompEncabezado encabe  : encabezados) {
			celda = new Listcell();
			try{
				celda.setAttribute("tipo", "texto");
				celda.setAttribute("indice", new Integer(i++));
				Class clase = dato.getClass();
				Method metodoEjecutable;
				metodoEjecutable = clase.getMethod(encabe.getMetodoBinder(), null);
				//System.out.println(dato.getClass().getName()+",  Id celda ("+celda.getId()+"), id LitItem "+itemnes.getId());
				try{
					celda.setLabel((String) metodoEjecutable.invoke(dato, null));
					binder.addBinding(celda, "label",  encabe.getMetodoModelo(), null, null, "save", null, null, null, null);
				}catch (ClassCastException e){
					celda.setLabel(String.valueOf(metodoEjecutable.invoke(dato, null) ));
					binder.addBinding(celda, "label",  encabe.getMetodoModelo(), null, null, "save", null, null, null, null);
				}
				items.appendChild(celda);
			}catch ( Exception e )
			{
				e.printStackTrace();
			}
		}	
		items.setAttribute("dato", dato);
		items.setAttribute("visible", true);
		items.setContext(menuContextual);
		return items;
	}
	
	
	public void cargarbinder(){
		for (int i=0;i<modelo.size();i++) {
			this.appendChild(cargarDato(modelo.get(i)));
			
		}
	}
	
	public void agregar(T objeto){
		this.appendChild(cargarDato(objeto));
	}

	public void editar(T objeto){
		editDato(objeto);
	}

	
	@SuppressWarnings("unchecked")
	public void agregar(List columna, Object objeto){
		Listitem  itemnes = new Listitem();
		Listcell  celda;		
		for (Object object : columna) {
			celda = new Listcell();
			celda.setAttribute("tipo", "texto");
			//celda.setLabel(String.valueOf(objeto));
			celda.setLabel(object.toString());
			itemnes.appendChild(celda);
		}
		this.appendChild(itemnes);
	}
	
	@SuppressWarnings("unchecked")
	public void cargarDatos(List<List> columnas, List objetos) throws ExcColumnasInvalidas{
		if (columnas.get(0).size() != nroColumnas)
			throw new ExcColumnasInvalidas();
		cargarDatosImagen(columnas, objetos);
	}
	
	@SuppressWarnings("unchecked")
	public void cargarDatosPuros(List<List> columnas, List datos){
		Listitem  itemnes;
		Listcell  celda;
		Object dato;
		List   objetos;
		Object object;
		for (int i=0; i<columnas.size(); i++) {
			objetos = columnas.get(i);
			dato = datos.get(i);
			itemnes = new Listitem();
			for (int j=0; j<objetos.size(); j++) {
				object = objetos.get(j);
				celda = new Listcell();
				celda.setAttribute("tipo", "texto");
				try{
					System.out.println("no tenia string");
					celda.setLabel(object.toString());
				}catch (Exception e) {
					e.printStackTrace();
				}
				itemnes.appendChild(celda);
			}	
			itemnes.setHeight("28px");
			itemnes.setAttribute("dato", dato);
			itemnes.setAttribute("visible", true);
			this.appendChild(itemnes);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarDatosImagen(List<List> columnas, List datos){
		Listitem  itemnes = new Listitem();
		Listcell  celda;
		Object dato;
		List   objetos;
		Object object;
		for (int r=0; r<columnas.size(); r++) {
			//List objetos : columnas
			objetos = columnas.get(r);
			dato = datos.get(r);
			itemnes = new Listitem();
			int i=0;
			//for (Object object : objetos) {
			for (int m=0; m<objetos.size(); m++) {
				//Object object : objetos
				object = objetos.get(m);
				celda = new Listcell();
				celda.setAttribute("tipo", "texto");
				celda.setLabel(object.toString());
				i++;
				itemnes.appendChild(celda);
			}	
			itemnes.setHeight("28px");
			itemnes.setAttribute("datIdRegistro(0))o", dato);
			itemnes.setAttribute("visible", true);
			this.appendChild(itemnes);
		}
		
	}
	
	public final int getNroColumnas() {
		return nroColumnas;
	}


	public void refrescar(int columna, String filtro){
		Listitem item;
		String cadena;
		int salida ;
		int j = this.getItemCount();
		for (int i=0; i<j; i++){
			item = this.getItemAtIndex(i);
			cadena = ((Listcell)item.getChildren().get(columna)).getLabel().toString().toUpperCase();
			salida = cadena.indexOf(filtro.toUpperCase());
			item.setVisible(salida>=0);
		}
	}
	
	public void refrescar(){
		Listitem item;
		for (int i=0; i<this.getItemCount(); i++){
			item = this.getItemAtIndex(i);
			item.setVisible(true);
		}
	}

	public List<T> getModelo() {
		if (modelo != null)
			if (modelo.size()>0)
				return modelo;
		return getDatos();
	}
	
	@SuppressWarnings("unchecked")
	private List<T> getDatos(){
		Listitem item;
		List<T> datos = new ArrayList<T>();
		int j = this.getItemCount();
		for (int i=0; i<j; i++){
			item = this.getItemAtIndex(i);
			datos.add((T) item.getAttribute("dato"));
		}
		return datos;
	}
	
	

	public void setModelo(List<T> modelo) {
		this.modelo = modelo;		
		binder.bindBean(getNombreModelo(), modelo);
		cargarbinder();
	}
	
	public String getNombreModelo(){
		String cadena = modelo.getClass().getName().toLowerCase();
		return cadena.substring(cadena.lastIndexOf(".")+1);
	}
	
	@SuppressWarnings("unchecked")
	public String buscarObjeto(Object dato, int id){
		T datoReal = (T) dato;
		List<Listitem> items = this.getItems();
		T temp = null;
		for (Listitem item: items){
			if (item != null)
				temp = (T)item.getAttribute("dato");
				if (temp.toString().equals(datoReal.toString())){
					List<Listcell>  celdas= item.getChildren();
					Listcell celda = celdas.get(id);
					return  celda.getLabel();
				}
		}	
		return null;	
	}
	
	public void agregarMenuContextual(){	
		
		if (this.padre instanceof Window){
			menuContextual = new Menupopup();
			menuItem = new Menuitem("Quitar","iconos/24x24/quitar.png");			
			menuItem.addEventListener(Events.ON_CLICK,  new EventListener() {				
				public void onEvent(Event event) throws Exception {	
					System.out.println("Hola");
					quitarItem();
				}
			});
					
			menuContextual.appendChild(menuItem);
			this.padre.appendChild(menuContextual);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void quitarItem(){
		eliminarItem();
		if (this.modelo!=null)
			this.modelo.remove((T)getSelectedItem().getAttribute("dato"));
	}
	
	
	@SuppressWarnings("unchecked")
	public void clear(){
		List<Listitem> items = this.getItems();
		
		for (Listitem item: items){
			this.removeChild(item);
		}
		this.modelo = null;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
	
		
	}
	
	
	
	
}

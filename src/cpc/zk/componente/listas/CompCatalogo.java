package cpc.zk.componente.listas;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

import cva.pc.componentes.CompEncabezado;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.modelo.ColumnaAuxiliar;



public class CompCatalogo<T> extends Listbox implements Serializable{
	
	private static final long serialVersionUID = 7130460898507667328L;
	private int  					nroColumnas;
	private Menupopup				menu; 
	private DataBinder				binder;
	private List<CompEncabezado> 	encabezados;
	private List<T>     			modelo;
	private List<Set<String>>		columnasFiltro;
	private List<Integer>   		imagenes;  
	
	public CompCatalogo(){
		super();
		inicializar();		
	}
	
	public CompCatalogo(List<CompEncabezado> encabezado){
		super();
		inicializar();
		crearEncabezado(encabezado);
	}
	
	
	@SuppressWarnings("unchecked")
	public CompCatalogo(List<CompEncabezado> encabezado, List<Integer> imagenes, List<List> datos, List objetos) throws ExcColumnasInvalidas{
		super();
		inicializar();
		setIndiceImagenes(imagenes);
		crearEncabezado(encabezado);
		cargarDatos(datos, objetos);
		
	}
	
	@SuppressWarnings("unchecked")
	public CompCatalogo(List<CompEncabezado> encabezado,  List<List> datos,  List objetos) throws ExcColumnasInvalidas{
		super();
		inicializar();
		crearEncabezado(encabezado);
		cargarDatos(datos, objetos);
	}

	
	public void inicializar(){
		setSclass("tabla_catalago");
		int pagina = (Integer)  SpringUtil.getBean("tamanoPagina");
		this.setMold("paging");
		this.setPageSize(pagina);
		this.setPagingPosition("top");
		this.getPagingChild().setMold("os");
		this.setFixedLayout(true);
		binder = new AnnotateDataBinder(this);
	}
	
	private void inicializarColumnas(){
		columnasFiltro = new ArrayList<Set<String>>();
		for (int i=0; i<nroColumnas; i++)
			columnasFiltro.add(new HashSet<String>());
	}
	
	public void setTipoSeleccion(){
		this.setCheckmark(true);
		this.setMultiple(true);
	}
	
	public void setIndiceImagenes(List<Integer> imagenes){
		this.imagenes = imagenes;
	}
	
	public void crearEncabezado(List<CompEncabezado> encabezado){
		encabezados = encabezado;
		Listhead encabezadoG = new Listhead();
		encabezadoG.setSizable(true);
		Listheader titulo;
		nroColumnas = 0;
		for (CompEncabezado compEncabezado : encabezado) {
			titulo = new Listheader();
			titulo.setLabel(compEncabezado.getDescripcion());
			titulo.setVisible(compEncabezado.isVisible());
			if (compEncabezado.isOrdenable())
				titulo.setSort("auto");
			titulo.setAlign(compEncabezado.getStrAlineacion());
			titulo.setWidth(compEncabezado.getAncho()+"px");
			encabezadoG.appendChild(titulo);
			nroColumnas++;
		} 
		this.appendChild(encabezadoG);
		inicializarColumnas();
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

	@SuppressWarnings("unchecked")
	public String obtenerSeleccion(int id) throws NullPointerException{
		String salida = "";
		System.out.println("el id es "+this.getId());
		Listitem item= this.getSelectedItem();
		System.out.println("rows ("+this.getItemCount()+"), seleccionados "+this.getSelectedCount());
		List<Listcell>  celdas= item.getChildren();
		Listcell celda;
		celda = celdas.get(id);
		salida = celda.getLabel();
		return  salida;
	}
	
	@SuppressWarnings("unchecked")
	public Listcell getSeleccion(int id) throws NullPointerException{
		Listitem item= this.getSelectedItem();
		List<Listcell>  celdas= item.getChildren();
		return celdas.get(id);
	}
	
	public void addMenuCrud(EventListener controlador){
		crearMenu(controlador);
		/*this.appendChild(menu);
		this.setContext(menu);
		Listitem item;
		for (int i=0; i<this.getItemCount(); i++){
			item = this.getItemAtIndex(i);
			item.setContext(menu);
		}
		this.getListhead().setContext(menu);*/
	}
	
	private void  crearMenu(EventListener controlador){
		menu = new Menupopup();
		Menuitem item = new Menuitem("Agregar");
		item.setId("ADD");
		item.addEventListener(Events.ON_CLICK, controlador);
		menu.appendChild(item);
		item = new Menuitem("editar");
		item.setId("EDI");
		item.addEventListener(Events.ON_CLICK, controlador);
		menu.appendChild(item);
		item = new Menuitem("eliminar");
		item.setId("DEL");
		item.addEventListener(Events.ON_CLICK, controlador);
		menu.appendChild(item);
		item = new Menuitem("consultar");
		item.setId("VER");
		item.addEventListener(Events.ON_CLICK, controlador);
		menu.appendChild(item);
	//	this.getPaging().addEventListener("onPaging",controlador);
	}	
	
	public void addEncabezadoAuxiliar(List<ColumnaAuxiliar> columnas) {
		Auxhead cabezeraAuxuliar = new Auxhead();
		Auxheader columnaAux;
		for (ColumnaAuxiliar columnaAuxiliar : columnas) {
			columnaAux= new Auxheader();
			columnaAux.setLabel(columnaAuxiliar.getDescripcion());
			columnaAux.setColspan(columnaAuxiliar.getColumnas());
			columnaAux.setAlign(columnaAuxiliar.getStrAlineacion());
			cabezeraAuxuliar.appendChild(columnaAux);
		}
		this.appendChild(cabezeraAuxuliar);
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
		Listitem  itemnes = new Listitem();
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
	public void cargarbinder2(){
	//volamos hijos
//		this.getChildren().removeAll(this.getChildren());
		
		int indice = this.getActivePage();
		int rangoi =(int) Math.pow(this.getPageSize(), indice);
		int rangof =rangoi+this.getPageSize();
		
		
		int indice2 = this.getActivePage();
		int rangof2 =(int) Math.pow(indice2, this.getPageSize());
		int rangoi2 =rangof-this.getPageSize();
		
		
		if (modelo != null)
			for (int i=rangoi;i<rangof;i++) {
				this.appendChild(cargarDato(modelo.get(i)));
				
			}
	}
	
	public void cargarbinder(){
		int indice = this.getActivePage();
		int rangof =(int) Math.pow(this.getPageSize(), indice);
		int rangoi =rangof-this.getPageSize();
		if (modelo != null)
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
		System.out.println("esta Agregando");
		if (imagenes == null){
			for (Object object : columna) {
				celda = new Listcell();
				celda.setAttribute("tipo", "texto");
				//celda.setLabel(String.valueOf(objeto));
				celda.setLabel(object.toString());
				itemnes.appendChild(celda);
			}
		}else{
			int i=0;
			int j=0;
			for (Object object : columna) {
				celda = new Listcell();
				if (i == imagenes.get(j)){
					celda.setAttribute("tipo", "imagen");
					celda.setImage(object.toString());
					j++;
				}
				else{
					celda.setAttribute("tipo", "texto");
					celda.setLabel(object.toString());
				}
				i++;
				itemnes.setAttribute("dato", objeto);
				itemnes.setAttribute("visible", true);
				itemnes.appendChild(celda);
			}	
		}
		this.appendChild(itemnes);
	}
	
	@SuppressWarnings("unchecked")
	public void cargarDatos(List<List> columnas, List objetos) throws ExcColumnasInvalidas{
		if (columnas.get(0).size() != nroColumnas)
			throw new ExcColumnasInvalidas();
		if (imagenes == null)
			cargarDatosPuros(columnas, objetos);
		else
			cargarDatosImagen(columnas, objetos);
		
	}
	
	@SuppressWarnings("unchecked")
	private void cargarDatosPuros(List<List> columnas, List datos){
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
					columnasFiltro.get(j).add(object.toString());
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
			int j=0;
			//for (Object object : objetos) {
			for (int m=0; m<objetos.size(); m++) {
				//Object object : objetos
				object = objetos.get(m);
				columnasFiltro.get(m).add(object.toString());
				celda = new Listcell();
				if (i == imagenes.get(j)){
					celda.setAttribute("tipo", "imagen");
					celda.setImage(object.toString());
					j++;
				}
				else{
					celda.setAttribute("tipo", "texto");
					celda.setLabel(object.toString());
				}
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

	@SuppressWarnings("unchecked")
	public Set getColumnas(int columna) {
		return columnasFiltro.get(columna);
	}

	public void refrescar(int columna, String filtro){
		Listitem item;
		boolean primero = false;
		String cadena;
		int salida ;
		int visibles = 0;
		int j = this.getItemCount();
		for (int i=0; i<j; i++){
			item = this.getItemAtIndex(i);
			cadena = ((Listcell)item.getChildren().get(columna)).getLabel().toString().toUpperCase();
			salida = cadena.indexOf(filtro.toUpperCase());
			if (salida>=0 && !primero){
				primero =true;
				item.setSelected(true);
			}
			item.setVisible(salida>=0);
			if (salida>=0) visibles++;
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
		return modelo;
	}

	public void setModelo(List<T> modelo) {
		limpiar();
		//if (modelo == null){ 
			this.modelo = modelo;
			binder.bindBean(getNombreModelo(), modelo);
			cargarbinder();
		//}
	}
	
	public String getNombreModelo(){
		if (modelo != null){
			String cadena = modelo.getClass().getName().toLowerCase();
			return cadena.substring(cadena.lastIndexOf(".")+1);
		}	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String buscarObjeto(Object dato, int id){
		T datoReal = (T) dato;
		List<Listitem> items = this.getItems();
		T temp = null;
		for (Listitem item: items){
			if (item != null)
				temp = (T)item.getAttribute("dato");
				if (temp.equals(datoReal)){
					System.out.println("encontro "+dato.toString());
					List<Listcell>  celdas= item.getChildren();
					Listcell celda = celdas.get(id);
					return  celda.getLabel();
				}
		}	
		return "";	
	}

	public List<Set<String>> getColumnasFiltro() {
		return columnasFiltro;
	}

	public void setColumnasFiltro(List<Set<String>> columnasFiltro) {
		this.columnasFiltro = columnasFiltro;
	}
	
	public void limpiar(){
		this.getItems().clear();
	}
}

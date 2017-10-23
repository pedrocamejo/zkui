package cpc.zk.componente.listas;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import cpc.zk.componente.modelo.ColumnaAuxiliar;
import cpc.zk.componente.modelo.RowLabelComparator;

import org.zkoss.zul.Menupopup;
import org.zkoss.zul.api.Bandbox;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.api.XulElement;

import cva.pc.componentes.CompEncabezado;

public class CompGrilla<T> extends Grid implements EventListener, Serializable{


	public static final String ON_ELIMINA_FILA = "onBorrarFila";
	/**
	 * 
	 */
	private static final long serialVersionUID = -8126954968943487970L;
	
	private Menupopup				menu; 
	private int  					nroColumnas;

	private EventListener			controlador;
	private Column					columna;
	private boolean 				maestroDetalle = false;
	private boolean 				permiteEliminar = true;
	private Columns					columnas;
	private Rows					filas;
	
	public Rows getFilas() {
		return filas;
	}

	private List<CompEncabezado> 	encabezados;
	
	private	 ArrayList 				componentes;
	
	private	T 						modelo; 
	private Class<T>				clase;
	private List<T> 				coleccion;
	private	DataBinder 				binder;
	private	List<DataBinder>		binders;
	
	
	private	int						item=0;
	
	public CompGrilla(EventListener controlador){
		this.controlador = controlador;
		this.setMold("paging");
		int pagina = (Integer) SpringUtil.getBean("tamanoPagina");
		this.setPageSize(pagina);
		//this.setPagingPosition("bottom");
		this.getPagingChild().setMold("os");
		this.setFixedLayout(true);
		filas = new Rows();
		this.setWidth("100%");
		coleccion = new ArrayList<T>();
		binders = new ArrayList<DataBinder>();
	}

	public CompGrilla(){
		this.setMold("paging");
		int pagina = (Integer) SpringUtil.getBean("tamanoPagina");
		this.setPageSize(pagina);
		//this.setPagingPosition("bottom");
		this.getPagingChild().setMold("os");
		this.setFixedLayout(true);
		filas = new Rows();
		this.setWidth("100%");
		coleccion = new ArrayList<T>();
		binders = new ArrayList<DataBinder>();
	}
	
	public CompGrilla(boolean maestroDetalle){
		this.setMold("paging");
		this.maestroDetalle = maestroDetalle;
		int pagina = (Integer) SpringUtil.getBean("tamanoPagina");
		this.setPageSize(pagina);
		//this.setPagingPosition("bottom");
		this.getPagingChild().setMold("os");
		this.setFixedLayout(true);
		filas = new Rows();
		this.setWidth("100%");
		coleccion = new ArrayList<T>();
		binders = new ArrayList<DataBinder>();
	}
	
	private Image  botonEliminar(){
		
		String icono = (String) SpringUtil.getBean("iconoBorrar");
		System.out.println(icono);
		Image boton = new Image(icono);
		boton.setTooltiptext("Eliminar");
		boton.addEventListener(Events.ON_CLICK, this);
		/*boton.setWidth("18px");
		boton.setHeight("18px");*/
		return boton;
	}
	
	public void cargar(List<T> coleccion){
		if (coleccion != null)
			synchronized (coleccion) {
				try{
					for (T miModelo : coleccion) {
						agregar(miModelo);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		
	}
	
	public void refrescar(){
		for (DataBinder item : binders) {
			item.saveAll();
			item.loadAll();
		}
	}
	
	
	public Window findVentana(){
		Component componente = this.getRoot();
		for(;;){
			componente = this.getParent();
			if (componente instanceof Window) {
				break;
			}if (componente == null){
				break;
				
			}
		}
		return (Window) componente;
	}
	
	public String getNombreModelo(){
		String cadena = modelo.getClass().getName().toLowerCase();
		return cadena.substring(cadena.lastIndexOf(".")+1);
	}
	
	@SuppressWarnings("unchecked")
	public void setEncabezados(List<CompEncabezado> encabezados){
		this.encabezados = encabezados;
		columnas = new Columns();
		columnas.setSizable(true);
		nroColumnas = encabezados.size();
		for (int i = 0; i < nroColumnas; i++) {
			if (maestroDetalle && i==0){
				columna = new Column();
				columna.setWidth("40px");
			}else{
				columna = new Column(encabezados.get(i).getDescripcion());
				columna.setWidth(encabezados.get(i).getAncho()+"px");
				columna.setVisible(encabezados.get(i).isVisible());
				if (encabezados.get(i).isOrdenable()){
					Comparator  asc = new RowLabelComparator(true,i);
					Comparator  dsc = new RowLabelComparator(false,i);
					columna.setSortAscending(asc);
					columna.setSortDescending(dsc);
				}
			}
			columnas.appendChild(columna);
		}
		if (permiteEliminar){
			columna = new Column();
			columna.setWidth("20px");
			columnas.appendChild(columna);
		}
		this.appendChild(columnas);
	}
	
	@SuppressWarnings("unchecked")
	public void actualizarEncabezados(){
		List<Column> grid = this.getColumns().getChildren();
		for (int i = 0; i < encabezados.size(); i++) {
			grid.get(i).setVisible(encabezados.get(i).isVisible());
		}
	}
	
	
	public void addMenuAutomatico(){
		columnas.setMenupopup("auto");
	}
	
	public void dibujar(){
		
		this.appendChild(filas);
	}
	
	@SuppressWarnings("unchecked")
	public Row setFila(ArrayList componentes, T modelo, DataBinder binder){
		setComponentes(componentes);
		setModelo(modelo);
		setBinder(binder);
		return setFila();
	}
	
	@SuppressWarnings("unchecked")
	public Row setFila(){
		Row registro;
		registro = new Row();
		Detail detalle = null;
		registro.setId(this.getNombreModelo()+"R"+(item++));
		if (menu != null)
			registro.setContext("MenuGrilla");
		List componentesClon = new ArrayList();
		for (int i = 0; i < componentes.size(); i++) {
			System.out.println(componentes.get(i).getClass().getName());
			AbstractComponent ComponenteClon;
			if (componentes.get(i) instanceof Bandbox){
				ComponenteClon = (CompBuscar) ((CompBuscar)componentes.get(i)).clone();
				
			}else{
				ComponenteClon = (AbstractComponent) ((AbstractComponent)componentes.get(i)).clone();
			}
			componentesClon.add(ComponenteClon);		
		}
		registro.setAttribute("dato", modelo);
		for (int i = 0; i < componentesClon.size(); i++) {
			if (maestroDetalle && i==0){
				detalle = new Detail();
				detalle.setOpen(true);
				detalle.appendChild((Component)componentesClon.get(i));
				registro.appendChild(detalle);
				
			}else{
				registro.appendChild((Component)componentesClon.get(i));
			}
			String metodo1 =(String)encabezados.get(i).getMetodoComponente();
			String modelog = getNombreModelo()+(String)encabezados.get(i).getMetodoModelo();
			//System.out.println("creando binder :"+componentesClon.get(i).getClass().getName()+"----"+metodo1+ " ... "+modelog);
			binder.addBinding((Component)componentesClon.get(i), metodo1, modelog, null, null, "save", null, null, null, null);
			if (modelo != null)
				agregarMetodo(i, componentesClon);
			//fijarMenu((Component)componentes.get(i));
		}
		if (permiteEliminar)
			registro.appendChild(botonEliminar());
		return registro;
	}
	
	public Row getUltimo(){
		int ultimo = filas.getChildren().size();
		return (Row)filas.getChildren().get(ultimo-1);
	}
	
	@SuppressWarnings("unchecked")
	private void agregarMetodo(int i, List componentesClon){
		try {
			if (clase == null)
				if (modelo!= null)
					clase = (Class<T>) modelo.getClass();
				else
					return;
			if (modelo !=null){
				Method metodoEjecutableModelo;
				metodoEjecutableModelo = clase.getMethod(encabezados.get(i).getMetodoBinder(), null);
				Object parametro = metodoEjecutableModelo.invoke(modelo, null); 
				Class claseComponente = componentesClon.get(i).getClass();
				Method metodoEjecutable;
				Class tipoParametro = Object.class;
				if (parametro != null){
					tipoParametro = parametro.getClass();
					String metodoC = encabezados.get(i).getMetodoComponente();
					metodoC="set"+metodoC.substring(0,1).toUpperCase()+metodoC.substring(1);
					System.out.println(metodoC +" clase comp "+claseComponente+" "+metodoC+" tipo parametro "+tipoParametro);
					if (tipoParametro == Boolean.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, boolean.class);
						metodoEjecutable.invoke(componentesClon.get(i), ((Boolean)parametro).booleanValue());
					}else if (tipoParametro == Integer.class){
						try{
							metodoEjecutable = claseComponente.getMethod(metodoC, int.class);
						}catch (NoSuchMethodException e){
							metodoEjecutable = claseComponente.getMethod(metodoC, Integer.class);
						}metodoEjecutable.invoke(componentesClon.get(i), ((Integer)parametro).intValue());
					}else if (tipoParametro == Double.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, double.class);
						metodoEjecutable.invoke(componentesClon.get(i), ((Double)parametro).doubleValue());
					}else if (tipoParametro == Float.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, float.class);
						metodoEjecutable.invoke(componentesClon.get(i), ((Float)parametro).floatValue());
					}else if (tipoParametro == Byte.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, byte.class);
						metodoEjecutable.invoke(componentesClon.get(i), ((Byte)parametro).byteValue());
					}else if (tipoParametro == Long.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, long.class);
						metodoEjecutable.invoke(componentesClon.get(i), ((Long)parametro).longValue());
					}else if (tipoParametro == Date.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, Date.class);
						metodoEjecutable.invoke(componentesClon.get(i), (Date)parametro);
					}else if (tipoParametro == java.sql.Date.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, Date.class);
						metodoEjecutable.invoke(componentesClon.get(i), (Date)parametro);
					}else if (tipoParametro == List.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, List.class);
						metodoEjecutable.invoke(componentesClon.get(i), (List)parametro);	
					}else if (tipoParametro == Collection.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, Collection.class);
						metodoEjecutable.invoke(componentesClon.get(i), (Collection)parametro);
					}else if (tipoParametro == ArrayList.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, ArrayList.class);
						metodoEjecutable.invoke(componentesClon.get(i), (ArrayList)parametro);
					}else if (tipoParametro == String.class || clase.getName().indexOf("Textbox") >0){
						if (parametro== null){
							metodoEjecutable = claseComponente.getMethod(metodoC, Object.class);
							metodoEjecutable.invoke(componentesClon.get(i), "");
						}else{
							metodoEjecutable = claseComponente.getMethod(metodoC, String.class);
							metodoEjecutable.invoke(componentesClon.get(i), parametro);
						}
					}else{
						metodoEjecutable = claseComponente.getMethod(metodoC, Object.class);
						metodoEjecutable.invoke(componentesClon.get(i), parametro);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void agregar(T modelo){
		this.modelo = modelo;
		coleccion.add(modelo);
		binder = new AnnotateDataBinder(this);
		binders.add(binder);
		binder.bindBean(getNombreModelo(), modelo);
		Row fila = setFila();
		filas.appendChild(fila);
		fila.setAttribute("index", filas.getChildren().size()-1);		
		try{
			if (fila.getChildren().get(0) instanceof InputElement) {
				InputElement comp = (InputElement) fila.getChildren().get(0);	
				comp.setFocus(true);
			}else if(fila.getChildren().get(0) instanceof XulElement){
				XulElement comp = (XulElement) fila.getChildren().get(0);	
				comp.setFocus(true);
			}

		}catch (NullPointerException e) {
			e.printStackTrace();
		}	
			
	}

	@SuppressWarnings("unchecked")
	public void setComponentes(ArrayList componentes){
		this.componentes = componentes;
	}
	
	public void setModelo(T modelo){
		this.modelo = modelo;
	}
	
	public void setBinder(DataBinder binder){
		this.binder = binder;
	}
	
	@SuppressWarnings("unchecked")
	public void eliminar(Image boton ){
		Row viejo = (Row) boton.getParent();
		filas.removeChild(boton.getParent());
		Events.sendEvent(new Event(ON_ELIMINA_FILA, this, viejo));
		/****BORRAR MODELO DE LA LISTA ****/
		coleccion.remove((T) viejo.getAttribute("dato"));		
		/****SUPRIMIR SI GENERA ERROR ***/
		reformarIndexRows();
	}
	
	public void obtener(Rows fila){
		this.appendChild(fila);
	}
	
	public void setListenerBorrar(EventListener controlador){
		this.controlador = controlador;
		addEventListener(ON_ELIMINA_FILA, controlador);
	}
	
	@SuppressWarnings("unused")
	private void fijarMenu(Component componente){
		if (menu !=null){
			if (componente instanceof Label) {
				((Label) componente).setContext(menu);
				//componente.addEventListener(Events.ON_DOUBLE_CLICK, controlador);
			}
		}
	}
	
	public void  crearMenu(List<Boolean> CRUD){
		menu = new Menupopup();
		menu.setId(this.getNombreModelo()+"MenuGrilla");
		Menuitem item;
		if (CRUD.get(0)){
			item = new Menuitem("Agregar");
			item.setId(this.getNombreModelo()+"ADD");
			item.addEventListener(Events.ON_CLICK, controlador);
			menu.appendChild(item);
		}if (CRUD.get(1)){
			item = new Menuitem("editar");
			item.setId(this.getNombreModelo()+"EDI");
			item.addEventListener(Events.ON_CLICK, controlador);
			menu.appendChild(item);
		}if (CRUD.get(2)){	
			item = new Menuitem("eliminar");
			item.setId(this.getNombreModelo()+"DEL");
			item.addEventListener(Events.ON_CLICK, controlador);
			menu.appendChild(item);
		}if (CRUD.get(3)){
			item = new Menuitem("consultar");
			item.setId(this.getNombreModelo()+"VER");
			item.addEventListener(Events.ON_CLICK, controlador);
			menu.appendChild(item);
		}
		Window ventana = findVentana();
		if (ventana!=null)
			ventana.appendChild(menu);
	}	
	
	
	public void  crearCRUDArreglo(Boolean[] CRUD){
		menu = new Menupopup();
		menu.setId("MenuGrilla");
		Menuitem item;
		if (CRUD[0]){
			item = new Menuitem("Agregar");
			item.setId(this.getNombreModelo()+"ADD");
			item.addEventListener(Events.ON_CLICK, controlador);
			menu.appendChild(item);
		}if (CRUD[1]){
			item = new Menuitem("editar");
			item.setId(this.getNombreModelo()+"EDI");
			item.addEventListener(Events.ON_CLICK, controlador);
			menu.appendChild(item);
		}if (CRUD[2]){	
			item = new Menuitem("eliminar");
			item.setId(this.getNombreModelo()+"DEL");
			item.addEventListener(Events.ON_CLICK, controlador);
			menu.appendChild(item);
		}if (CRUD[3]){
			item = new Menuitem("consultar");
			item.setId(this.getNombreModelo()+"VER");
			item.addEventListener(Events.ON_CLICK, controlador);
			menu.appendChild(item);
		}
		Window ventana = findVentana();
		if (ventana!=null)
			ventana.appendChild(menu);
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

	public Menupopup getMenu() {
		return menu;
	}

	public void onEvent(Event event) throws Exception {
		eliminar((Image) event.getTarget() );
	}
	
	public List<T> getColeccion() {
		return coleccion;
	}

	public EventListener getControlador() {
		return controlador;
	}
	
	
	@SuppressWarnings("unchecked")
	public void refrescarColeccion() {
		coleccion= new ArrayList<T>();
		synchronized (coleccion) {
			List<Row> columnasT = filas.getChildren();
			for(Row item: columnasT){
				coleccion.add((T) item.getAttribute("dato"));
			}
		}
	}

	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}

	@SuppressWarnings("unchecked")
	public void setDisable(boolean desactivar ){
		List<Row> filas = getFilas().getChildren();
		for (Row item : filas) {
			InputElement componente;
			for (int i=0; i<item.getChildren().size();i++){
				if (item.getChildren().get(i) instanceof InputElement){
					componente =(InputElement)item.getChildren().get(i);
					componente.setDisabled(desactivar);
				}else if (item.getChildren().get(i) instanceof Image){
					((Image)item.getChildren().get(i)).setVisible(!desactivar);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void clear(){
		List<Row> filas = getFilas().getChildren();
		for (int i=filas.size();i>0 ;i--){
			filas.remove(i-1);
		}
			
	}
	
	@SuppressWarnings("unchecked")
	public Row obtenerRowDelModelo(T modelo){
		List<Row> filas = getFilas().getChildren();
		for (Row item : filas) {			 
			 T dato = (T) item.getAttribute("dato");			
			 if (dato.equals(modelo)){			
				return item;  
			 }
		}		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void reformarIndexRows(){
		List<Row> filas = getFilas().getChildren();
		for (int i = 0; i<filas.size(); i++) {
			filas.get(i).setAttribute("index",i);
		}
		
	}

	public void setColeccion(List<T> coleccion) {
		if (coleccion!=null)
			if (coleccion.size() >0){
				modelo	= coleccion.get(0);
				cargar(coleccion);
			}
	}

	public Class<T> getClase() {
		return clase;
	}

	public void setClase(Class<T> clase) {
		this.clase = clase;
	}

	public boolean isMaestroDetalle() {
		return maestroDetalle;
	}

	public void setMaestroDetalle(boolean maestroDetalle) {
		this.maestroDetalle = maestroDetalle;
	}

	public boolean isPermiteEliminar() {
		return permiteEliminar;
	}

	public void setPermiteEliminar(boolean permiteEliminar) {
		this.permiteEliminar = permiteEliminar;
	}
	
	
}

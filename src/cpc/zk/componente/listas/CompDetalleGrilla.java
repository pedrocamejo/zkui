package cpc.zk.componente.listas;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cpc.zk.componente.modelo.ColumnaAuxiliar;

import org.zkoss.zul.api.Bandbox;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.api.XulElement;

import cva.pc.componentes.CompEncabezado;

public class CompDetalleGrilla<T> extends Vbox implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6059973130946150625L;

	private EventListener			controlador;

	private List<CompEncabezado> 	encabezados;
	
	@SuppressWarnings("unchecked")
	private	ArrayList 				componentes;
	
	private	T 						modelo; 
	private Class<T>				clase;
	private List<T> 				coleccion;
	private	DataBinder 				binder;
	private	List<DataBinder>		binders;
	
	
	//private	int						item=0;
	
	public CompDetalleGrilla(EventListener controlador){
		this.controlador = controlador;
		this.setWidth("100%");
		coleccion = new ArrayList<T>();
		binders = new ArrayList<DataBinder>();
	}

	public CompDetalleGrilla(){
		this.setMold("paging");
		this.setWidth("100%");
		coleccion = new ArrayList<T>();
		binders = new ArrayList<DataBinder>();
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
	
	public void setEncabezados(List<CompEncabezado> encabezados){
		this.encabezados = encabezados;
	}
	
	@SuppressWarnings("unchecked")
	public Hbox setFila(ArrayList componentes, T modelo, DataBinder binder){
		setComponentes(componentes);
		setModelo(modelo);
		setBinder(binder);
		return setFila();
	}
	
	@SuppressWarnings("unchecked")
	public Hbox setFila(){
		Hbox registro;
		registro = new Hbox();
		//registro.setId(this.getNombreModelo()+"R"+(item++));
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
			registro.appendChild((Component)componentesClon.get(i));
			String metodo1 =(String)encabezados.get(i).getMetodoComponente();
			String modelog = getNombreModelo()+(String)encabezados.get(i).getMetodoModelo();
			//System.out.println("creando binder :"+componentesClon.get(i).getClass().getName()+"----"+metodo1+ " ... "+modelog);
			binder.addBinding((Component)componentesClon.get(i), metodo1, modelog, null, null, "save", null, null, null, null);
			if (modelo != null)
				agregarMetodo(i, componentesClon);
		}
		return registro;
	}
	
	public Hbox getUltimo(){
		int ultimo = this.getChildren().size();
		return (Hbox)this.getChildren().get(ultimo-1);
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
					}else if (tipoParametro == ArrayList.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, ArrayList.class);
						metodoEjecutable.invoke(componentesClon.get(i), (ArrayList)parametro);	
					}else if (tipoParametro == List.class){
						metodoEjecutable = claseComponente.getMethod(metodoC, List.class);
						metodoEjecutable.invoke(componentesClon.get(i), (List)parametro);	
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
		Hbox fila = setFila();
		this.appendChild(fila);
		fila.setAttribute("index", this.getChildren().size()-1);		
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
	
	
	public void obtener(Rows fila){
		this.appendChild(fila);
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
			List<Hbox> columnasT = this.getChildren();
			for(Hbox item: columnasT){
				coleccion.add((T) item.getAttribute("dato"));
			}
		}
	}

	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}

	@SuppressWarnings("unchecked")
	public void setDisable(boolean desactivar ){
		List<Hbox> filas = this.getChildren();
		for (Hbox item : filas) {
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
		List<Row> filas = this.getChildren();
		for (int i=filas.size();i>0 ;i--){
			filas.remove(i-1);
		}
			
	}
	
	@SuppressWarnings("unchecked")
	public Row obtenerRowDelModelo(T modelo){
		List<Row> filas = this.getChildren();
		for (Row item : filas) {			 
			 T dato = (T) item.getAttribute("dato");			
			 if (dato.equals(modelo)){			
				return item;  
			 }
		}		
		return null;
	}
	

	public void setColeccion(List<T> coleccion) {
		if (coleccion!=null)
			if (coleccion.size() >0){
				modelo	= coleccion.get(0);
				cargar(coleccion);
			}
	}

	@SuppressWarnings("unchecked")
	public void setColeccion(ArrayList coleccion) {
		if (coleccion!=null)
			if (coleccion.size() >0){
				modelo	= (T) coleccion.get(0);
				cargar(coleccion);
			}
	}
	
	public Class<T> getClase() {
		return clase;
	}

	public void setClase(Class<T> clase) {
		this.clase = clase;
	}




	
}

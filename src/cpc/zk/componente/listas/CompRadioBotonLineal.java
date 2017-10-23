package cpc.zk.componente.listas;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;

public class CompRadioBotonLineal<T> extends Radiogroup implements EventListener, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4629630944957064729L;
	
	private List<T>			modelo;	
	
	private Radio			boton;
	private int     		ultimo = 0;
	private T 	    		seleccion;
	private double			ancho = 150;
	private int				nroColumnas;
	private int				nroColumna;
	private List<String>	anchoColumnas;
	private Grid 			grid;
	private Rows 			rows;
	private Columns 		columnas;
	private Row 			row;
	private List<Radio> 	botonera;
	
	
	

	public CompRadioBotonLineal(List<T> modelo){
		if (modelo.size()>5)
			nroColumnas = 5;
		else
			nroColumnas = modelo.size();
		this.modelo = modelo; 
		inicializar();
	}
	
	public CompRadioBotonLineal(List<T> modelo, boolean auto){
		if (modelo.size()>5)
			nroColumnas = 5;
		else
			nroColumnas = modelo.size();
		this.modelo = modelo; 
		inicializar();
		if (auto)
			dibujar();
	}
	
	public CompRadioBotonLineal(List<T> modelo, int columnas){
		nroColumnas = columnas;
		this.modelo = modelo; 
		inicializar();
	}
	
	
	private void inicializar(){		
		grid 		= new Grid();
		rows 		= new Rows();
		row 		= new Row();
		columnas 	= new Columns();
		botonera = new ArrayList<Radio>();
		this.addEventListener(Events.ON_CHECK, this);
		initAnchoColumnas();
		nroColumna =0;
		Column  columna;
		for (String ancho : anchoColumnas) {
			columna 	= new Column();
			if (ancho != null)
				columna.setWidth(ancho+"px");
			columnas.appendChild(columna);
		}
		grid.appendChild(columnas);
		this.grid.setStyle("padding: 5px; background : white; border : 0px solid white ");
		addBotones();
	}

	public void redimensionar(){
		Column columna;
		for (int i=0; i<nroColumnas; i++) {
			if (anchoColumnas.get(i) != null){
				columna  = (Column) columnas.getChildren().get(i);
				columna.setWidth(anchoColumnas.get(i));
			}
		}
	}
	
	
	public void addComponente(Component componente){
		row.appendChild(componente);
		row.setStyle("background-color:white; color:black;  border-style : hidden; border-color :white");
		nroColumna++;
		rows.appendChild(row);
		if (nroColumna>=nroColumnas){
			row = new Row();
			rows.appendChild(row);
			nroColumna = 0;
		}
	}
	
	public void dibujar(Component componente){
		grid.appendChild(rows);
		this.appendChild(grid);
		componente.appendChild(this);
		redimensionar();
	}
	
	
	public void dibujar(){
		grid.appendChild(rows);
		this.appendChild(grid);
		redimensionar();
	}
	
	public void addComponente(String texto, Component componente){
		addComponente(new Label(texto));
		addComponente(componente);
	}
	
	public void setAnchoColumna(int columna, int ancho){
		if (anchoColumnas == null){
			anchoColumnas = new ArrayList<String>();
			for (int i=0; i< nroColumnas; i++) {
				anchoColumnas.add(null);
			}
		}
		anchoColumnas.set(columna, ancho+"px");
	} 
	
	
	public void setAnchoColumnas(int ancho){
		if (anchoColumnas == null){
			anchoColumnas = new ArrayList<String>();
			for (int i=0; i< nroColumnas; i++) {
				anchoColumnas.add(ancho+"px");
			}
		}else{
			for (int i=0; i< nroColumnas; i++) {
				anchoColumnas.set(i, ancho+"px");
			}
		}
	}
	
	public void setAnchoColumnas(){
		anchoColumnas = new ArrayList<String>();
		for (int i=0; i< nroColumnas; i++) {
			anchoColumnas.add("260px");
		}
	}
	
	
	public void initAnchoColumnas(){
		anchoColumnas = new ArrayList<String>();
		for (int i=0; i< nroColumnas; i++) {
			anchoColumnas.add(null);
		}
	}
	
	public boolean isOpen() {
		return this.grid.isVisible();

	}

	public void setOpen(boolean open){
		this.grid.setVisible(open);
	}
	


	private void addBoton(T label){
		boton = new Radio(label.toString());
		boton.setValue(""+ultimo++);
		boton.setWidth(ancho+"px");
		boton.setAttribute("dato", label);
		botonera.add(boton);
		//boton.beforeParentChanged(grupo);
		//grupo.appendChild(boton);
		//grupo.appendItem(label, ""+ultimo++);
		addComponente(boton);
	}
	
	
	private void addBotones(){
		for (T valor : modelo) {
			addBoton(valor);
		}
	}
	
	
	public T getSeleccion(){
		return seleccion;
	}
	
	public void setIndexSeleccion(int i){
		try{
			this.setSelectedIndex(i);
			seleccion = modelo.get(i);
		}catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}
		//((Radio)getChildren().get(i)).setChecked(true);
	}
	
	public void setSeleccion(T valorI) throws ExcDatosInvalidos{
		if (valorI != null){
			int i=-1;
			for (int j=0; j<modelo.size();j++) {
				if (valorI.toString().equals(modelo.get(j).toString())){
					i=j;
					break;
				}
			}
			if (i==-1) throw new  ExcDatosInvalidos("Valor no valido");
			this.setSelectedIndex(i);
			seleccion = modelo.get(i);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		if (event.getName() == Events.ON_CHECK){
			seleccion = (T) this.getSelectedItem().getAttribute("dato");
		}
	}
	
	public double getAncho() {
		return ancho;
	}

	public void setAncho(double ancho) {
		this.ancho = ancho;
	}

	public void setDisabled(boolean disabled){
		for (Radio opcion : botonera){
			opcion.setDisabled(disabled);
		}
	}
	
}

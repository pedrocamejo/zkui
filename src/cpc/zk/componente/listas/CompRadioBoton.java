package cpc.zk.componente.listas;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zhtml.Caption;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;

public class CompRadioBoton<T> extends Div implements EventListener, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4629630944957064729L;
	
	private List<T>			modelo;	
	
	private Radio			boton;
	private int     		ultimo = 0;
	private T 	    		seleccion;
	private Radiogroup		grupo;
	private double			ancho = 5000;
	private Caption 		titulo;
	private int				nroColumnas;
	private int				nroColumna;
	private List<String>	anchoColumnas;
	private Grid 			grid;
	private Rows 			rows;
	private Columns 		columnas;
	private Row 			row;
	private Div  			divTitulo;
	private List<Radio> 	botonera;
	

	public CompRadioBoton(List<T> modelo, String stitulo){
		if (modelo.size()>5)
			nroColumnas = 5;
		else
			nroColumnas = modelo.size();
		this.modelo = modelo; 
		divTitulo 	= new Div();					
		Label label = new Label(stitulo);
		this.divTitulo.appendChild(label);
		this.appendChild(divTitulo);
		inicializar();
	}
	
	public CompRadioBoton(List<T> modelo, String stitulo, boolean auto){
		if (modelo.size()>5)
			nroColumnas = 5;
		else
			nroColumnas = modelo.size();
		this.modelo = modelo; 
		divTitulo 	= new Div();					
		Label label = new Label(stitulo);
		this.divTitulo.appendChild(label);
		this.appendChild(divTitulo);
		inicializar();
		if (auto)
			dibujar();
	}
	
	public CompRadioBoton(List<T> modelo){		
		if (modelo.size()>5)
			nroColumnas = 5;
		else
			nroColumnas = modelo.size();
		this.modelo = modelo;
		divTitulo 	= new Div();					
		Label label = new Label();
		this.divTitulo.appendChild(label);
		this.appendChild(divTitulo);
		inicializar();
	}
	
	public CompRadioBoton(List<T> modelo, String stitulo, int columnas){
		nroColumnas = columnas;
		this.modelo = modelo; 
		divTitulo 	= new Div();					
		Label label = new Label(stitulo);
		this.divTitulo.appendChild(label);
		this.appendChild(divTitulo);
		inicializar();
	}
	
	public CompRadioBoton(List<T> modelo, int columnas){
		nroColumnas = columnas;
		this.modelo = modelo;
		divTitulo 	= new Div();					
		Label label = new Label();
		this.divTitulo.appendChild(label);
		this.appendChild(divTitulo);
		inicializar();
	}
	
	private void inicializar(){		
		grid 		= new Grid();
		rows 		= new Rows();
		row 		= new Row();
		columnas 	= new Columns();
		grupo 		= new Radiogroup();
		botonera 	= new ArrayList<Radio>();
		grupo.addEventListener(Events.ON_CHECK, this);
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
		this.grid.setStyle("padding: 5px; background : white; border : 0px solid silver ");
		this.setStyle("border : 1px solid gray");
		this.divTitulo.setStyle("background:url('css/img/zul/groupbox/groupbox-hm_1.png'); padding:3px; font-family :arial; font-size:80%; border-bottom:1px solid gray;");
		this.divTitulo.addEventListener(Events.ON_CLICK,this);
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
		grupo.appendChild(grid);
		this.appendChild(grupo);
		componente.appendChild(this);
		redimensionar();
		componente.appendChild(new Separator());
		//this.addComponente(grupo);
	}
	
	
	public void dibujar(){
		grid.appendChild(rows);
		grupo.appendChild(grid);
		this.appendChild(grupo);
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
	public Caption getTitulo() {
		return titulo;
	}

	public void setTitulo(Caption titulo) {
		this.titulo = titulo;
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
	
	public void setSeleccion(int i){
		grupo.setSelectedIndex(i);
		seleccion = modelo.get(i);
		//((Radio)getChildren().get(i)).setChecked(true);
	}
	
	public void setSeleccion(T valorI) throws ExcDatosInvalidos{
		int i=-1;
		for (int j=0; j<modelo.size();j++) {
			if (valorI.equals(modelo.get(j))){
				i=j;
				break;
			}
		}
		if (i==-1) throw new  ExcDatosInvalidos("Valor no valido");
		grupo.setSelectedIndex(i);
		seleccion = modelo.get(i);
	}
	
	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		if (event.getName() == Events.ON_CHECK){
			seleccion = (T) grupo.getSelectedItem().getAttribute("dato");
		}
		if (event.getTarget() == this.divTitulo){			
			this.grid.setVisible(!grid.isVisible());			
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

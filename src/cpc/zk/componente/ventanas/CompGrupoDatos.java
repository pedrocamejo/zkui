package cpc.zk.componente.ventanas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;

public class CompGrupoDatos extends Div implements EventListener, Serializable{

	private static final long serialVersionUID = -7183210073252533239L;
	protected int				nroColumnas;
	protected int				nroColumna;
	private List<String>		anchoColumnas;
	private Grid 				grid;
	private Rows 				rows;
	private Columns 			columnas;
	private Row 				row;
	private Div  				divTitulo;
	private Div  				divCuerpo;
	private Label				lblTitulo;
	
	public CompGrupoDatos(String stitulo){		
		nroColumnas = 2;		
		divTitulo = new Div();					
		lblTitulo = new Label(stitulo.toUpperCase());
		lblTitulo.setStyle("font-weight:bold;");
		this.divTitulo.setStyle("margin: 5px; border-bottom:1px solid gray;");
		this.divTitulo.appendChild(lblTitulo);
		this.appendChild(divTitulo);
		inicializar();
	}

	public CompGrupoDatos(){		
		nroColumnas = 2;		
		divTitulo = new Div();					
		lblTitulo = new Label();
		lblTitulo.setStyle("font-weight:bold;");
		this.divTitulo.setStyle("margin: 5px; border-bottom:1px solid gray;");
		this.divTitulo.appendChild(lblTitulo);
		this.appendChild(divTitulo);
		inicializar();
	}

	public CompGrupoDatos(int columnas){		
		nroColumnas = columnas;		
		divTitulo = new Div();			
		lblTitulo = new Label();
		lblTitulo.setStyle("font-weight:bold;");
		this.divTitulo.setStyle("margin: 5px; border-bottom:1px solid gray;");
		this.divTitulo.appendChild(lblTitulo);
		this.appendChild(divTitulo);
		inicializar();
	}
	
	public CompGrupoDatos(String stitulo, int columnas){		
		nroColumnas = columnas;		
		divTitulo = new Div();			
		lblTitulo = new Label(stitulo.toUpperCase());
		lblTitulo.setStyle("font-weight:bold;");
		this.divTitulo.setStyle("margin: 5px; border-bottom:1px solid gray;");
		this.divTitulo.appendChild(lblTitulo);
		this.appendChild(divTitulo);
		inicializar();
	}
	
	@SuppressWarnings("unchecked")
	public void setAlineacion(String alineacion){
		List<Rows> filas = grid.getChildren();
		for (Rows itemg: filas ){
			List<Row> detalleRows = itemg.getChildren();
			for (Row item : detalleRows) {
				divCuerpo = (Div) item.getChildren();
				divCuerpo.setAlign(alineacion);
			}
		}
	}
	
	private void inicializar(){		
		grid 		= new Grid();
		rows 		= new Rows();
		row 		= new Row();
		divCuerpo	= new Div();
		row.setStyle("background-color:white; color:black;  border-style : hidden; border-color :white");
		
		grid.setStyle(" border-color : #c6e1af");
		columnas 	= new Columns();
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
		this.grid.setStyle("padding: 5px; background :white;  border-style : hidden; border-color :white");
		this.setStyle("border : 1px solid gray; margin: 5px;");
		this.divTitulo.addEventListener(Events.ON_CLICK,this);
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
		//row.appendChild(componente);
		divCuerpo = new Div();
		divCuerpo.appendChild(componente);
		row.appendChild(divCuerpo);
		nroColumna++;
		rows.appendChild(row);
		if (nroColumna>=nroColumnas){
			row = new Row();
			row.setStyle("background-color:white; color:black;  border-style : hidden; border-color :white");
			rows.appendChild(row);
			nroColumna = 0;
		}
	}
	
	public void dibujar(Component componente){
		grid.appendChild(rows);
		this.appendChild(grid);
		componente.appendChild(this);
		redimensionar();
		componente.appendChild(new Separator());
	}
	
	
	public void dibujar(){
		grid.appendChild(rows);
		this.appendChild(grid);
		redimensionar();
	}
	
	public void dibujarGrupos(CompGrupoDatos componente1, CompGrupoDatos componente2){
		componente1.dibujar();
		componente2.dibujar();
		row= new Row();
		divCuerpo = new Div();
		row.setStyle("background-color:white; color:black; border-style : hidden; border-color :white");
		/*divCuerpo.appendChild(componente1);
		divCuerpo.appendChild(componente2);
		row.appendChild(divCuerpo);*/
		row.appendChild(componente1);
		row.appendChild(componente2);
		rows.appendChild(row);
		grid.appendChild(rows);
		this.appendChild(grid);
		redimensionar();
	}
	
	
	public void addComponente(String texto, Component componente){
		addComponente(new Label(texto));
		addComponente(componente);
	}
	
	public void addComponente(Component componente1,Component componente2){
		addComponente(componente1);
		addComponente(componente2);
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
	
	public void onEvent(Event evento) throws Exception {
		if (evento.getTarget() == this.divTitulo){			
			this.grid.setVisible(!grid.isVisible());			
		}		
	}

	public Grid getGrid() {
		return grid;
	}

	public Rows setTitulo() {
		return rows;
	}
	
	public void setTitulo(String titulo) {
		divTitulo = new Div();					
		lblTitulo = new Label(titulo.toUpperCase());
		lblTitulo.setStyle("font-weight:bold;");
		this.divTitulo.setStyle("margin: 5px; border-bottom:1px solid gray;");
		this.divTitulo.appendChild(lblTitulo);
		this.appendChild(divTitulo);
	}
	
	public void addComponenteMultiples(String etiqueta, int ancho, Component ... componentes){
		int total=componentes.length;
		System.out.println("registros "+total);
		Grid grilla	= new Grid();
		Columns	columnas = new  Columns();
		columnas.setSizable(true);
		Column colomna;
		grilla.setStyle("border-style : hidden; border-color :white");
		for (int i=0; i<total;i++){
			colomna =new Column();
			colomna.setWidth(ancho+"px");
			columnas.appendChild(colomna);
		}
		grilla.appendChild(columnas);
		
		Rows  filas = new Rows();
		Row row = new Row();
		if (nroColumnas == 1)
			row.appendChild(new Label(etiqueta));
		for (Component item: componentes){
			row.setStyle("background-color:white; color:black; border-style : hidden; border-color :white");
			row.appendChild(item);
		}
		filas.appendChild(row);
		grilla.appendChild(filas);
		if (nroColumnas == 1){
			addComponente(grilla);
		}else{	
			addComponente(new Label(etiqueta));
			addComponente(grilla);
		}
	}
	
	public void addComponenteMultiples(int ancho, Component ... componentes){
		int total=componentes.length;
		System.out.println("registros "+total);
		Grid grilla	= new Grid();
		Columns	columnas = new  Columns();
		columnas.setSizable(true);
		Column colomna;
		grilla.setStyle("border-style : hidden; border-color :white");
		for (int i=0; i<total;i++){
			colomna =new Column();
			colomna.setWidth(ancho+"px");
			columnas.appendChild(colomna);
		}
		grilla.appendChild(columnas);
		
		Rows  filas = new Rows();
		Row row = new Row();
		if (nroColumnas == 1)
			addComponente(componentes[0]);
		for (int i=1; i<componentes.length;i++){
			row.setStyle("background-color:white; color:black; border-style : hidden; border-color :white");
			row.appendChild(componentes[i]);
		}
		filas.appendChild(row);
		grilla.appendChild(filas);
		if (nroColumnas == 1){
			addComponente(grilla);
		}else{
			addComponente(componentes[0]);
			addComponente(grilla);
		}
	}

	public Label getLblTitulo() {
		return lblTitulo;
	}

	public void setLblTitulo(Label lblTitulo) {
		this.lblTitulo = lblTitulo;
	}
	
}

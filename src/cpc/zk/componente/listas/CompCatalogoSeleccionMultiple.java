package cpc.zk.componente.listas;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cva.pc.componentes.CompEncabezado;

public class CompCatalogoSeleccionMultiple<T> extends Window implements EventListener{
		
	private static final long serialVersionUID = -1970818338688728303L;	
	private CompListaBusca<T> listaResultado;
	private Hbox	botonera, contComponentes;
	private Vbox	contCompAdicionales;
	private Button	btnAceptar,btnCancelar,btnLimpiarLista;	
	private List<CompEncabezado> encabezados;
	private int indice;
	private EventListener controlador;
	private Component padre;
	
	public CompCatalogoSeleccionMultiple(List<CompEncabezado> encabezados,int indice,EventListener controlador, Component padre) {
		this.encabezados = encabezados;
		this.indice = indice;
		this.controlador = controlador;
		this.padre = padre;
		this.inicializar(); 
		this.dibujar();
		this.listaResultado.limpiarLista();
	}	
	
	private void inicializar(){			
		listaResultado = new CompListaBusca<T>(encabezados, indice, controlador,this.padre);
		botonera = new Hbox();	
		contCompAdicionales = new Vbox();		
		btnAceptar = new Button("Aceptar");
		btnCancelar = new Button("Cancelar");
		btnLimpiarLista = new Button("Limpiar Lista");
	
	}
	
	private void dibujar(){		
		try {			
			this.setTitle("Catalogo de Seleccion Multiple");
			this.setBorder("normal");
			this.setClosable(true);
			this.setMode("modal");			
			
			this.contCompAdicionales.setStyle("margin: 5px; border:1px solid gray; padding:5px");			
			this.appendChild(contCompAdicionales);
			this.appendChild(listaResultado);
			this.appendChild(botonera);
			this.botonera.appendChild(btnAceptar);
			this.botonera.appendChild(btnCancelar);
			this.botonera.appendChild(btnLimpiarLista);			
			
			this.btnAceptar.setId("btnAceptar");
			this.btnCancelar.setId("btnCancelar");
			this.btnLimpiarLista.setId("btnLimpiarLista");
			
			this.btnAceptar.addEventListener(Events.ON_CLICK, this.controlador);
			this.btnCancelar.addEventListener(Events.ON_CLICK, this);
			this.btnLimpiarLista.addEventListener(Events.ON_CLICK, this);
			
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}

	public CompListaBusca<T> getListaResultado() {
		return listaResultado;
	}

	public void setListaResultado(CompListaBusca<T> listaResultado) {
		this.listaResultado = listaResultado;
	}
	
	public void setModelo(List<T> datos){
		
		this.listaResultado.setModelo(datos);
	}	

	public Button getBtnAceptar() {
		return btnAceptar;
	}

	public void setBtnAceptar(Button btnAceptar) {
		this.btnAceptar = btnAceptar;
	}

	public Button getBtnCancelar() {
		return btnCancelar;
	}

	public void setBtnCancelar(Button btnCancelar) {
		this.btnCancelar = btnCancelar;
	}

	public Button getBtnLimpiarLista() {
		return btnLimpiarLista;
	}

	public void setBtnLimpiarLista(Button btnLimpiarLista) {
		this.btnLimpiarLista = btnLimpiarLista;
	}
	
	public void agregarComponente(String etiqueta,Component comp){
		contComponentes = new Hbox();
		contComponentes.appendChild(new Label(etiqueta));
		contComponentes.appendChild(comp);
		contCompAdicionales.appendChild(contComponentes);
	}
	
	public void removerComponente(Component comp){
		contCompAdicionales.removeChild(comp);
	}

	@Override
	public void onEvent(Event evento) throws Exception {
		if (evento.getName()== Events.ON_CLICK){
			if (evento.getTarget() instanceof Button){
				Button btn = ((Button)evento.getTarget());
				if (btn.getId()== "btnCancelar"){
					this.onClose();
					
				}
				
				if (btn.getId()== "btnLimpiarLista"){
					this.listaResultado.limpiarLista();
					
				}
			}
		}
	
		
	}
}

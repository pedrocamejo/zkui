package cpc.zk.componente.ui;

import java.io.Serializable;

import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cpc.ares.interfaz.IMenu;
import cpc.ares.modelo.Sede;
import cpc.ares.modelo.Usuario;
import cpc.zk.componente.controlador.ContLogin;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompLogin;

public abstract class UIAmbiente<T> extends  GenericAutowireComposer implements IZkAplicacion, Serializable{
	
	private static final long serialVersionUID = 8061167411842686604L;
	 
	private Vbox     	contGeneral;
	private Splitter 	sepMenuEscritorio;
	private Window   	contFormas;
	private Hbox     	contAmbiente;
	private Vbox     	contMenu;	
	private Div      	divEncabezadoAplicacion;
	private Div		 	divPiePagina;
	private Div		 	divCuerpo;
	private Div 	 	divInfoUsuario;
	
	private Label 		lblDatosUsuario;
	private Label 		lblPieDePagina;
	private Button 		btnResetSesion;
	private Img 		imgBanner;
	private EventListener 	controlador;
	
	
	private     CompLogin   login;
	

	public UIAmbiente() {		
		init();
		dibujar();
		
	}
	
	public void init(){
		
		this.contGeneral 	= new Vbox();
		this.sepMenuEscritorio = new Splitter();		
		this.contFormas     = new Window();
		this.contAmbiente   = new Hbox();
		this.contMenu       = new Vbox();
		this.imgBanner      = new Img();		
		this.divEncabezadoAplicacion  = new Div();		
		this.divCuerpo	    = new Div();
		this.divPiePagina   = new Div();
		this.divInfoUsuario = new Div();
		this.lblDatosUsuario= new Label();
		
		this.lblPieDePagina = new Label("Datos de Pie de Pagina");
		this.btnResetSesion = new Button("Rst");
		
		
	}
	
	public void dibujar(){			
	    this.divEncabezadoAplicacion.setWidth("100%");
	    this.divEncabezadoAplicacion.setHeight("100px");		    
		this.divEncabezadoAplicacion.appendChild(imgBanner);			
								
		this.contFormas.setHeight("700px");			
								
		this.divInfoUsuario.setStyle("background:#f0f0f0; padding: 0px; border:1px solid silver;margin-right 5px");
		this.divInfoUsuario.setWidth("99,6%");
		this.divInfoUsuario.setHeight("28px");	
		
		
		this.btnResetSesion.setWidth("14px");
		this.btnResetSesion.setHeight("14px");
		
		//this.divInfoUsuario.appendChild(this.btnResetSesion);		
		this.lblDatosUsuario.setStyle("color: white");
		this.divInfoUsuario.appendChild(this.lblDatosUsuario);
		
		this.sepMenuEscritorio.setCollapse("before");
		this.sepMenuEscritorio.setOpen(true);
		
		this.contMenu.setWidth("160px");			
		this.contMenu.setHeight("100%");			
		this.contMenu.setStyle("background: black");
		
		
		this.contAmbiente.appendChild(this.contMenu);
		this.contAmbiente.appendChild(sepMenuEscritorio);
		this.contAmbiente.appendChild(this.contFormas);	
		
		this.contGeneral.appendChild(this.contAmbiente);
		this.contGeneral.setWidth("100%");
		this.contGeneral.setHeight("100%");
		this.divCuerpo.appendChild(this.contGeneral);
		
		this.divPiePagina.setStyle("background: silver; padding: 5px; border:1px solid silver;");
		this.divPiePagina.appendChild(lblPieDePagina);
	}
	
	public void agregarAEscritorio(Component componente){
		
		if (this.contFormas.getFirstChild() != null)
			this.contFormas.removeChild(this.contFormas.getFirstChild());
		this.contFormas.appendChild(componente);
	}
	
	public void agregarMenu(Component componente){
		this.contMenu.appendChild(componente);
	}
	
	public void setFondoAplicacion(String imagen){		
		((Window)self).setStyle("background:url('"+imagen+"') repeat-x ");
	}
	
	
	public void setBannerAplicacion(String imagen, boolean repiteEjeX){
		if (repiteEjeX)
		    this.divEncabezadoAplicacion.setStyle("background:url('"+imagen+"') repeat-x ");
		else 
			this.divEncabezadoAplicacion.setStyle("background:url('"+imagen+"') no-repeat");
		
	}
	
	public void setDatosUsuario(String datosUsuario){
		this.lblDatosUsuario.setValue(datosUsuario);
		
	}
	
	public void setImagenBotonResetSesion(String imagen){
		
	}
	
	public void setImagenFondoDatosUsuario(String imagen){
		this.divInfoUsuario
				.setStyle("background:url('"
						+ imagen
						+ "') repeat-x; padding: 2px; border:1px solidwidth: 100%;width: 100%;width: 100%;width: 100%;;margin-right 5px");
	}
	
	public void setImagenFondoPiePagina(String imagen){
		this.divPiePagina.setStyle("background:url('"+imagen+"') repeat-x; padding: 5px; border:1px solid silver;");
	}
	
	public void setColorFondoPiePagina(String color){
		this.divPiePagina.setStyle("background-color:"+color+"padding: 5px; border:1px solid silver;");
	}
	
	public void setTextPiePagina(String texto){
		this.lblPieDePagina.setValue(texto);
		
	}	
	
	public void doAfterCompose(Component comp) throws Exception {	
		try {
			super.doAfterCompose(comp);
			((Window)self).setWidth("100%");
			((Window)self).setHeight("100%");
			((Window)self).appendChild(this.divEncabezadoAplicacion);
			((Window)self).appendChild(this.divInfoUsuario);
			((Window)self).appendChild(this.divCuerpo);			 
			((Window)self).appendChild(this.divPiePagina);
			login =new CompLogin();
			((Window)self).appendChild(login);
			setBannerAplicacion("imagenes/banner_ares.png", false);
			setFondoAplicacion("imagenes/fondoAplicacion.png");
			setImagenFondoDatosUsuario("imagenes/fondo_info_usuario.png");			
			this.setControlador(new ContLogin(this));
			
			
		} catch (Exception e) {
	
		}		
	}
	

	public final CompLogin getLogin() {
		return login;
	}

	public final void setLogin(CompLogin login) {
		this.login = login;
	}
	
	public final EventListener getControlador() {
		return controlador;
	}

	public final void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}
	
	

	public Component getSelf(){
		return self;
	}


	
	public void agregar(Component comp){
		((Window)self).appendChild(comp);
	}
	



	@SuppressWarnings("static-access")
	public void mostrarConfirmacion(String arg0) {
		Messagebox mensaje = new Messagebox();
		try{
			mensaje.show(arg0, "Confirmacion", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}

	
	@SuppressWarnings("static-access")
	public void mostrarError(String arg0)  {
		Messagebox mensaje = new Messagebox();
		try{
			mensaje.show(arg0, "Error", Messagebox.OK, Messagebox.ERROR);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		//((Window)self).appendChild( mensaje );
		
	}


	public void mostrarImpresion(String arg0) {
		// TODO Auto-generated method stub
		
	}

	
	@SuppressWarnings("static-access")
	public void mostrarInformacion(String arg0) {
		Messagebox mensaje = new Messagebox();
		try{
			mensaje.show(arg0, "Informacion", Messagebox.OK, Messagebox.INFORMATION);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}


	public abstract void reiniciarInfUsaurio();
	public abstract void restablecerEscritorio();
	public abstract void setMenuIni(IMenu arg0);
	public abstract void setEscritorio();
	public abstract void cambiarFuete(int arg0);
	public abstract void configurarEscritorio() ;
	public abstract Object getContextProperty(String arg0);
	
	
	public void setSede(Sede sede){
		getSelf().getDesktop().getSession().setAttribute("sede", sede);
		System.out.println("Sede en session "+sede.getNombre());
	}
	
	public Sede getSede(){
		Sede sede = (Sede) getSelf().getDesktop().getSession().getAttribute("sede");
		return sede;
	}

	public Usuario getUsuario() {
		Usuario usuario = (Usuario) getSelf().getDesktop().getSession().getAttribute("usuario");
		return usuario;
	}

	public void setUsuario(Usuario usuario){
		getSelf().getDesktop().getSession().setAttribute("usuario", usuario);
	}
	

	public String getdatosUsuario() {
		Usuario usuario = (Usuario) getSelf().getDesktop().getSession().getAttribute("usuario");
		return usuario.getNombreIdentidad()+" "+usuario.getApellido();
	}


	public void limpiarEscritorio() {
		// TODO Auto-generated method stub
		
	}
	
	public void agregarASession(T componente) {
		getSelf().getDesktop().getSession().setAttribute("sesion", componente);
		
	}

	@SuppressWarnings("unchecked")
	public T getDeSession() {
		T sesion = (T) getSelf().getDesktop().getSession().getAttribute("sesion");
		return sesion;
	}
	
}

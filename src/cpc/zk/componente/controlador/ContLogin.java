package cpc.zk.componente.controlador;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;







import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInvalida;
import cpc.ares.modelo.Modulo;
import cpc.ares.modelo.Sistema;
import cpc.ares.modelo.Usuario;
import cpc.ares.persistencia.PerUsuario;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.menus.CompAcordeon;
import cpc.zk.componente.ventanas.CompLogin;



public class ContLogin implements EventListener, Serializable {
	
	private static final long 		serialVersionUID = 2567836584315117687L;	
	private IZkAplicacion 			appAres;	
	private int						numeroIntentos=0;	
	private int 					sistema;	
	private Usuario					usuario;
	
	
	public ContLogin(IZkAplicacion uInterface) {
		this.appAres =  uInterface;
		appAres.getLogin().setControlador(this);
		
	}
	

	
	public void validarUsuario(String nombre, String contrasena) {
		
	}

	public void aceptar() throws Exception{
		String login = appAres.getLogin().getJ_username().getValue();
		String clave = appAres.getLogin().getJ_password().getValue();
		sistema  = Sistema.getSistema();
		List<Modulo> modulos;
		try {
			usuario = PerUsuario.verificarUsuario(login,clave, sistema ,appAres);
			if (numeroIntentos >2)
				if (!appAres.getLogin().getCaptcha().getValue().equalsIgnoreCase(appAres.getLogin().getJ_captcha().getValue()))
						throw new ExcEntradaInvalida("Datos de la imagen incorrecta");
			modulos = PerUsuario.obtenerPermisos(usuario.getId(), appAres);
			CompAcordeon acordeon = new CompAcordeon(modulos);
			appAres.agregarMenu(acordeon);
			appAres.agregarReporte();
			appAres.getLogin().quitarModal();
			appAres.setEscritorio();		
			appAres.setUsuario(usuario);
			appAres.setSede(usuario.getSede());
			appAres.configurarEscritorio();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExcAccesoInvalido e) {
			numeroIntentos++;
			appAres.mostrarError(e.getMensaje());
			if (numeroIntentos ==3)
				appAres.getLogin().setVisibleCapcha(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ExcEntradaInvalida e) {
			appAres.mostrarError(e.getMessage());
		
		} catch (ExcArgumentoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cancelar(){
		
	}


	public void onEvent(Event event) throws Exception {
		/*System.out.println(event.getTarget());
		System.out.println(event.getTarget().getRoot());
		System.out.println(event.getTarget().getId());
		System.out.println(event.getName());*/
		//eventos click
		if (event.getName() == Events.ON_CLICK){
			if (event.getTarget().getId() == CompLogin.BOTONACEPTAR){  //boton ACEPTAR
				aceptar();
			}
			else if (event.getTarget().getId() == CompLogin.BOTONRECETEAR){  //boton ACEPTAR
				cancelar();
			}
			else if (event.getTarget() == appAres.getLogin().getCaptcha()){  //boton ACEPTAR
				appAres.getLogin().getCaptcha().randomValue();
			}
		}else if(event.getName() == Events.ON_OK){
			aceptar();
		}
			
		/*try {
			usuario = PerUsuario.verificarUsuario(uInterface.getTxtNombre(), uInterface.getTxtContrasena(), Sistema.getSistema(),AppAres);
			menuIni = new ContAcordeon(usuario.getPerfil().getModulos());
			if (accion.getActionCommand()==UILogin.COMMAND_OK){
				boolean puedeCerrar;
				if (trabFunc == 0)
					puedeCerrar=true;
				else
					puedeCerrar=false;
				System.out.println("*****************puede cerrar = "+ puedeCerrar+" trabaja con unidades "+trabFunc);
				unidades = PerUsuario.getUnidades(usuario);
				System.out.println("cantidad unidades : " +unidades.size() +"  - usa unidades "+trabFunc);
				menuIni = new ContAcordeon(usuario.getPerfil().getModulos());
				System.out.println(usuario.getListaUnidades());
				if (usuario.getUnidadesAdministrativas().size()==0){
					AppAres.cargarInfUsuario(usuario.getIdentidad().getNombreCompleto()+" <"+usuario.getNombre()+">", usuario.getSede());
					puedeCerrar=true;
				}if (usuario.getUnidadesAdministrativas().size()>=1){// Tiene Una Unidad Administrativa Asociada
					
					AppAres.cargarInfUsuario(usuario.getIdentidad().getNombreCompleto()+" <"+usuario.getNombre()+">", usuario.getSede(), usuario.getUnidadesAdministrativas());
					
					if (unidades.size() == 1){
						usuario.setUnidadActiva(usuario.getUnidadesAdministrativas().get(0));
						puedeCerrar=true;
					}
					
				}if(unidades.size() > 1 && trabFunc > 0 ){
					uInterface.getEntUnidadAdministrativa().setVisible(true);
					uInterface.getCmbUnidades().setModel(new DefaultListModel(unidades.toArray()));					
					uInterface.getEntContrasena().setVisible(false);
					uInterface.getEntNombre().setVisible(false);
					uInterface.getBtnAceptar().setVisible(false);					
				}
				AppAres.setMenuIni(menuIni);
				AppAres.setUsuario(usuario);
				System.out.println("*****************puede cerrar = "+ puedeCerrar+" trabaja con unidades "+trabFunc);
				if (puedeCerrar || trabFunc == 0){
					AppAres.quitarDelEscritorio(uInterface);
					AppAres.restablecerEscritorio();
				}
				
			
			}if (accion.getActionCommand()== "cancelar" ){
				
			}if (accion.getSource()==uInterface.getCmbUnidades()){
				//Sede sede = new Sede(obtenerIdSede(),uInterface.getCmbUnidades().getSelectedItem().toString(),null,true);
				//AppDemeter.cargarInfUsuario(usuario.getIdentidad().getNombreCompleto()+" <"+usuario.getNombre()+">", sede);
				usuario.setUnidadActiva(unidades.get(uInterface.getCmbUnidades().getSelectedIndex()));
				AppAres.setMenuIni(menuIni);
				AppAres.setUsuario(usuario);
				AppAres.quitarDelEscritorio(uInterface);
				AppAres.restablecerEscritorio();
				
			}
		} catch (NoSuchAlgorithmException e) {				
			e.printStackTrace();
		} catch (ExcAccesoInvalido e) {		
			e.printStackTrace();
			AppAres.mostrarError(e.getMensaje());				
		} catch (SQLException e) {				
			e.printStackTrace();
		} catch (ExcArgumentoInvalido e) {				
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		*/
	}
}

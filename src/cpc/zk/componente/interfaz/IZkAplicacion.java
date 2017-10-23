package cpc.zk.componente.interfaz;

import java.io.Serializable;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkex.zul.Jasperreport;

import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IMenu;
import cpc.zk.componente.ventanas.CompLogin;

public interface IZkAplicacion extends IAplicacion, Serializable{

	public Component getSelf();
	public void doAfterCompose(Component comp) throws Exception;
	public void agregar(Component comp);
	public void agregarReporte();
	public void setMenuIni(IMenu arg0);
	public void agregarAEscritorio(Component componente);
	public CompLogin getLogin();
	public void setLogin(CompLogin login)  throws Exception;
	public void agregarMenu(Component componente);
	public void setEscritorio();
	public void agregarHija(Component componente);
	public Jasperreport getReporte();
	public void setReporte(Jasperreport reporte);
	public String getIp();
	public String getNombreUsuario();
	@SuppressWarnings("rawtypes")
	public Map getXLSParameters(boolean passProtected);
}
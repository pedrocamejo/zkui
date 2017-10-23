package cpc.zk.componente.excepciones;

public class ExcColumnasInvalidas extends Exception{

	private String mensaje;
	
	
	public ExcColumnasInvalidas() {
		super("numero de columnas no valido");	
		this.mensaje = "numero de columnas no valido";
	}
	
	public ExcColumnasInvalidas(String string) {
		// TODO Auto-generated constructor stub
		super(string);	
		this.mensaje = string;
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2493687376842569315L;
	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}

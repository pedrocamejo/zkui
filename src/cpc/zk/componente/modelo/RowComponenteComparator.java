package cpc.zk.componente.modelo;

import java.util.Comparator;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Decimalbox;
import org.zkoss.zul.api.Intbox;


@SuppressWarnings("unchecked")
public class RowComponenteComparator implements Comparator {
	private boolean _asc;
	public RowComponenteComparator(boolean asc) {
		_asc = asc;
	}
	public int compare(Object o1, Object o2) {
		String s1 = getValue(o1), s2 = getValue(o2);
		int v = s1.compareTo(s2);
		return _asc ? v: -v;
	}
	private String getValue(Object o) {
		String salida="";
		if (o instanceof Textbox) {
			salida = ((Textbox)((Row)o).getChildren().get(0)).getValue();
		}else if (o instanceof Label) {
			salida = ((Label)((Row)o).getChildren().get(0)).getValue();	
		}else if (o instanceof Checkbox) {
			salida = String.valueOf(((Checkbox)((Row)o).getChildren().get(0)).isChecked());
		}else if (o instanceof Combobox) {
			salida = ((Combobox)((Row)o).getChildren().get(0)).getSelectedItem().getLabel();
		}else if (o instanceof Datebox) {
			salida = String.valueOf(((Datebox)((Row)o).getChildren().get(0)).getValue());
		}else if (o instanceof Decimalbox) {
			salida = String.valueOf(((Decimalbox)((Row)o).getChildren().get(0)).getValue());
		}else if (o instanceof Intbox) {
			salida = String.valueOf(((Intbox)((Row)o).getChildren().get(0)).getValue());
		}
		return salida;
	}
}

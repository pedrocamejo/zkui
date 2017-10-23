package cpc.zk.componente.modelo;


import java.util.Comparator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;


@SuppressWarnings("unchecked")
public class RowLabelComparator implements Comparator {
	private boolean _asc;
	private int _col;
	public RowLabelComparator(boolean asc, int col) {
		_asc = asc;
		_col = col;
	}
	public int compare(Object o1, Object o2) {
		String s1 = getValue(o1), s2 = getValue(o2);
		int v = s1.compareTo(s2);
		return _asc ? v: -v;
	}
	private String getValue(Object o) {
		return ((Label)((Component)o).getChildren().get(_col)).getValue().trim().toUpperCase();
	}
}
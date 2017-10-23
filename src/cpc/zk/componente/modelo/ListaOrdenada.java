package cpc.zk.componente.modelo;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaOrdenada {
	@SuppressWarnings("unchecked")
	public static void ordena(List lista, final String propiedad) {
		Collections.sort(lista, new Comparator() {

			public int compare(Object obj1, Object obj2) {

				Class clase = obj1.getClass();
				// String getter = "get" +
				// Character.toUpperCase(propiedad.charAt(0)) +
				// propiedad.substring(1);
				String getter = propiedad; // Character.toUpperCase(propiedad.charAt(0))
											// + propiedad.substring(1);
				try {
					Method getPropiedad = clase.getMethod(getter);

					Object propiedad1 = getPropiedad.invoke(obj1);
					Object propiedad2 = getPropiedad.invoke(obj2);

					if (propiedad1 instanceof Comparable
							&& propiedad2 instanceof Comparable) {
						Comparable prop1 = (Comparable) propiedad1;
						Comparable prop2 = (Comparable) propiedad2;
						return prop1.compareTo(prop2);
					}// CASO DE QUE NO SEA COMPARABLE
					else {
						if (propiedad1.equals(propiedad2))
							return 0;
						else
							return 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
	}
}

package model.utils; 

import java.util.Comparator;

import model.data_structures.ILista;
import model.data_structures.SeparateChainingHash;
import model.data_structures.SeparateChainingHash.Nodo;

public class Ordenamiento<K extends Comparable<K>, V extends Comparable <V>> {

	public void ordenarInsercion(ILista<K> lista, Comparator<K> criterio, boolean ascendente) {
		int n = lista.size();
		int scale = ascendente ? 1 : -1;
		for (int i = 0; i <= n; i++) {
			boolean flag = false;
			for (int j = i + 1; j > 0 && !flag; j -= 1) {
				K elem1 = lista.getElement(j);
				K elem2 = lista.getElement(j - 1);
				if (elem1 != null && elem2 != null) {
					int factorComparacion = scale * criterio.compare(elem1, elem2);
					if (factorComparacion < 0) {
						lista.exchange(j, j - 1);
					} else
						flag = true;
				}
			}
		}
	}

	public void ordenarShell(ILista<K> lista, Comparator<K> criterio, boolean ascendente) {
		int n = lista.size();
		int h = 1;
		while (h < n / 3)
			h = h * 3 + 1;
		int scale = (ascendente) ? 1 : -1;
		while (h >= 1) {
			for (int i = h + 1; i < n; i++) {
				boolean flag = false;
				for (int j = i; j > h && !flag; j -= h) {
					K elem = lista.getElement(j);
					K elem2 = lista.getElement(j - h);
					if (criterio.compare(elem, elem2) * scale < 0) {
						lista.exchange(i, j);
					} else {
						flag = true;
					}
				}
			}
			h /= 3;
		}
	}

	

	/**
	 * Método que va dejando el pivot en su lugar, mientras mueve elementos menores
	 * a la izquierda del pivot y elementos mayores a la derecha del pivot.
	 */
	private final int partition(SeparateChainingHash<K, V> lista, Comparator<V> criterio, boolean ascendente, int lo, int hi) {
		int follower, leader;
		follower = leader = lo;
		Nodo n3=  (Nodo) lista.getTable()[hi];
		Nodo n4 = null;
		while (leader <= hi) {
			V elem1 = (V) lista.getTable()[leader].getValue();
			V elem2 = (V) lista.getTable()[follower].getValue();
			
			Nodo n1=  (Nodo) lista.getTable()[leader];
			Nodo n2=  (Nodo) lista.getTable()[follower];
		
			
			if (elem1 != null && elem2 != null) {

				int factorComparacion = (ascendente ? 1 : -1)
						* criterio.compare(elem1, elem2);
				if (factorComparacion < 0) {
					
					lista.getTable()[follower]=n1;
					lista.getTable()[leader]=n2;
					follower++;
				}
			}
			leader++;
			n4=n2;
		}
		lista.getTable()[follower]= n3;
		lista.getTable()[hi]= n4;
		return follower;
	}

	/**
	 * Se localiza el pivot, utilizando el método de partición. Luego se hace la
	 * recursión con los elementos a la izquierda del pivot y los elementos a la
	 * derecha del pivot.
	 */
	private final void ordenarQuicksort(SeparateChainingHash<K, V>lista, Comparator<V> criterio, boolean ascendente, int lo, int hi) {
		if (lo >= hi)
			return;
		int pivot = partition(lista, criterio, ascendente, lo, hi);
		ordenarQuicksort(lista, criterio, ascendente, lo, pivot - 1);
		ordenarQuicksort(lista, criterio, ascendente, pivot + 1, hi);
	}

	/**
	 * Método de entrada, lanza el quick sort recursivo.
	 */
	public final void quickSort(SeparateChainingHash<K, V> lista, Comparator<V> criterio, boolean ascendente) {
		ordenarQuicksort(lista, criterio, ascendente, 0, lista.getSize() - 1);
	}
}

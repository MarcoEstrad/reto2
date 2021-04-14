package model.data_structures;

import java.util.Iterator;

public interface ITablaHash<K extends Comparable<K>, V>
{	
	public void put(K key, V value);
	public V get (K key);
	public V delete(K key);
	public Iterable<K>keys();
	public void reHash();
}

package model.data_structures;

import java.util.Iterator;

public class SeparateChainingHash<K extends Comparable<K>, V extends Comparable<V>>  implements ITablaHash<K,V>
{
	private int capacidad;
	public final static double FACTOR_CARGA=5.0;
	private int peso;
	private Nodo[] arreglo;
	private int rehashes;
	



	public SeparateChainingHash(int m)
	{
		capacidad=m;
		arreglo = new Nodo[m];
		peso = 0;
		rehashes = 0;
	}

	private int hash(K key) 
	{
		return (key.hashCode() & 0x7fffffff) % capacidad;
	}
	
	public int getSize()
	{
		return peso;
	}

	@Override
	public void put(K key, V value) 
	{
		// TODO Auto-generated method stub
		int i = hash(key); 
		Nodo nuevo = new Nodo(key, value);
		for(Nodo x = arreglo[i]; x != null; x = x.next())
		{
			if(key.equals(x.getKey()))
			{
				x.value = value;
				return;
			}
		}
		if(arreglo[i] == null)
		{
			arreglo[i] = nuevo;
			peso++;
		}
		else
		{
			nuevo.changeNext(arreglo[i]);
			arreglo[i] = nuevo;
			peso++;
		}
		if (peso/capacidad == 5.0)
		{
			reHash();
		}



	}

	@Override
	public V get(K key) 
	{
		// TODO Auto-generated method stub
		int i = hash(key);
		for( Nodo x = arreglo[i]; x != null; x = x.next())
		{
			if (key.equals(x.getKey()))
			{
				return (V) x.getValue();
			}
		}
		return null;
	}

	@Override
	public V delete(K key) 
	{
		// TODO Auto-generated method stub
		int i = hash(key); 
		for( Nodo x = arreglo[i]; x != null; x = x.next())
		{
			if (x.getKey().equals(key))
			{
				Nodo retorno = x;
				arreglo[i] = x.next();
				peso--;
				return (V) retorno.getValue();
			}
		}
		return null;
	}
	
	
	
	public Nodo[] getTable()
	{
		return arreglo;
	}
	public int getCapacidad()
	{
		return capacidad;
	}
	public int getRehashes()
	{
		return rehashes;
	}



	@Override
	public void reHash() {
		// TODO Auto-generated method stub
		SeparateChainingHash<K, V> nuevo = new SeparateChainingHash<>(capacidad*2 +1) ;
		for(int i = 0; i < capacidad; i++)
		{
			for(Nodo x = arreglo[i]; x != null; x = x.next())
			{
				nuevo.put((K)x.getKey(), (V)x.getValue());
			}
		}
		this.arreglo = nuevo.arreglo;
		this.peso = nuevo.peso;
		this.capacidad = nuevo.capacidad;
		rehashes++;	
	}
	public static class Nodo<K, V>
	{
		private K key; 
		private V value; 
		private Nodo next;

		public Nodo(K pKey, V pValue)
		{
			key = pKey; 
			value = pValue;
			next = null;
		}
		public Nodo next()
		{
			return next;
		}
		public void changeNext(Nodo siguiente)
		{
			next = siguiente;
		}
		public void changeValue( V value)
		{
			this.value = value;
		}
		public K getKey()
		{
			return key;
		}
		public V getValue()
		{
			return value;
		}

	}
	@Override
	public Iterable<K> keys() {
		Queue<K> queue = new Queue<K>();
		for (int i = 0; i < capacidad; i++) 
		{
			for (Nodo x = arreglo[i]; x != null; x = x.next) 
			{
				queue.enqueue((K) x.key);
			}
		}
		return (Iterable<K>) queue;
	} 
}
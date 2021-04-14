package model.data_structures;

import java.util.Iterator;


public class Queue<T  > implements IQueue<T> 
{
	private Nodo<T> primero;
	private Nodo<T> ultimo;
	private int tamano; 

	public Queue ()
	{
		primero=null;
		tamano=0;
	}

	public Iterator<T> iterator() 
	{
		return new IteratorLista<T>(primero);
	}

	@Override
	public boolean isEmpty() 
	{
		return primero==null;
	}

	public T darElem(int i)
	{
		Nodo<T> actual=primero;
		int x=1;
		while (x!=i)
		{
			actual=actual.getNext();
			x++;
		}
		return actual.getElement();
	}

	@Override
	public int size() 
	{
		return tamano;
	}

	@Override
	public void enqueue(T t) 
	{
		if(primero==null)
		{
			primero=new Nodo<T>(t);
			ultimo = primero;
		}

		else 
		{
			Nodo<T>dato = new Nodo<T>(t);
			ultimo.setNext(dato);
			ultimo = ultimo.getNext();
		}

		tamano++;
	}

	@Override
	public T dequeue() 
	{
		Nodo<T> eliminado=primero;
		primero=primero.getNext();
		tamano--;
		return eliminado.getElement();
	}

}

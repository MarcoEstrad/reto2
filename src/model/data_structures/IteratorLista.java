package model.data_structures;



import java.util.Iterator;

public class IteratorLista<T> implements Iterator
{

	private Nodo<T> proximo; 

	private Nodo<T> ant_prox; 

	public IteratorLista(Nodo<T> primero)
	{
		proximo = primero; 
	}

	public boolean hasNext() {
		// TODO Auto-generated method stub
		return next() != null; 
	}

	public T next() 
	{
		// TODO Auto-generated method stub
		T retornar = proximo.getElement();
		ant_prox = proximo;
		proximo = proximo.getNext(); 
		return retornar; 
	}
}

package model.data_structures;

import java.util.Iterator;
public class Nodo<T> {

	public Nodo<T> next;
	
	private T element;
	
	public Nodo(T element) {
		this.element = element;
	}

	public Nodo<T> getNext() {
		return next;
	}
	
	public T getElement() {
		return element;
	}

	public void setElement(T elemnt) {
		this.element = elemnt;
	}

	public void setNext(Nodo next) {
		this.next = next;
	}
	
	@Override
	public String toString() {
		return element.toString();
	}
}

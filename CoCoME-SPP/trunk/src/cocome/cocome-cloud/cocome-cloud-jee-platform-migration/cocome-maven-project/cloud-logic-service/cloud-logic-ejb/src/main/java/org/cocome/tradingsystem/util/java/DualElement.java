package org.cocome.tradingsystem.util.java;

public class DualElement<T, E> {
	private T elem1;
	private E elem2;
	
	
	public DualElement(T elem1, E elem2) {
		this.elem1 = elem1;
		this.elem2 = elem2;
	}
	
	public T getFirstElement() {
		return elem1;
	}
	
	public E getSecondElement() {
		return elem2;
	}
}

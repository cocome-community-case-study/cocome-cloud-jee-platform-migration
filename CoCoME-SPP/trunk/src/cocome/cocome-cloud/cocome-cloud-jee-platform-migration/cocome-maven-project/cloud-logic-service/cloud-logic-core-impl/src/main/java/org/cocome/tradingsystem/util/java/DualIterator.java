package org.cocome.tradingsystem.util.java;

import java.util.Collection;
import java.util.Iterator;

public class DualIterator<T, E> implements Iterable<DualElement<T, E>> {
	private Collection<T> coll1;
	private Collection<E> coll2;
	
	private Iterator<T> iterator1;
	private Iterator<E> iterator2;
	
	private T currElement1;
	private E currElement2;
	
	public DualIterator(Collection<T> coll1, Collection<E> coll2) {
		this.coll1 = coll1;
		this.coll2 = coll2;
		
		if (coll1 != null) {
			iterator1 = coll1.iterator();
		}
		
		if (coll2 != null) {
			iterator2 = coll2.iterator();
		}
	}
	
	@Override
	public Iterator<DualElement<T, E>> iterator() {
		return new Iterator<DualElement<T, E>>() {

			@Override
			public boolean hasNext() {
				if (coll1 == null || coll2 == null) return false;
				if (iterator1 == null || iterator2 == null) return false;
				
				return iterator1.hasNext() || iterator2.hasNext();
			}

			@Override
			public DualElement<T, E> next() {
				if (coll1 == null || coll2 == null) return null;
				if (iterator1 == null || iterator2 == null) return null;
				
				if (iterator1.hasNext()) {
					currElement1 = iterator1.next();
				}
				
				if (iterator2.hasNext()) {
					currElement2 = iterator2.next();
				}
				
				return new DualElement<T, E>(currElement1, currElement2);
			}

			@Override
			public void remove() {
				if (coll1 != null) {
					if (iterator1 != null) {
						iterator1.remove();
					};
				}
				
				if (coll2 != null) {
					if (iterator2 != null) {
						iterator2.remove();
					}
				}
			}
		};
	}

}

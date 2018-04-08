/*
 * @(#)Bag.java Copyright 2010 Zachary Davis. All rights reserved.
 */

package bbth.engine.util;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Set;

/**
 * A bag is an extremely high-performance {@link Collection} of elements that
 * foregoes the invariants of {@link Set} and {@link List}; specifically,
 * duplicates are allowed, and ordering is guaranteed only if
 * {@link #remove(int)} and {@link #remove(Object)} are not used.
 * 
 * @author Zach Davis
 * @param <E>
 *            the type of elements this Bag contains
 * @see Collection
 * @see Set
 * @see List
 */
public class Bag<E> extends AbstractCollection<E> implements RandomAccess,
		Cloneable, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	int numModifications;
	int numElements;
	int capacity;

	Object[] elements;

	/*
	 * Constructors
	 */

	/**
	 * Creates an empty bag with an initial capacity of 16 elements.
	 */
	public Bag() {
		this(16);
	}

	/**
	 * Creates a bag with the given initial capacity.
	 * 
	 * @param initialCapacity
	 *            the initial size of the array
	 */
	public Bag(int initialCapacity) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException(
					"Initial capacity was less than 1"); //$NON-NLS-1$

		numElements = 0;
		capacity = initialCapacity;
		elements = new Object[initialCapacity];
	}

	/**
	 * Creates a bag with an initial capacity equal to the size of
	 * {@link collection} and adds all of the elements it contains to the bag.
	 * 
	 * @param collection
	 *            the collection from which to add all the elements
	 */
	public Bag(Collection<? extends E> collection) {
		this(collection, collection.size());
	}

	/**
	 * Creates a bag with the given initial capacity and adds all of the
	 * elements of {@link collection} to the bag.
	 * 
	 * @param collection
	 *            the collection from which to add all the elements
	 * @param initialCapacity
	 *            the initial size of the array
	 */
	public Bag(Collection<? extends E> collection, int initialCapacity) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException(
					"Initial capacity was less than 1"); //$NON-NLS-1$
		int size = collection.size();
		if (initialCapacity < size)
			throw new IllegalArgumentException(
					"Initial capacity was less than given collection size"); //$NON-NLS-1$

		this.numElements = size;
		this.capacity = initialCapacity;

		if (size == initialCapacity) {
			elements = collection.toArray();
		} else {
			elements = collection.toArray(new Object[initialCapacity]);
		}
	}

	// TODO: ZD: Add constructors Bag(E[]) and Bag(E[], int) if useful someday

	/*
	 * Helper methods
	 */

	/**
	 * Ensures the <tt>Bag</tt> has room to add a given number of new elements
	 * 
	 * @param numNewElements
	 *            the number of new elements to ensure the <tt>Bag</tt> has room
	 *            for
	 */
	protected void accommodateNewElements(int numNewElements) {
		++numModifications;
		if (numNewElements + numElements > capacity) {
			int newCapacity = Math.max(capacity + numNewElements, capacity * 2);
			if (newCapacity > capacity) {
				elements = copyArray(elements, newCapacity);
				capacity = newCapacity;
			}
		}
	}

	/**
	 * Throws an {@link IndexOutOfBoundsException} if the given index is out of
	 * range.
	 * 
	 * @param index
	 */
	protected void ensureInBounds(int index) {
		if (index < 0 || index >= numElements)
			throw new IndexOutOfBoundsException(
					"index = " + index + ", numElements = " + numElements); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Removes the last element in the list
	 * 
	 * @return the removed element
	 */
	@SuppressWarnings("unchecked")
	protected E unsafeRemoveLast() {
		E ret = (E) elements[--numElements];
		elements[numElements] = null;
		return ret;
	}

	/*
	 * Mutative API methods
	 */

	/**
	 * Adds the given element to the end of the <tt>Bag</tt>. Runs in constant
	 * time. This method will not change the ordering of existing elements.
	 * 
	 * @param element
	 *            the element to add
	 * @return {@inheritDoc} (which, in this case, is always)
	 */
	@Override
	public boolean add(E element) {
		if (element == null)
			throw new NullPointerException();
		accommodateNewElements(1);
		elements[numElements++] = element;
		return true;
	}

	/**
	 * Adds all of the elements of the given collection to the end of the
	 * <tt>Bag</tt>. Implemented with {@link Collection#toArray()} and
	 * {@link System#arraycopy}; performance will depend on the efficiency with
	 * which <tt>toArray</tt> is implemented in the given collection's class.
	 * This method will not change the ordering of existing elements.
	 * 
	 * @param collection
	 *            {@inheritDoc}
	 * @return {@inheritDoc} (which, in this case, is always)
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		if (collection.isEmpty())
			return false;
		Object[] newElements = collection.toArray();
		int numNewElements = newElements.length;
		accommodateNewElements(numNewElements);
		System.arraycopy(newElements, 0, elements, numElements, numNewElements);
		numElements += numNewElements;
		return true;
	}

	/**
	 * Removes the first entry found to be equal ({@link #indexOf(Object)}).
	 * Runs in linear time with respect to the number of elements in this
	 * <tt>Bag</tt>. Note that this method <b>will</b> change the ordering of
	 * remaining elements.
	 * 
	 * @param object
	 *            {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean remove(Object object) {
		if (object == null)
			throw new NullPointerException();
		int index = indexOf(object);
		if (index != -1) {
			remove(index);
			return true;
		} else
			return false;
	}

	/**
	 * Removes the element at the given index. Runs in constant time. Note that
	 * this method <b>will</b> change the ordering of remaining elements.
	 * 
	 * @param index
	 *            the index from which to remove
	 * @return the element removed from the given index
	 */
	@SuppressWarnings("unchecked")
	public E remove(int index) {
		++numModifications;
		ensureInBounds(index);
		if (index == numElements - 1)
			return unsafeRemoveLast();
		else {
			E ret = (E) elements[index];
			elements[index] = unsafeRemoveLast();
			return ret;
		}
	}

	/**
	 * Removes the element at the end of this <tt>Bag</tt>. Runs in constant
	 * time. Note that this method will not change the ordering of remaining
	 * elements.
	 * 
	 * @return the element removed from the end of the <tt>Bag</tt>
	 */
	public E removeLast() {
		++numModifications;
		ensureInBounds(0);
		return unsafeRemoveLast();
	}

	/**
	 * Removes all elements from this <tt>Bag</tt>. Implemented with an array
	 * fill.
	 */
	@Override
	public void clear() {
		++numModifications;
		Arrays.fill(elements, 0, numElements, null);
		numElements = 0;
	}

	/*
	 * Non-mutative API methods
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return numElements == 0;
	}

	/**
	 * Returns the element located at the given index.
	 * 
	 * @param index
	 *            the index of the element to retrieve
	 * @return the element at the specified index
	 */
	@SuppressWarnings("unchecked")
	public E get(int index) {
		ensureInBounds(index);
		return (E) elements[index];
	}

	/**
	 * Returns the first element in this <tt>Bag</tt>.
	 * 
	 * @return the element at index 0, or <tt>null</tt> if the
	 */
	@SuppressWarnings("unchecked")
	public E getFirst() {
		if (numElements < 1)
			return null;
		return (E) elements[0];
	}

	/**
	 * Returns the last element in this <tt>Bag</tt>.
	 * 
	 * @return the element at <tt>size() - 1</tt>, or <tt>null</tt> if the
	 */
	@SuppressWarnings("unchecked")
	public E getLast() {
		if (numElements < 1)
			return null;
		return (E) elements[numElements - 1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return copyArray(elements, numElements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] array) {
		int expectedNumModifications = numModifications;
		if (array.length < numElements) {
			array = copyArray(array, numElements);
		}
		System.arraycopy(elements, 0, array, 0, numElements);
		if (array.length > numElements) {
			Arrays.fill(array, numElements, array.length, null);
		}
		if (expectedNumModifications != numModifications)
			throw new ConcurrentModificationException();
		return array;
	}

	/**
	 * Returns the lowest index which contains a, or -1
	 * 
	 * @param object
	 *            the object for which to search
	 * @return the first index found to contain an object equal to the one given
	 */
	public int indexOf(Object object) {
		int numElements = this.numElements;
		for (int i = 0; i < numElements; ++i) {
			if (elements[i].equals(object))
				return i;
		}
		return -1;
	}

	/**
	 * Reverses the elements of this Bag in-place
	 */
	public void reverse() {
		int numElementsToReverse = this.numElements / 2;
		for (int i = 0, j = numElements - 1; i < numElementsToReverse; ++i, --j) {
			Object temp = elements[j];
			elements[j] = elements[i];
			elements[i] = temp;
		}
	}

	/**
	 * Returns a "shallow" copy of this <tt>Bag</tt>.
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Bag<E> clone() {
		int expectedNumModifications = numModifications;

		try {
			@SuppressWarnings("unchecked")
			Bag<E> ret = (Bag<E>) super.clone();
			ret.elements = copyArray(elements, capacity);

			if (expectedNumModifications != numModifications)
				throw new ConcurrentModificationException();

			return ret;
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return numElements;
	}

	BagIterator sharedIterator = new BagIterator();

	/**
	 * {@inheritDoc Iterable#iterator()} <b>WARNING!</b> <tt>Bag</tt>'s
	 * implementation returns a single shared instance that is simply reset
	 * every time this is called. If this <tt>Bag</tt> is being iterated over
	 * while being iterated over, use {@link #newIterator()} to construct a new,
	 * dedicated <tt>Iterator</tt>.
	 */
	@Override
	public Iterator<E> iterator() {
		sharedIterator.reset();
		return sharedIterator;
	}

	/**
	 * Returns a new iterator (that is, not the shared instance) over the
	 * elements in this <tt>Bag</tt>.
	 * 
	 * @return a new iterator over the elements in this <tt>Bag</tt>
	 */
	public Iterator<E> newIterator() {
		return new BagIterator();
	}

	protected class BagIterator implements Iterator<E> {
		int currentIndex;
		int myNumModifications;
		boolean lastOpWasRemove;

		BagIterator() {
			myNumModifications = numModifications;
		}

		void reset() {
			currentIndex = 0;
			myNumModifications = numModifications;
			lastOpWasRemove = false;
		}

		void ensureUnmodified() {
			if (myNumModifications != numModifications)
				throw new ConcurrentModificationException();
		}

		@Override
		public boolean hasNext() {
			ensureUnmodified();
			return currentIndex < numElements;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			ensureUnmodified();
			if (currentIndex < numElements) {
				lastOpWasRemove = false;
				return (E) elements[currentIndex++];
			} else
				throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			ensureUnmodified();
			if (lastOpWasRemove)
				throw new IllegalStateException(
						"BagIterator.remove() called twice in a row without a call to next"); //$NON-NLS-1$
			if (currentIndex == 0)
				throw new IllegalStateException(
						"BagIterator.remove() called before BagIterator.next()"); //$NON-NLS-1$
			Bag.this.remove(--currentIndex);
			myNumModifications = numModifications;
		}

	}

	/**
	 * Bag simply performs a defaultWriteObject(), but ensures that it was not
	 * modified while it was being written.
	 */
	private void writeObject(java.io.ObjectOutputStream s)
			throws java.io.IOException {
		int expectedNumModifications = numModifications;

		s.defaultWriteObject();

		if (expectedNumModifications != numModifications) {
			throw new ConcurrentModificationException();
		}
	}

	// Added because Android's java.util.Arrays sucks (doesn't have the copyOf
	// method)
	static <T> T[] copyArray(T[] original, int newLength) {
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) ((original.getClass() == Object[].class) ? new Object[newLength]
				: Array.newInstance(original.getClass().getComponentType(),
						newLength));
		System.arraycopy(original, 0, newArray, 0,
				original.length < newLength ? original.length : newLength);
		return newArray;
	}
}

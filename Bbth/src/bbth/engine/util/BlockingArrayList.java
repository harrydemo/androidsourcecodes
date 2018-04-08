package bbth.engine.util;

import java.util.ArrayList;

public class BlockingArrayList<E> {

	private ArrayList<E> _backingList;
	private boolean _locked;
	private int size;

	public BlockingArrayList() {
		_backingList = new ArrayList<E>();
	}

	public synchronized void add(E o) {
		while (_locked) {
		}
		_locked = true;
		_backingList.add(o);
		size++;
		_locked = false;
	}

	public synchronized boolean remove(E o) {
		while (_locked) {
		}
		_locked = true;
		size--;
		_locked = false;
		return _backingList.remove(o);
	}

	public synchronized E get(int index) {
		while (_locked) {
		}
		_locked = true;
		E to_return = _backingList.get(index);
		_locked = false;
		return to_return;
	}

	public synchronized int size() {
		while (_locked) {

		}
		_locked = true;
		int to_return = size;
		_locked = false;
		return to_return;
	}

	public synchronized boolean contains(E o) {
		while (_locked) {

		}
		_locked = true;
		boolean to_return = _backingList.contains(o);
		_locked = false;
		return to_return;
	}

}

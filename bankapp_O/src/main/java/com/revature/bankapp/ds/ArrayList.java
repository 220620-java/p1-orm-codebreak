package com.revature.bankapp.ds;

import java.util.Arrays;

public class ArrayList<T> implements List<T> {
	private T[] array;
	private int size;
	
	@SuppressWarnings("unchecked")
	public ArrayList() {
		this.size = 0;
		this.array = (T[]) new Object[10];
	}
	
	public ArrayList(T...objects) {
		this.array = objects;
	}

	public void add(T obj) {
		this.array[size] = obj;
		size++;
	}

	public T get(int index) {
		if (index >= 0 && index < this.size) {
			return this.array[index];
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public int indexOf(T obj) {
		for (int i = 0; i < this.size; i++) {
			if (obj==null) {
				if (this.array[i]==null)
					return i;
			} else if (obj.equals(this.array[i])) {
				return i;
			}
		}
		return -1;
	}

	public boolean contains(T obj) {
		for (T element : this.array) {
			if (obj==null) {
				if (element==null)
					return true;
			} else if (obj.equals(element)) {
				return true;
			}
		}
		return false;
	}

	public T remove(int index) {
		if (index >= 0 && index < this.array.length) {
			T value = this.array[index];
			for (int i=index; i < this.array.length-1; i++) {
				this.array[i] = this.array[i+1];
			}
			this.array[this.array.length-1]=null;
			this.size--;
			return value;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public int size() {
		return this.size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(array);
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayList other = (ArrayList) obj;
		if (!Arrays.deepEquals(array, other.array))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArrayList [array=" + Arrays.toString(array) + ", size=" + size + "]";
	}

	
}

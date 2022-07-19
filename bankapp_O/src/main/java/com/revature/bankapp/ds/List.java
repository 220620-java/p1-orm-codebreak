package com.revature.bankapp.ds;

public interface List<T> {

	// adds an element to the end of the list

	// @param t the object to be added into the list
	public void add(T t);

	// retrieves an element from a specified index

	// @param index the index of the desired element
	// @return the index of the specific object in the list
	public T get(int index);

	// returns the index of the first instance of the specified object in the list.
	// The
	// .equals method is used to determine whether the object is the one that is
	// being searched for
	public int indexOf(T t);

	// returns whether a specified object exists in the list.
	// the .equals method is used for this

	// @param t the object being searched for
	// @return true if the object is in the list, false otherwise
	public boolean contains(T t);

	// removes the object at the specified index from the list and returns the
	// object

	// @param index the index of the object to be removed
	// @return the object that was removed
	public T remove(int index);

	// returns the number of elements currently in the list

	// @return the number of elements in the list
	public int size();
}

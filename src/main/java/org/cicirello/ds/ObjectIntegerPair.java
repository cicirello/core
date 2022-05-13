/*
 * Module org.cicirello.core
 * Copyright 2019-2022 Vincent A. Cicirello, <https://www.cicirello.org/>.
 *
 * This file is part of module org.cicirello.core.
 *
 * Module org.cicirello.core is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 *
 * Module org.cicirello.core is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with module org.cicirello.core.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cicirello.ds;

/**
 * An instance of this class encapsulates an object with a corresponding
 * integer value, such as but not limited to a priority value for a priority queue.
 * This class is used by several classes of the library.
 *
 * @param <E> The type of object contained in the ObjectIntegerPair.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class ObjectIntegerPair<E> {
	
	// package-private access on purpose
	final E element;
	int value;
	
	/**
	 * Initializes an ObjectIntegerPair.
	 *
	 * @param element The object.
	 * @param value The value of the object.
	 */
	public ObjectIntegerPair(E element, int value) {
		this.element = element;
		this.value = value;
	}
	
	/**
	 * Checks if another ObjectIntegerPair is equal to this one.
	 *
	 * @param other The other ObjectIntegerPair.
	 *
	 * @return true if and only if they contain an identical object, determined by
	 * the equals method of the encapsulated object, and also the same value.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		// After upgrade to Java 17, change following to: other instanceof ObjectIntegerPair<E>
		if (other instanceof ObjectIntegerPair) {
			@SuppressWarnings("unchecked")
			ObjectIntegerPair<E> casted = (ObjectIntegerPair<E>)other;
			return value == casted.value && element.equals(casted.element);
		}
		return false;
	}
	
	/**
	 * Gets the encapsulated object.
	 *
	 * @return The object within the pair.
	 */
	public E getObject() {
		return element;
	}
	
	/**
	 * Computes the hashCode of the ObjectIntegerPair.
	 *
	 * @return a hashCode
	 */
	@Override
	public int hashCode() {
		return element.hashCode() * 31 + value;
	}
	
	/**
	 * Gets the integer value.
	 *
	 * @return the integer value.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Changes the integer value.
	 *
	 * @param value The new value of the object.
	 */
	public void setValue(int value) {
		this.value = value;
	}
}

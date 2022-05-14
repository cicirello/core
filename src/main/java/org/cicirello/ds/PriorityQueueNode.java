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
 * An instances of the nested subclasses of this class encapsulate an (element, priority) pair
 * for use by the various priority queue classes of the library.
 *
 * @param <E> The type of element contained in the PriorityQueueNode.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public abstract class PriorityQueueNode<E> {
	
	// package-private access on purpose
	final E element;
	
	/*
	 * private
	 */
	private PriorityQueueNode(E element) {
		this.element = element;
	}
	
	/**
	 * Checks if another PriorityQueueNode is equal to this one.
	 *
	 * @param other The other PriorityQueueNode.
	 *
	 * @return true if and only if they contain an identical object, determined by
	 * the equals method of the encapsulated object.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		// After upgrade to Java 17, change following to: other instanceof PriorityQueueNode<E>
		if (other instanceof PriorityQueueNode) {
			@SuppressWarnings("unchecked")
			PriorityQueueNode<E> casted = (PriorityQueueNode<E>)other;
			return element.equals(casted.element);
		}
		return false;
	}
	
	/**
	 * Gets the encapsulated object.
	 *
	 * @return The object within the pair.
	 */
	public final E getElement() {
		return element;
	}
	
	/**
	 * Computes the hashCode of the PriorityQueueNode.
	 *
	 * @return a hashCode
	 */
	@Override
	public int hashCode() {
		return element.hashCode();
	}
	
	/**
	 * An instance of this class encapsulates an (element, priority) pair
	 * for an integer valued priority. This class is used by the various
	 * priority queue classes of the library.
	 *
	 * @param <E> The type of element contained in the PriorityQueueNode.
	 *
	 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
	 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
	 */
	public static final class Integer<E> extends PriorityQueueNode<E> {
		
		// package-private on purpose for use by 
		// priority queue classes in the package.
		int value;
		
		/**
		 * Initializes the PriorityQueueNode.Integer.
		 *
		 * @param element The object.
		 * @param value The value of the object.
		 */
		public Integer(E element, int value) {
			super(element);
			this.value = value;
		}
		
		/**
		 * Checks if another PriorityQueueNode.Integer is equal to this one.
		 *
		 * @param other The other PriorityQueueNode.Integer.
		 *
		 * @return true if and only if they contain an identical object, determined by
		 * the equals method of the encapsulated object, as well as the same priority.
		 */
		@Override
		public boolean equals(Object other) {
			return super.equals(other) && ((Integer)other).value == value;
		}
		
		/**
		 * Computes the hashCode of the PriorityQueueNode.Integer.
		 *
		 * @return a hashCode
		 */
		@Override
		public int hashCode() {
			return super.hashCode() * 31 + value;
		}
		
		/**
		 * Gets the priority value.
		 *
		 * @return the priority value.
		 */
		public int getPriority() {
			return value;
		}
		
		/*
		 * package-private: It would be dangerous to allow
		 * priority changes external to the priority queue classes
		 */
		final void setValue(int value) {
			this.value = value;
		}
	}
}
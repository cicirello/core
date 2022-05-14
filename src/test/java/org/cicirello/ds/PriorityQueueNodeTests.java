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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the PriorityQueueNode class.
 */
public class PriorityQueueNodeTests {
	
	@Test
	public void testIntegerNode() {
		PriorityQueueNode.Integer<String> node1 = new PriorityQueueNode.Integer<String>("hello", 42);
		assertEquals("hello", node1.getElement());
		assertEquals(42, node1.getPriority());
		PriorityQueueNode.Integer<String> node2 = new PriorityQueueNode.Integer<String>("hello", 42);
		assertEquals("hello", node2.getElement());
		assertEquals(42, node2.getPriority());
		PriorityQueueNode.Integer<String> node3 = new PriorityQueueNode.Integer<String>("hello", 43);
		assertEquals("hello", node3.getElement());
		assertEquals(43, node3.getPriority());
		PriorityQueueNode.Integer<String> node4 = new PriorityQueueNode.Integer<String>("world", 42);
		assertEquals("world", node4.getElement());
		assertEquals(42, node4.getPriority());
		assertEquals(node1, node2);
		assertEquals(node1.hashCode(), node2.hashCode());
		assertNotEquals(node1, node3);
		assertNotEquals(node1, node4);
		assertNotEquals(node1.hashCode(), node3.hashCode());
		assertNotEquals(node1.hashCode(), node4.hashCode());
		assertFalse(node1.equals(null));
		assertFalse(node1.equals("hello"));
		node1.setPriority(55);
		assertEquals("hello", node1.getElement());
		assertEquals(55, node1.getPriority());
		assertNotEquals(node1, node2);
	}
	
	@Test
	public void testDoubleNode() {
		PriorityQueueNode.Double<String> node1 = new PriorityQueueNode.Double<String>("hello", 42.0);
		assertEquals("hello", node1.getElement());
		assertEquals(42.0, node1.getPriority(), 0.0);
		PriorityQueueNode.Double<String> node2 = new PriorityQueueNode.Double<String>("hello", 42.0);
		assertEquals("hello", node2.getElement());
		assertEquals(42.0, node2.getPriority(), 0.0);
		PriorityQueueNode.Double<String> node3 = new PriorityQueueNode.Double<String>("hello", 43.0);
		assertEquals("hello", node3.getElement());
		assertEquals(43.0, node3.getPriority(), 0.0);
		PriorityQueueNode.Double<String> node4 = new PriorityQueueNode.Double<String>("world", 42.0);
		assertEquals("world", node4.getElement());
		assertEquals(42.0, node4.getPriority(), 0.0);
		assertEquals(node1, node2);
		assertEquals(node1.hashCode(), node2.hashCode());
		assertNotEquals(node1, node3);
		assertNotEquals(node1, node4);
		assertNotEquals(node1.hashCode(), node3.hashCode());
		assertNotEquals(node1.hashCode(), node4.hashCode());
		assertFalse(node1.equals(null));
		assertFalse(node1.equals("hello"));
		node1.setPriority(55.0);
		assertEquals("hello", node1.getElement());
		assertEquals(55.0, node1.getPriority(), 0.0);
		assertNotEquals(node1, node2);
	}
}

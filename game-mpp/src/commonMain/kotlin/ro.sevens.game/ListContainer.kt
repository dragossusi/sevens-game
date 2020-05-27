package ro.sevens.game


/**
 * Sevens
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * Sevens is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * sSevens is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sevens.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
interface ListContainer<T> : List<T> {

    val items: List<T>

    override val size: Int
        get() = items.size

    override fun contains(element: T): Boolean {
        return items.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return items.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    override fun iterator(): Iterator<T> {
        return items.iterator()
    }

    override fun get(index: Int): T {
        return items[index]
    }

    override fun indexOf(element: T): Int {
        return items.indexOf(element)
    }

    override fun lastIndexOf(element: T): Int {
        return items.lastIndexOf(element)
    }

    override fun listIterator(): ListIterator<T> {
        return items.listIterator()
    }

    override fun listIterator(index: Int): ListIterator<T> {
        return items.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<T> {
        return items.subList(fromIndex, toIndex)
    }

}
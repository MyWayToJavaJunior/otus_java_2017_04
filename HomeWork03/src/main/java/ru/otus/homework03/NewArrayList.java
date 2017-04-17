package ru.otus.homework03;

import java.util.*;
import java.util.function.Consumer;

public class NewArrayList<E> implements List<E> {
    private int MAX_INC_SIZE = 100;

    private Object[] innerArr;
    private int actualSize = 0;


    public NewArrayList() {
        innerArr = new Object[1];
    }

    private void increaseSize(boolean useLen, int len) {
        if (actualSize == innerArr.length) {
            int newSize = (actualSize * 2 > MAX_INC_SIZE) ? MAX_INC_SIZE : (actualSize * 2);
            newSize = useLen ? (actualSize + len) : newSize;

            Object[] newInnerArr = Arrays.copyOf(innerArr, newSize);

            innerArr = newInnerArr;
        }
    }

    @Override
    public E get(int index) {
        return (E)innerArr[index];
    }

    @Override
    public int size() {
        return actualSize;
    }

    @Override
    public boolean add(E e) {
        increaseSize(false, 0);
        actualSize++;
        innerArr[actualSize - 1] = e;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null || c.isEmpty()) return true;
        increaseSize(true, c.size());
        actualSize++;
        c.forEach(o -> innerArr[actualSize - 1] = o);
        return true;
    }

    @Override
    public E set(int index, E element) {
        E old = (E) innerArr[index];
        innerArr[index] = element;

        return old;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (int i = 0; i < actualSize; i++) {
            action.accept((E)innerArr[i]);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(innerArr, 0, actualSize));

    }

    @Override
    public boolean isEmpty() {
        return actualSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(innerArr, 0, actualSize);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        actualSize = 0;
        innerArr = new Object[1];
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new NewListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private class NewListIterator implements ListIterator<E> {
        private int current = -1;

        @Override
        public boolean hasNext() {
            return current != actualSize - 1;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            current++;
            return (E) innerArr[current];
        }

        @Override
        public boolean hasPrevious() {
            return current != 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            current--;
            return (E) innerArr[current];
        }

        @Override
        public int nextIndex() {
            return current + 1;
        }

        @Override
        public int previousIndex() {
            return current - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            innerArr[current] = e;
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }
}

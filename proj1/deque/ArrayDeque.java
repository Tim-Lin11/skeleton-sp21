package deque;

import org.junit.Test;

import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    @SuppressWarnings("unchecked")
    private T[] Array =(T[]) new Object[8];
    private int size;
    private int capacity;

    public ArrayDeque(){
        size=0;
        capacity=8;
    }


    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T>{
        private int Index=0;

        @Override
        public boolean hasNext() {
            return Index<size;
        }

        @Override
        public T next() {
            if(hasNext()){
                T re = get(Index);
                Index++;
                return re;
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean equals(Object o){
        if (o==null) { return false; }
        else if (!(o instanceof Deque)) { return false; }
        ArrayDeque<?> list = (ArrayDeque<?>) o;
        if(size!=list.size()) { return false; }
        for(int i=0;i<size;i++){
            if(get(i)!=list.get(i)){
                return false;
            }
        }
        return true;
    }

    private void shrinksize(){
        if(size==0){
            resize(8);
        } else if (capacity/4 > size && size>8) {
            resize(capacity/4);
        }
    }


    private void resize(int Capacity){
        @SuppressWarnings("unchecked")
        T[] Brray = (T[]) new Object[Capacity];
        System.arraycopy(Array,0,Brray,0,Capacity/2);
        Array=Brray;
        capacity=Capacity;
    }

    @Override
    public void addFirst(T item){
        if(size==capacity){
            resize(capacity*2);
        }
        if(size>0) {
            System.arraycopy(Array, 0, Array, 1, size);
        }
        Array[0]=item;
        size++;
    }

    @Override
    public void addLast(T item){
        if(size==capacity){
            resize(capacity*2);
        }
        Array[size] = item;
        size++;
    }

    @Override
    public T removeFirst(){
        if (size!=0){
            T re = Array[0];
            System.arraycopy(Array,1, Array,0,size-1);
            size--;
            return re;
        } else {
            return null;
        }
    }

    @Override
    public T removeLast() {
        if (size != 0) {
            T re =Array[size-1];
            Array[size-1] = null;
            size--;
            return re;
        } else {
            return null;
        }
    }


    @Override
    public void printDeque(){
        for(int i=0; i<size;i++){
            System.out.print(Array[i]+" ");
        }
        System.out.println("\n");
    }

    @Override
    public T get(int Index){
        return Array[Index];
    }

    @Override
    public int size(){
        return size;
    }
}

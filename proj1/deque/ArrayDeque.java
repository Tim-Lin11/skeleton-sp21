package deque;

import java.io.ObjectStreamException;
import java.lang.reflect.Array;

public class ArrayDeque<T> implements Deque<T> {
    private int size;
    private int capacity;
    private T[] Array;

    public ArrayDeque(){
        @SuppressWarnings("unchecked")
        T[] Array =(T[]) new Object[8];
        this.Array=Array;
        size=0;
        capacity=8;
    }

    public void resize(int Capacity){
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
    public boolean isEmpty(){
        return size==0;
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

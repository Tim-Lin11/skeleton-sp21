package deque;

import java.io.ObjectStreamException;
import java.lang.reflect.Array;

public class ArrayDeque<T> {
    private int size;
    private int capacity;
    private T[] Array;

    public ArrayDeque(){
        T[] Array =(T[]) new Object[8];
        size=0;
        capacity=8;
    }

    public void resize(int Capacity){
        T[] Brray = (T[]) new Object[Capacity];
        System.arraycopy(Array,0,Brray,0,Capacity);
        Array=Brray;
        capacity=Capacity;
    }

    public void addFirst(T item){
        if(size==capacity){
            resize(capacity*2);
        }
        T[] Brray =(T[]) new Object[capacity];
        Brray[0]=item;
        System.arraycopy(Array,0,Brray,1,size);
        Array=Brray;
        size++;
    }

    public void addLast(T item){
        if(size==capacity){
            resize(capacity*2);
        }
        Array[size] = item;
        size++;
    }

    public Object removeFirst(){
        if (size!=0){
            T re = Array[0];
            T[] Brray =(T[]) new Object[capacity];
            System.arraycopy(Array,1,Brray,0,size);
            Array=Brray;
            size--;
            return re;
        } else {
            return null;
        }
    }

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

    public boolean isEmpty(){
        return size==0;
    }

    public void printDeque(){
        for(int i=0; i<size;i++){
            System.out.print(Array[i]);
        }
        System.out.print("\n");
    }

    public Object get(int Index){
        return Array[Index];
    }

    public int size(){
        return size;
    }

}

package deque;

import java.lang.reflect.Array;

public class ArrayDeque {
    private int size;
    private int capacity;
    private Object[] Array;

    public ArrayDeque(){
        Object[] Array = new Object[8];
        size=0;
        capacity=8;
    }

    

    public void addLast(Object item){
        Array[size] = item;
        size++;
    }

    public void removeLast() {
        if (size != 0) {
            Array[size] = null;
            size--;
        }
    }

    public Object get(int Index){
        return Array[Index];
    }

    public int size(){
        return size;
    }



}

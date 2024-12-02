package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addint(){
        ArrayDeque<Integer> intlist = new ArrayDeque<>();
        intlist.addFirst(1);
        intlist.addFirst(2);
        assertEquals(intlist.removeFirst(),Integer.valueOf(2));
        assertEquals(intlist.removeFirst(),Integer.valueOf(1));
    }

    @Test
    public void addStr(){
        ArrayDeque<String> strlist = new ArrayDeque<>();
        strlist.addFirst("c");
        strlist.addLast("s");
        assertEquals(strlist.removeFirst(),"c");
        assertEquals(strlist.removeFirst(),"s");
    }

    @Test
    public void add1000(){
        ArrayDeque<Integer> intlist = new ArrayDeque<>();
        assertEquals("size should be 0",0, intlist.size());
        for (int i=0;i<1000;i++){
            intlist.addLast(i);
        }
        assertEquals("should be 1000",1000,intlist.size());
        for(int i=1000;i>0;i--){
            intlist.removeFirst();
        }
        assertEquals("should be 0",0,intlist.size());
    }

    @Test
    public void printtest(){
        ArrayDeque<String> strlist = new ArrayDeque<>();
        strlist.addFirst("c");
        strlist.addLast("s");
        strlist.addLast("6");
        strlist.addLast("1");
        strlist.addLast("B");
        strlist.printDeque();
        System.out.print("Test");
    }

    @Test
    public void gettest(){
        ArrayDeque<String> strlist = new ArrayDeque<>();
        strlist.addFirst("c");
        strlist.addLast("s");
        strlist.addLast("6");
        strlist.addLast("1");
        strlist.addLast("B");
        assertEquals("should be c","c",strlist.get(0));
        assertEquals("should be s","s",strlist.get(1));
        ArrayDeque<Integer> intlist = new ArrayDeque<>();
        intlist.addFirst(1);
        intlist.addFirst(2);
        assertEquals("should be 1",Integer.valueOf(2),intlist.get(0));
    }


    @Test
    public void Iteratortest(){
        ArrayDeque<Integer> intlist = new ArrayDeque<>();
        intlist.addFirst(3);
        intlist.addFirst(2);
        intlist.addFirst(1);
        for(int i : intlist){
            System.out.print(i);
        }
    }

    @Test
    public void randomizedTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        int N = 1000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                arrayDeque.addFirst(randVal);
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                arrayDeque.addLast(randVal);
            } else if (arrayDeque.size() == 0) {
                assertTrue(arrayDeque.isEmpty());
            } else if (operationNumber == 2) {
                assertTrue(arrayDeque.size() > 0);
            } else if (operationNumber == 3) {
                arrayDeque.removeFirst();
            } else if (operationNumber == 4) {
                arrayDeque.removeLast();
            } else if (operationNumber == 5) {
                int randIndex = StdRandom.uniform(0, arrayDeque.size());
                arrayDeque.get(randIndex);
            }
        }
    }
}

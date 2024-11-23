package deque;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addint(){
        ArrayDeque<Integer> intlist = new ArrayDeque<>();
        intlist.addFirst(1);
        intlist.addFirst(2);
        assertEquals(intlist.removeFirst(),2);
        assertEquals(intlist.removeFirst(),1);
    }

    @Test
    public void addStr(){
        ArrayDeque<String> strlist = new ArrayDeque<>();
        strlist.addFirst("c");
        strlist.addLast("s");
        assertEquals(strlist.removeFirst(),"c");
        assertEquals(strlist.removeFirst(),"s");
    }
}

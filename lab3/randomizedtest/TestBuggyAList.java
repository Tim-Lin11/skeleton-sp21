package randomizedtest;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> AList = new AListNoResizing<>();
        BuggyAList<Integer> BList = new BuggyAList<>();

        AList.addLast(4);
        BList.addLast(4);
        AList.addLast(5);
        BList.addLast(5);
        AList.addLast(6);
        BList.addLast(6);

        AList.removeLast();
        BList.removeLast();
        assertEquals(AList.getLast(), BList.getLast());
    }


    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> BL = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                BL.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size_bug = BL.size();
                assertEquals(size,size_bug);
                System.out.println("size: " + size);
            } else if(operationNumber ==2){
                if(L.size()!=0){
                    int output = L.getLast();
                    int output_bug = BL.getLast();
                    assertEquals(output_bug,output);
                    System.out.println("getLast("+output+")");
                }
            }else if(operationNumber==3){
                if(L.size()!=0){
                    int output = L.removeLast();
                    int output_bug = BL.removeLast();
                    assertEquals(output,output_bug);
                    System.out.println("removeLast("+output+")");
                }
            }
        }
    }
}

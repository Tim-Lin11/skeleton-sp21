package deque;

import org.junit.Assert;
import org.junit.Test;
import java.util.Comparator;
import org.junit.Assert.*;

public class MaxArrayDequeTest {

    @Test
    public void simp1eTest(){
        Comparator<Integer> c = (a,b) -> a-b;

        MaxArrayDeque<Integer> intlist = new MaxArrayDeque<>(c);
        Assert.assertEquals("size should be 0",0,intlist.size());
        intlist.addFirst(1);
        intlist.addFirst(2);
        intlist.addFirst(3);
        Assert.assertEquals("should be 3",Integer.valueOf(3),intlist.max());
        Assert.assertEquals("size should be 3",3,intlist.size());
    }

    @Test
    public void simp1eTest_re(){
        Comparator<Integer> c = (a,b) -> b-a;

        MaxArrayDeque<Integer> intlist = new MaxArrayDeque<>(c);
        intlist.addFirst(1);
        intlist.addFirst(2);
        intlist.addFirst(3);
        Assert.assertEquals("should be 1",Integer.valueOf(1),intlist.max());
        Comparator<Integer> c_re = (a,b) -> a-b;
        int result = intlist.max(c_re);
        Assert.assertEquals("should be 3",3,result);
    }

    @Test
    public void StringTest(){
        Comparator<String> length = this::string_length;
        Comparator<String> alphabet = this::string_alphabet;
        MaxArrayDeque<String> strlist = new MaxArrayDeque<>(length);
        strlist.addLast("cs61b");
        strlist.addLast("is");
        strlist.addLast("a");
        strlist.addLast("fun");
        strlist.addLast("lecture");
        Assert.assertEquals("should be lecture","lecture",strlist.max());
        Assert.assertEquals("should be a","a",strlist.max(alphabet));
    }

    public int string_length(String a, String b){
        return a.length()-b.length();
    }

    public int string_alphabet(String a,String b){
        int alength = a.length();
        int blength = b.length();
        int length = Math.min(alength,blength);
        for(int i=0;i<length;i++){
            int a_i=a.charAt(i);
            int b_i=b.charAt(i);
            if(a_i!=b_i){
                return b_i-a_i;
            }
        }
        if(alength!=blength){
            return alength-blength;
        }
        return 0;
    }

    @Test
    public void testalphabet(){
        Assert.assertEquals(11,string_alphabet("a","lecture"));
    }
}

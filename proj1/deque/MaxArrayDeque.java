package deque;

import java.util.Comparator;

public class MaxArrayDeque<T>  extends ArrayDeque<T>{
    private Comparator<T> comparator;


    public MaxArrayDeque(Comparator<T> c){
        this.comparator = c;
    }

    public T max(){
        if(size()==0){
            return null;
        }
        int max_index = 0;
        for(int i=0;i<size();i++){
            if(comparator.compare(this.get(i),this.get(max_index))>0){
                max_index=i;
            }
        }
        return this.get(max_index);
    }

    public T max(Comparator<T> c){
        this.comparator=c;
        return max();
    }

}

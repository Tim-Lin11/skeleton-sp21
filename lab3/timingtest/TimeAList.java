package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        //creat N from 1000 to 128000
        AList<Integer> N_list= new AList<>();
        AList<Double> times_list = new AList<>();
        AList<Integer> op_list = new AList<>();
        //N_list
        for(int i=0;i<8;i++){
            N_list.addLast(1000*(int)Math.pow(2,i));
        }
        // times_list and op_list
        for(int i =1000;i<=128000;i*=2){
            AList<Integer> test_list = new AList<>();
            int op_num = 0;
            Stopwatch sw = new Stopwatch();
            for(int j=0;j<i;j++) {
                test_list.addLast(1);
                op_num++;
            }
            double time_in_second = sw.elapsedTime();
            times_list.addLast(time_in_second);
            op_list.addLast(op_num);
        }
        printTimingTable(N_list,times_list,op_list);

    }
}

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package CodeExercise
 * @Description: ForkJoinTask是一个抽象类，它还有两个抽象子类：RecusiveAction和RecusiveTask。
 * 其中RecusiveTask代表有返回值的任务，而RecusiveAction代表没有返回值的任务
 * @date 2020-11-16 23:03
 */
public class ForkJoinCalculator extends RecursiveTask<Long>  {
    private long[] numbers;
    private int from;
    private int to;
    private ForkJoinPool pool;

    public ForkJoinCalculator(long[] numbers, int from, int to) {
        this.numbers = numbers;
        this.from = from;
        this.to = to;
    }

    public ForkJoinCalculator() {
        this.pool = new ForkJoinPool();
    }

    public static void main(String[] args) {
        // 创建一个随机数组
        long[] numbers = LongStream.range(1, 20).toArray();
        System.out.println(Arrays.toString(numbers));
        ForkJoinCalculator calculator = new ForkJoinCalculator();

        long result=calculator.sumUp(numbers);
        System.out.println("累加之和:"+result);
    }

    @Override
    protected Long compute() {
        // 当要计算的小于6时，直接计算结果
        if (to - from < 6) {
            long total = 0;
            for (int i = from; i <= to; i++) {
                total += numbers[i];
            }
            System.out.println(String.format("currentThread:%s,total:%s,from:%s,to:%s",Thread.currentThread().getName(),total,from,to));
            return total;
        } else { // 把任务一分为二递归计算
            int middle = (from + to) / 2;
            ForkJoinCalculator left = new ForkJoinCalculator(numbers, from, middle);
            ForkJoinCalculator right = new ForkJoinCalculator(numbers, middle + 1, to);
            left.fork();
            right.fork();
            return left.join() + right.join();
        }
    }

    public long sumUp(long[] numbers) {
        return pool.invoke(new ForkJoinCalculator(numbers, 0, numbers.length-1));
    }

}

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
 *
 * fork()：开启一个新线程（或是重用线程池内的空闲线程），将任务交给该线程处理。 fork() 做的工作只有一件事，既是把任务推入当前工作线程的工作队列里。
 * join()：等待该任务的处理线程处理完毕，获得返回值。join() 的工作则复杂得多，也是 join() 可以使得线程免于被阻塞的原因——不像同名的 Thread.join()。
 * 并不是每个 fork() 都会促成一个新线程被创建，而每个 join() 也不是一定会造成线程被阻塞
 *
 * >>>>>>>>>>>> Fork/Join Framework 的实现算法 ：work stealing <<<<<<<<<<<<<<<
 * 1. ForkJoinPool 的每个工作线程都维护着一个工作队列（WorkQueue），这是一个双端队列（Deque），里面存放的对象是任务（ForkJoinTask）。
 * 2. 每个工作线程在运行中产生新的任务（通常是因为调用了 fork()）时，会放入工作队列的队尾，并且工作线程在处理自己的工作队列时，使用的是 LIFO 方式，也就是说每次从队尾取出任务来执行。
 * 3. 每个工作线程在处理自己的工作队列同时，会尝试窃取一个任务（或是来自于刚刚提交到 pool 的任务，或是来自于其他工作线程的工作队列），窃取的任务位于其他线程的工作队列的队首，也就是说工作线程在窃取其他工作线程的任务时，使用的是 FIFO 方式。
 * 4. 在遇到 join() 时，如果需要 join 的任务尚未完成，则会先处理其他任务，并等待其完成。
 * 5. 在既没有自己的任务，也没有可以窃取的任务时，进入休眠。
 * 所谓work-stealing模式，即每个工作线程都会有自己的任务队列。当工作线程完成了自己所有的工作后，就会去“偷”别的工作线程的任务
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

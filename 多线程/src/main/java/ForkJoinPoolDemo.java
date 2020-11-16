import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

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
 * @date 2020-11-16 22:48
 */
public class ForkJoinPoolDemo extends RecursiveAction {
    //定义一个分解任务的阈值为50,即一个任务最多承担50个工作量
    private int THRESHOLD = 50;
    // 任务量
    private int taskNum = 0;

    public ForkJoinPoolDemo(int taskNum) {
        this.taskNum = taskNum;
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建一个支持分解任务的线程池 ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinPoolDemo task = new ForkJoinPoolDemo(120);
        forkJoinPool.submit(task);
        // 等待20s，观察结果
        forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);
        forkJoinPool.shutdown();
    }

    @Override
    protected void compute() {
        if (taskNum <= THRESHOLD) {
            System.out.println(Thread.currentThread().getName()+"承担了"+taskNum+"份工作");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else { // 随机分解为两个任务
            Random random = new Random();
            int i = random.nextInt(THRESHOLD);

            ForkJoinPoolDemo left = new ForkJoinPoolDemo(i);
            ForkJoinPoolDemo rigth = new ForkJoinPoolDemo(taskNum - i);

            left.fork();
            rigth.fork();
        }
    }
}

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

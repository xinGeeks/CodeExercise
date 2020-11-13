import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package CodeExercise
 * @Description: three thread print 1-100 可重入锁
 * @date 2020-11-11 23:01
 */
public class ThreeThreadPrint_Lock {
    private static Lock lock = new ReentrantLock();
    private static int sequence = 1;
    private static int count = 1;

    public static void main(String[] args) throws InterruptedException {
        // 通过传入的self, next控制线程执行顺序
        new Thread(new PrintThread(1, 2), "线程1").start();
        Thread.sleep(100);
        new Thread(new PrintThread(2, 3), "线程2").start();
        Thread.sleep(100);
        new Thread(new PrintThread(3, 1), "线程3").start();
        Thread.sleep(100);
    }

    static class PrintThread implements Runnable {
        private int self;
        private int next;

        public PrintThread(int self, int next) {
            this.self = self;
            this.next = next;
        }

        @Override
        public void run() {
            while (count <= 100){ // 当count > 100时退出
                try {
                    lock.lock();
                    while (sequence == self) { // 控制线程执行顺序
                        while (count <= 100) {
                            System.out.println(Thread.currentThread().getName() + ":" + count);
                            break;
                        }
                        count++;
                        sequence = next; // 控制下个线程由谁执行
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}


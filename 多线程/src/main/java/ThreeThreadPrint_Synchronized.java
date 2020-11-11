import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package CodeExercise
 * @Description: three thread print 1-100
 * @date 2020-11-10 21:40
 */
public class ThreeThreadPrint_Synchronized {

    static AtomicInteger count = new AtomicInteger(1);

    static class ThreadPrinter implements Runnable {

        private String name;
        private Object prev;
        private Object self;

        private ThreadPrinter(String name, Object prev, Object self) {
            this.name = name;
            this.prev = prev;
            this.self = self;
        }

        @Override
        public void run() {
            while (count.getAcquire() < 101) {
                synchronized (prev) {        // 获取前一个的锁
                    synchronized (self) {    // 获取自己的锁
                        System.out.println(name + ":" + count.getAcquire());
                        count.incrementAndGet();
                        self.notifyAll();   // 唤醒其他线程竞争self锁
                    }

                    try {
                        if (count.getAcquire() == 101) {  // count == 100 最后一次打印操作，通过notifyAll操作释放对象锁
                            prev.notifyAll();
                        } else {
                            prev.wait();  // 否则释放prev锁
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public static void main(String[] args) throws InterruptedException {
            Object first = new Object();
            Object second = new Object();
            Object three = new Object();

            ThreadPrinter pa = new ThreadPrinter("线程1", three, first);
            ThreadPrinter pb = new ThreadPrinter("线程2", first, second);
            ThreadPrinter pc = new ThreadPrinter("线程3", second, three);

            // 保证初始启动顺序
            new Thread(pa).start();
            Thread.sleep(10);
            new Thread(pb).start();
            Thread.sleep(10);
            new Thread(pc).start();
            Thread.sleep(10);
        }
    }
}

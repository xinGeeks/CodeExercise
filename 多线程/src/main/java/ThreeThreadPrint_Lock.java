import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package CodeExercise
 * @Description: three thread print 1-100
 * @date 2020-11-11 23:01
 */
public class ThreeThreadPrint_Lock {
    private static Lock lock = new ReentrantLock();
    private static int sequence = 1;
    private static int count = 1;

    public static void main(String[] args) {
        new Thread(new firstThread(), "线程1").start();
        new Thread(new secondThread(), "线程2").start();
        new Thread(new threeThread(), "线程3").start();
    }

    static class firstThread implements Runnable {

        @Override
        public void run() {
            while (count < 99) {
                try {
                    lock.lock();
                    while (sequence == 1) {
                        System.out.println(Thread.currentThread().getName() + ":" + count);
                        count++;
                        sequence = 2;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class secondThread implements Runnable {

        @Override
        public void run() {
            while (count < 99) {
                try {
                    lock.lock();
                    while (sequence == 2) {
                        System.out.println(Thread.currentThread().getName() + ":" + count);
                        count++;
                        sequence = 3;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class threeThread implements Runnable {

        @Override
        public void run() {
            while (count < 99) {
                try {
                    lock.lock();
                    while (sequence == 3) {
                        System.out.println(Thread.currentThread().getName() + ":" + count);
                        count++;
                        sequence = 1;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}

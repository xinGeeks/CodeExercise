import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package CodeExercise
 * @Description:
 * @date 2020-11-12 20:52
 */
public class ThreeThreadPrint_Lock_Condition {


    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();

        new Thread(new SyncPrinter(lock, condition1, condition2), "线程1").start();
        Thread.sleep(50);
        new Thread(new SyncPrinter(lock, condition2, condition3), "线程2").start();
        Thread.sleep(50);
        new Thread(new SyncPrinter(lock, condition2, condition3), "线程3").start();

    }

    static class SyncPrinter implements Runnable {
        private Lock lock;
        private Condition selfCondition;
        private Condition nextCondition;
        private int count;

        public SyncPrinter(Lock lock, Condition selfCondition, Condition nextCondition) {
            this.lock = lock;
            this.selfCondition = selfCondition;
            this.nextCondition = nextCondition;
        }

        @Override
        public void run() {
            try {
                lock.lock(); // 进入临界区
                for (int i = 1; i <= 100; i++) {
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                    nextCondition.signalAll();
                    if (i == count) {
                        try {
                            selfCondition.await();
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                lock.unlock();
            }

        }
    }
}

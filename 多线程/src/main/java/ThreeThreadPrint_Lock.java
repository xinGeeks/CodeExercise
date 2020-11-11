import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package CodeExercise
 * @Description:
 * @date 2020-11-11 23:01
 */
public class ThreeThreadPrint_Lock {
    private static Lock lock = new ReentrantLock();
    private static AtomicInteger state = new AtomicInteger(0);

    class firstThread implements Runnable {

        @Override
        public void run() {


        }
    }
}

package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class SynchronizedTest {

    boolean isStarted;

    Map<String, TestObject> testObjectList;

    private class TestObject {
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    private class TestRunner implements Callable<Boolean> {

        int id;

        public TestRunner(int id) {
            this.id = id;
        }

        @Override
        public Boolean call() throws Exception {
            while (!isStarted)
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            // System.out.printf("Thread: %d\n", id);
            for (int i = 0; i < 10000000; i++) {
                {
                    TestObject x = testObjectList.get("Test1");
                    /*
                     * if id is not flushed to main memory then would expect eventually to get an error because would be
                     * reading back the results of the last write from this thread rather than another
                     */
                    if (id == x.getId())
                        throw new IllegalArgumentException("Error");
                    x.setId(id);
                }
            }
            return null;
        }
    }

    @Test
    public void test() {
        MethodName.log();
        testObjectList = new TreeMap<String, TestObject>();
        TestObject o = new TestObject();
        testObjectList.put("Test1", o);
        isStarted = false;
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ExecutorCompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(threadPool);
        int remainingFutures = 0;
        for (int i = 0; i < 10; i++) {
            TestRunner tr = new TestRunner(i);
            completionService.submit(tr);
            remainingFutures++;
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
            /*
             * do nothing
             */
        }
        isStarted = true;
        while (remainingFutures > 0) {
            try {
                completionService.take();
            } catch (InterruptedException e) {
                throw new IllegalArgumentException("Unexpected Interrupted Excpetion");
            }
            remainingFutures--;
        }
    }
}

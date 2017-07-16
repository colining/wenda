package cn.colining;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by colin on 2017/7/13.
 */
class myThread extends Thread {
    private int tid;

    public myThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("%d :%d", tid, i));
            }
        } catch (Exception e) {

        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<String> queue;

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + queue.take());
            }
        } catch (Exception e) {

        }
    }

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> queue;

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(1000);
                queue.put(String.valueOf(i));
            }
        } catch (Exception e) {

        }
    }

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }
}


public class MultiThreadTests {
    private static Object object = new Object();

    public static void test1() {
        synchronized ((object)) {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(200);
                    System.out.println(String.format("T3 :%d", i));
                }
            } catch (Exception e) {

            }
        }

    }

    public static void test2() {
        synchronized (object) {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(200);
                    System.out.println(String.format("T4 :%d", i));
                }
            } catch (Exception e) {

            }
        }

    }

    public static void test() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test1();
                    test2();
                }
            }).start();
        }

    }

    public static void testBlockingQueue() {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(blockingQueue)).start();
        new Thread(new Consumer(blockingQueue)).start();
        new Thread(new Consumer(blockingQueue)).start();

    }

    private static ThreadLocal<Integer> threadLocalId = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; i++) {
            final int finaI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        threadLocalId.set(finaI);
                        Thread.sleep(1000);
                        System.out.println("thread: " + threadLocalId.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }

        for (int i = 0; i < 10; i++) {
            final int finaI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        userId = finaI;
                        Thread.sleep(1000);
                        System.out.println("userId: " + userId);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }

    public static void testExecutor() {
//        ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("execurot1: " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("execurot2: " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(1000);
                service.shutdown();
                System.out.println("Wait for termination");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void testWithoutAtomic() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; j++) {
                            counter++;
                            System.out.println(counter);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    public static void testWithAtomic() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; j++) {
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    public static void testAtomic() {

//        testWithoutAtomic();
        testWithAtomic();
    }

    public static void testFutuer() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                throw new IllegalArgumentException("异常");
//                return 1;
            }
        });
        service.shutdown();
        try {
            System.out.println(future.get());
//            System.out.println(future.get(100,TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        test();
        testBlockingQueue();
//        testThreadLocal();
//        testExecutor();
//        testAtomic();
//        testFutuer();
    }

    public static void testThread() {
//        for (int i = 0; i < 10; i++) {
//            new myThread(i).start();
//
//        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2 :%d", i));
                        }
                    } catch (Exception e) {

                    }
                }
            }).start();

        }
    }
}

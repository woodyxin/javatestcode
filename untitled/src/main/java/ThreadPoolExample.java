import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExample {

    // 模拟的任务类，实现 Runnable 接口
    static class MyTask implements Runnable {
        private final int taskId;

        public MyTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            System.out.println("任务 " + taskId + " 开始，线程：" + Thread.currentThread().getName());
            try {
                // 模拟任务执行耗时
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("任务 " + taskId + " 被中断");
                Thread.currentThread().interrupt();
            }
            System.out.println("任务 " + taskId + " 完成");
        }
    }

    public static void main(String[] args) {
        // 创建一个固定大小为3的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        // 提交多个任务到线程池
        for (int i = 1; i <= 10; i++) {
            threadPool.submit(new MyTask(i));
        }

        // 关闭线程池：不再接收新任务，等待已提交任务执行完毕
        threadPool.shutdown();

        try {
            // 等待所有任务完成（最多等待60秒）
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // 超时则强制关闭
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow(); // 当前线程被打断也要强制关闭
        }

        System.out.println("所有任务执行完毕，线程池关闭");
    }
}

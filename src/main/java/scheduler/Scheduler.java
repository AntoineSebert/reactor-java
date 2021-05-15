package scheduler;

import org.jetbrains.annotations.NotNull;
import reactor.Reaction;

import java.util.concurrent.*;

public class Scheduler {

    private static ThreadPoolExecutor executorService = null;

    private Scheduler() {

    }

    public static void createExecutorService(int number_of_threads) throws RuntimeException {
            if (executorService != null) {
                executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(number_of_threads);
            }

    }

    public static void resetExecutorService() {
        executorService = null;
    }

    public static ExecutorService getScheduler() {
        return executorService;
    }

    public static void addReactionTask(@NotNull Reaction reaction) {
        executorService.submit(reaction);
    }

    public static boolean inQueue (Reaction reaction) {
        BlockingQueue<Runnable> queue = executorService.getQueue();
        for (Runnable runnable : queue) {
                if(runnable.toString().contains(reaction.toString()))
                    return true;

        }
        return false;
    }

    public static boolean isEmpty() {
         return executorService.getActiveCount() == 0;
    }

}

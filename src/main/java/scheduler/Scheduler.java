package scheduler;

import org.jetbrains.annotations.NotNull;
import reactor.Reaction;

import java.util.concurrent.*;

public class Scheduler {

    private static ScheduledThreadPoolExecutor executorService = null;

    private static boolean timedTasks = false;

    private Scheduler() {

    }

    public static void createExecutorService(int number_of_threads) throws RuntimeException {
            if (executorService == null) {
                executorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(number_of_threads);
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

    public static void addScheduledReaction(@NotNull Reaction reaction,@NotNull long delay) {
        timedTasks = true;
        executorService.schedule(reaction, delay, TimeUnit.NANOSECONDS);
    }

    public static void addRepeatingReaction(@NotNull Reaction reaction,@NotNull long period, @NotNull long delay) {
        timedTasks = true;
        executorService.scheduleAtFixedRate(reaction, delay, period, TimeUnit.NANOSECONDS);
    }

    public static boolean inQueue (Reaction reaction) {
        BlockingQueue<Runnable> queue = executorService.getQueue();
        for (Runnable runnable : queue) {
                if(runnable.toString().contains(reaction.toString()))
                    return true;

        }
        return false;
    }

    public static void awaitTermination(long time, TimeUnit unit) throws InterruptedException {
        if (timedTasks)
            executorService.awaitTermination(time, unit);
        else
            while (!isEmpty());
    }

    public static void awaitTermination(){
            while (!isEmpty());
    }

    public static void shutDown() {
        executorService.shutdown();
    }

    public static boolean isEmpty() {
         return executorService.getActiveCount() == 0;
    }


}


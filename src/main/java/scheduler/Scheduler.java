package scheduler;

import org.jetbrains.annotations.NotNull;
import reactor.Reaction;

import java.util.concurrent.*;

public class Scheduler {

    private static ScheduledThreadPoolExecutor executorService;

    private static boolean timedTasks;
    private static boolean keepAlive;
    private static volatile boolean aborted;

    private Scheduler() {

    }

    public static void createExecutorService(int number_of_threads) throws RuntimeException {
            if (executorService == null) {
                executorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(number_of_threads);
                timedTasks = false;
                keepAlive = false;
                aborted = false;
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

    public static void addScheduledReaction(@NotNull Reaction reaction, long delay) {
        timedTasks = true;
        executorService.schedule(reaction, delay, TimeUnit.NANOSECONDS);
    }

    public static void addRepeatingReaction(@NotNull Reaction reaction, long period, long delay) {
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

    public static void awaitTermination(long time, TimeUnit unit) throws RuntimeException {
        try {
            if (timedTasks || keepAlive)
                executorService.awaitTermination(time, unit);
            else {
                while (!isEmpty()) {
                    if (aborted) throw new InterruptedException();
                }
            }


        } catch (InterruptedException e) {
            throw new RuntimeException();
        }


    }

    public static void awaitTermination() {
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {

        }
    }

    public static void shutDown() {
        executorService.shutdown();
    }

    public static boolean isEmpty() {
         return executorService.getActiveCount() == 0;
    }

    public static void setKeepAlive(boolean value) {
        keepAlive = value;
    }

    public static void request_stop() {
        executorService.shutdown();
    }

    public static void shutdown() {
        executorService.shutdownNow();
    }

    public static void abort() {
        System.out.println("Aborted");
        aborted = true;
        executorService.shutdownNow();
    }
}


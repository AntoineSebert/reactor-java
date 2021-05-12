package scheduler;

import reactor.Reaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {

    private static ExecutorService executorService = null;

    private Scheduler() {

    }

    public static void createExecutorService(int number_of_threads) throws RuntimeException {
            if (executorService != null) {
                throw new RuntimeException("Cannot re-instantiate executor service");
            }
            executorService = Executors.newFixedThreadPool(number_of_threads);
    }

    public static ExecutorService getScheduler() {
        return executorService;
    }

    public static void addReactionTask(Reaction reaction) {
        executorService.execute(reaction);
    }
}

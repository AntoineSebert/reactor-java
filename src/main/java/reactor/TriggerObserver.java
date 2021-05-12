package reactor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import scheduler.Scheduler;

public class TriggerObserver  {
    private static Map<Trigger, Reaction> reactionMap = new HashMap<>();


    public static void update(Trigger trigger) {
        if (reactionMap.get(trigger) != null)
            Scheduler.addReactionTask(reactionMap.get(trigger));
    }
}

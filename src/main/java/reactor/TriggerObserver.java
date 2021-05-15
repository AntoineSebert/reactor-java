package reactor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import scheduler.Scheduler;

public class TriggerObserver  {
    private static Map<Trigger, Reaction> reactionMap = new HashMap<>();


    public static void update(Trigger trigger) {
        Reaction reaction = reactionMap.get(trigger);
        if (reaction != null && !Scheduler.inQueue(reaction))
            Scheduler.addReactionTask(reaction);
    }

    public static void addReactionMapEntry(Trigger trigger, Reaction reaction) {
        reactionMap.put(trigger,reaction);
    }
}

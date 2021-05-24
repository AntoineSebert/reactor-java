package reactor;

import scheduler.Scheduler;

import java.util.HashMap;
import java.util.Map;

public class TriggerObserver  {
    private static Map<Trigger, Reaction> reactionMap = new HashMap<>();


    public static void update(Trigger trigger) {
        Reaction reaction = reactionMap.get(trigger);
        if (reaction != null && !Scheduler.inQueue(reaction)) {
            reaction.timestamp(trigger.timestamp());
            Scheduler.addReactionTask(reaction);
        }
    }

    public static void addReactionMapEntry(Trigger trigger, Reaction reaction) {
        reactionMap.put(trigger,reaction);
    }
}

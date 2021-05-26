package reactor;

import scheduler.Scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TriggerObserver  {
    private static Map<Trigger, Set<Reaction>> reactionMap = new HashMap<>();


    public static void update(Trigger trigger) {
        Set<Reaction> reactions = reactionMap.get(trigger);
        for (Reaction reaction : reactions) {
            if (reaction.can_trigger()) {
                reaction.timestamp(trigger.timestamp());
                Scheduler.addReactionTask(reaction);
            }
        }
    }

    public static void addReactionMapEntry(Trigger trigger, Reaction reaction) {
        reactionMap.putIfAbsent(trigger,new HashSet<Reaction>());
        reactionMap.get(trigger).add(reaction);
    }
}

package reactor;

import scheduler.Scheduler;

import java.util.*;

public class TriggerObserver  {
    private static Map<String, Set<Reaction>> reactionMap = new HashMap<>();


    public static void update(Trigger trigger) {
        Set<Reaction> reactions = reactionMap.get(trigger.toString());

        for (Reaction reaction : reactions) {
            if (reaction.can_trigger()) {
                reaction.timestamp(trigger.timestamp());
                Scheduler.addReactionTask(reaction);
            }
        }
    }

    public static void addReactionMapEntry(Trigger trigger, Reaction reaction) {
        reactionMap.putIfAbsent(trigger.toString(),new HashSet<Reaction>());
        reactionMap.get(trigger.toString()).add(reaction);
    }
}

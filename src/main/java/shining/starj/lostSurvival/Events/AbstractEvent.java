package shining.starj.lostSurvival.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Events.Prework.TimerEvent;

public abstract class AbstractEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public static void initial() {
        TimerEvent.builder().build();
    }


}

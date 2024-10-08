package shining.starj.lostSurvival.Listeners.PreWork;

import lombok.Builder;
import org.bukkit.event.EventHandler;
import shining.starj.lostSurvival.Buffs.AbstractBuff;
import shining.starj.lostSurvival.Events.Prework.TimerEvent;
import shining.starj.lostSurvival.Listeners.AbstractEventListener;

@Builder
public class BuffListener extends AbstractEventListener {

    @EventHandler
    public void Events(TimerEvent e) {
        for (AbstractBuff buff : AbstractBuff.getList()) buff.tickAll();
    }
}

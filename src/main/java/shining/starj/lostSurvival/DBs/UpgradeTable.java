package shining.starj.lostSurvival.DBs;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpgradeTable extends AbstractTableInstance {
    private final long id;
    private String player;
    private String upgrade;
    private int level;


}

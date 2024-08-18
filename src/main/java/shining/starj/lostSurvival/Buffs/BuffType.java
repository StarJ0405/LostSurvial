package shining.starj.lostSurvival.Buffs;

import lombok.Getter;

@Getter
public enum BuffType {
    POSITIVE("버프"), NEGATIVE("디버프"), ETC("기타"), IDENTITY("아이덴티티")
    //
    ;
    private final String name;

    BuffType(String name) {
        this.name = name;
    }
}

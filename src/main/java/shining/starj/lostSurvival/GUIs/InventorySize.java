package shining.starj.lostSurvival.GUIs;

public enum InventorySize {
    One, Two, Three, Four, Five, Six
    //
    ;

    public int getSize() {
        return (this.ordinal() + 1) * 9;
    }
}
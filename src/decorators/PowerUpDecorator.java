package decorators;

import core.Cell;

public class PowerUpDecorator extends CellDecorator {

    public static final int BONUS_SCORE     = 1;
    public static final int BONUS_CLEAR_ROW = 2;
    public static final int BONUS_SLOW_TIME = 3;
    public static final int BONUS_EXTRA_ROT = 4;

    private final int bonusType;
    private final int bonusValue;    
    private boolean   consumed;      

    public PowerUpDecorator(Cell cell, int bonusType, int bonusValue) {
        super(cell);
        this.bonusType  = bonusType;
        this.bonusValue = bonusValue;
        this.consumed   = false;
        this.colorId    = Cell.COLOR_POWERUP;
    }

    @Override
    public int getColorId() { return Cell.COLOR_POWERUP; }

    @Override
    public boolean isFilled() { return true; }

    @Override
    public void activate() {
        if (consumed) {
            System.out.println("[PowerUp] Zaten kullanıldı.");
            return;
        }
        String typeName = bonusTypeName();
        System.out.println("[PowerUp] AKTIVE! Tip: " + typeName +
                           " | Değer: " + bonusValue);
        consumed = true;
        wrapped.activate();   
    }

    @Override
    public String render() {
        if (consumed) return wrapped.render();
        return "[★]";   
    }

    public int     getBonusType()   { return bonusType;  }
    public int     getBonusValue()  { return bonusValue; }
    public boolean isConsumed()     { return consumed;   }

    public void reset()             { consumed = false;  }

    private String bonusTypeName() {
        switch (bonusType) {
            case BONUS_SCORE:     return "EKSTRA_PUAN(+" + bonusValue + ")";
            case BONUS_CLEAR_ROW: return "SATIR_TEMIZLE(" + bonusValue + ")";
            case BONUS_SLOW_TIME: return "YAVASLA(" + bonusValue + " tick)";
            case BONUS_EXTRA_ROT: return "EKSTRA_ROTASYON(+" + bonusValue + ")";
            default:              return "BILINMEYEN(" + bonusType + ")";
        }
    }

    @Override
    public String toString() {
        return "PowerUpDecorator{type=" + bonusTypeName() +
               ", consumed=" + consumed + "}";
    }
}

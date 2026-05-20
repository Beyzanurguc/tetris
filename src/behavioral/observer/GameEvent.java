package behavioral.observer;

public class GameEvent {

    public enum EventType {
        LINE_CLEARED,     
        LEVEL_UP,         
        GAME_OVER,        
        PIECE_LANDED,     
        COMBO_ACHIEVED    
    }

    private final EventType type;   
    private final int       data;   

    public GameEvent(EventType type, int data) {
        this.type = type;
        this.data = data;
    }

    public GameEvent(EventType type) {
        this(type, 0);
    }

    public EventType getType() { return type; }
    public int       getData() { return data; }

    @Override
    public String toString() {
        return "GameEvent[" + type + ", data=" + data + "]";
    }
}

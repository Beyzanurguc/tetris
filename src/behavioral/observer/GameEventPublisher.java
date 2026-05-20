package behavioral.observer;

import core.DynamicArray;

public abstract class GameEventPublisher {

    private final DynamicArray listeners;

    protected GameEventPublisher() {
        this.listeners = new DynamicArray();
    }

    public void subscribe(GameEventListener listener) {
        if (listener == null) return;

        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i) == listener) return;
        }
        listeners.add(listener);
        System.out.println("[Publisher] Abone eklendi: " +
                           listener.getClass().getSimpleName());
    }

    public void unsubscribe(GameEventListener listener) {
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i) == listener) {
                listeners.remove(i);
                System.out.println("[Publisher] Abone çıkarıldı: " +
                                   listener.getClass().getSimpleName());
                return;
            }
        }
    }

    public void notifyListeners(GameEvent event) {
        System.out.println("[Publisher] Olay yayılıyor: " + event);
        for (int i = 0; i < listeners.size(); i++) {
            GameEventListener l = (GameEventListener) listeners.get(i);
            l.onEvent(event);
        }
    }

    public void clearListeners() {
        listeners.clear();
    }

    public int getListenerCount() {
        return listeners.size();
    }
}

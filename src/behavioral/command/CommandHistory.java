package behavioral.command;

import core.DynamicArray;

public class CommandHistory {

    private static final int MAX_HISTORY = 10;

    private final DynamicArray history;   
    private int topIndex;                  

    public CommandHistory() {
        this.history  = new DynamicArray(MAX_HISTORY + 1);
        this.topIndex = -1;
    }

    public void push(Command command) {
        if (command == null) return;

        while (history.size() > topIndex + 1) {
            history.remove(history.size() - 1);
        }

        if (history.size() >= MAX_HISTORY) {
            history.remove(0);
            topIndex = history.size() - 1;
        }

        history.add(command);
        topIndex = history.size() - 1;
        System.out.println("[CommandHistory] Push: " + command.getDescription() +
                           " (stack=" + (topIndex + 1) + "/" + MAX_HISTORY + ")");
    }

    public void undo() {
        if (topIndex < 0) {
            System.out.println("[CommandHistory] Undo yapılacak komut yok.");
            return;
        }
        Command cmd = (Command) history.get(topIndex);
        cmd.undo();
        topIndex--;
        System.out.println("[CommandHistory] Undo: " + cmd.getDescription() +
                           " (kalan=" + (topIndex + 1) + ")");
    }

    public Command pop() {
        if (topIndex < 0) return null;
        Command cmd = (Command) history.get(topIndex);
        history.remove(topIndex);
        topIndex--;
        return cmd;
    }

    public boolean isEmpty() { return topIndex < 0; }

    public int size() { return topIndex + 1; }

    public void clear() {
        history.clear();
        topIndex = -1;
    }

    @Override
    public String toString() {
        return "CommandHistory [size=" + (topIndex + 1) +
               "/" + MAX_HISTORY + "]";
    }
}

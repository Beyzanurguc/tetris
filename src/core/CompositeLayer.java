package core;

public class CompositeLayer extends BoardLayer {

    private final DynamicArray children;

    public CompositeLayer(String name) {
        super(name);
        this.children = new DynamicArray(4);
    }

    public void add(BoardLayer layer) {
        if (layer == null) return;
        children.add(layer);
        System.out.println("[CompositeLayer/" + name + "] Katman eklendi: " + layer.getName());
    }

    public boolean remove(String layerName) {
        for (int i = 0; i < children.size(); i++) {
            BoardLayer child = (BoardLayer) children.get(i);
            if (child.getName().equals(layerName)) {
                children.remove(i);
                System.out.println("[CompositeLayer/" + name + "] Katman kaldırıldı: " + layerName);
                return true;
            }
        }
        return false;
    }

    public int childCount() { return children.size(); }

    public BoardLayer getChild(int index) {
        return (BoardLayer) children.get(index);
    }

    @Override
    public int getCellColorAt(int row, int col) {
        if (!visible) return Cell.COLOR_EMPTY;

        for (int i = children.size() - 1; i >= 0; i--) {
            BoardLayer child = (BoardLayer) children.get(i);
            int color = child.getCellColorAt(row, col);
            if (color != Cell.COLOR_EMPTY) return color;
        }
        return Cell.COLOR_EMPTY;
    }

    @Override
    public boolean isFilledAt(int row, int col) {
        if (!visible) return false;
        for (int i = 0; i < children.size(); i++) {
            if (((BoardLayer) children.get(i)).isFilledAt(row, col)) return true;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < children.size(); i++) {
            ((BoardLayer) children.get(i)).clear();
        }
        System.out.println("[CompositeLayer/" + name + "] Tüm katmanlar temizlendi.");
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("CompositeLayer[").append(name).append("] ")
          .append(children.size()).append(" alt katman | visible=").append(visible).append("\n");
        for (int i = 0; i < children.size(); i++) {
            sb.append("  └─ ").append(((BoardLayer) children.get(i)).describe()).append("\n");
        }
        return sb.toString();
    }

    public BoardLayer find(String layerName) {
        for (int i = 0; i < children.size(); i++) {
            BoardLayer child = (BoardLayer) children.get(i);
            if (child.getName().equals(layerName)) return child;
            if (child instanceof CompositeLayer) {
                BoardLayer found = ((CompositeLayer) child).find(layerName);
                if (found != null) return found;
            }
        }
        return null;
    }
}

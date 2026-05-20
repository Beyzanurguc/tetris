package core;

public class DynamicArray {

    private Object[] data;       
    private int      size;       
    private int      capacity;   

    private static final int DEFAULT_CAPACITY = 8;

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1) initialCapacity = DEFAULT_CAPACITY;
        this.capacity = initialCapacity;
        this.data     = new Object[capacity];
        this.size     = 0;
    }

    public void add(Object element) {
        ensureCapacity();
        data[size++] = element;
    }

    public void insert(int index, Object element) {
        checkIndexForInsert(index);
        ensureCapacity();

        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        size++;
    }

    public Object get(int index) {
        checkIndex(index);
        return data[index];
    }

    public void set(int index, Object element) {
        checkIndex(index);
        data[index] = element;
    }

    public Object remove(int index) {
        checkIndex(index);
        Object removed = data[index];

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;   
        shrinkIfNeeded();
        return removed;
    }

    public Object removeLast() {
        if (size == 0) throw new IndexOutOfBoundsException("Dizi boş");
        Object last = data[--size];
        data[size] = null;     
        shrinkIfNeeded();
        return last;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;    
        }
        size = 0;

        capacity = DEFAULT_CAPACITY;
        data = new Object[capacity];
    }

    public int  size()      { return size; }
    public boolean isEmpty(){ return size == 0; }

    private void ensureCapacity() {
        if (size < capacity) return;
        int newCapacity = capacity * 2;
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        data     = newData;
        capacity = newCapacity;
    }

    private void shrinkIfNeeded() {
        if (capacity <= DEFAULT_CAPACITY) return;
        if (size > capacity / 4)         return;
        int newCapacity = Math.max(capacity / 2, DEFAULT_CAPACITY);
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = null;    
        }
        data     = newData;
        capacity = newCapacity;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size);
    }

    private void checkIndexForInsert(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException(
                "Insert index: " + index + ", Size: " + size);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

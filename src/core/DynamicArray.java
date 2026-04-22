package core;

/**
 * Manuel Dinamik Dizi (ArrayList yasağı nedeniyle elle yazıldı).
 *
 * STL / java.util koleksiyonları KESİNLİKLE YASAK olduğundan,
 * bu sınıf Object[] üzerinden kendi büyüme / küçülme mantığını uygular.
 *
 * Tasarım Kararları:
 *  - capacity dolduğunda 2x büyür   (amortised O(1) ekleme)
 *  - size < capacity/4 olduğunda 2x küçülür (bellek verimliliği)
 *  - Bellek temizliği: silinen slotlar null'lanır (Java GC yardımı)
 */
public class DynamicArray {

    // ------------------------------------------------------------------ fields
    private Object[] data;       // iç depo
    private int      size;       // gerçek eleman sayısı
    private int      capacity;   // tahsis edilen kapasite

    private static final int DEFAULT_CAPACITY = 8;

    // --------------------------------------------------------------- ctor/init
    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1) initialCapacity = DEFAULT_CAPACITY;
        this.capacity = initialCapacity;
        this.data     = new Object[capacity];
        this.size     = 0;
    }

    // -------------------------------------------------------------- public API

    /** Dizinin sonuna eleman ekle — amortised O(1) */
    public void add(Object element) {
        ensureCapacity();
        data[size++] = element;
    }

    /** Belirtilen index'e eleman ekle — O(n) */
    public void insert(int index, Object element) {
        checkIndexForInsert(index);
        ensureCapacity();
        // sağa kaydır
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        size++;
    }

    /** Index'teki elemanı getir — O(1) */
    public Object get(int index) {
        checkIndex(index);
        return data[index];
    }

    /** Index'teki elemanı değiştir — O(1) */
    public void set(int index, Object element) {
        checkIndex(index);
        data[index] = element;
    }

    /** Index'teki elemanı sil — O(n) */
    public Object remove(int index) {
        checkIndex(index);
        Object removed = data[index];
        // sola kaydır
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;   // bellek temizliği (GC hint)
        shrinkIfNeeded();
        return removed;
    }

    /** Son elemanı sil ve döndür — O(1) */
    public Object removeLast() {
        if (size == 0) throw new IndexOutOfBoundsException("Dizi boş");
        Object last = data[--size];
        data[size] = null;     // bellek temizliği
        shrinkIfNeeded();
        return last;
    }

    /** Dizinin tüm içeriğini temizle */
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;    // bellek temizliği
        }
        size = 0;
        // kapasiteyi DEFAULT_CAPACITY'ye sıfırla
        capacity = DEFAULT_CAPACITY;
        data = new Object[capacity];
    }

    public int  size()      { return size; }
    public boolean isEmpty(){ return size == 0; }

    // --------------------------------------------------------- private helpers

    /** Kapasite dolduğunda 2× büyüt */
    private void ensureCapacity() {
        if (size < capacity) return;
        int newCapacity = capacity * 2;
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        // eski diziyi null'la (bellek temizliği)
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        data     = newData;
        capacity = newCapacity;
    }

    /** size < capacity/4 olduğunda 2× küçült (min DEFAULT_CAPACITY) */
    private void shrinkIfNeeded() {
        if (capacity <= DEFAULT_CAPACITY) return;
        if (size > capacity / 4)         return;
        int newCapacity = Math.max(capacity / 2, DEFAULT_CAPACITY);
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = null;    // bellek temizliği
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

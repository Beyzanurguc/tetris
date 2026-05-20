package behavioral.iterator;

public interface GameIterator<T> {

    boolean hasNext();

    T next();

    void reset();
}

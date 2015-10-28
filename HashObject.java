 /*
 Author: Aaron Gardner
 */
public class HashObject<E> extends Object {

    final E object;
    private final int key;
    private int freqCount;

    HashObject(int key, E value) {
        this.object = value;
        this.key = object.hashCode();
        freqCount = 1;
    }

    @Override
    public boolean equals(Object c) {
    	return this.object.equals(c);
    }

    @Override
    public String toString() {
        return object.toString();
    }

    int getKey() {
        return key;
    }

    void incrementFrequency() {
        freqCount++;
    }

    int getFreq() {
        return freqCount;
    }
}

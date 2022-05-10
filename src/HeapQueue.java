import java.util.Arrays;

class Pair {
    int key,value;
    Pair (int key, int value) {
        this.key = key;
        this.value = value;
    }
    public String toString () {
        return "("+this.key+','+this.value+")";
    }
}
public class HeapQueue {
    private Pair [] array;
    private int [] keys;
    private int size;
    public HeapQueue (int capacity) {
        this.array = new Pair[capacity+1];
        this.keys = new int[capacity+1];
        this.size = 0;
    }
    public int getMax() throws IllegalAccessException {
        if (this.size > 0)
            return this.array[1].value;
        throw new IllegalAccessException("Removing from an empty heap");
    }
    private Boolean comp (int i, int j) {
        return i < j;
    }
    private void swap (int i, int j) {
        keys[array[i].value] = j;
        keys[array[j].value] = i;
        Pair temp = array[i];
        array[i] = array[j];
        array[j] = temp;

    }
    private void swim (int j) {
        while (j > 1 && comp(array[j].key, array[j/2].key)) {
            swap(j,j/2);
            j/=2;
        }
    }
    private void sink (int j) {
        int k;
        while (2*j <= size) {
            k = 2*j;
            if (k + 1 < size && comp(array[k + 1].key, array[k].key)) k++;
            if (comp(array[j].key,array[k].key)) break;
            swap(j,k);
            j = k;
        }
    }
    public void insert (int k, int v) {
        v++;
        if (v < keys.length) {
            if (this.keys[v] == 0) {
                Pair value = new Pair(k, v);
                array[++size] = value;
                this.keys[v] = size;
                swim(size);
            } else {
                int kv = array[this.keys[v]].key;
                array[this.keys[v]].key = k;
                if (comp(k, kv)) swim(this.keys[v]);
                else sink(this.keys[v]);
            }
        }
    }
    public int extractMin () throws IllegalAccessException {
        if (size > 0) {
            int value = array[1].value;
            array[1] = array[size--];
            this.keys[value] = 0;
            if (size != 0)
                this.keys[ array[1].value ] = 1;
            sink(1);
            return value-1;
        } else
            throw new IllegalAccessException("Removing from an empty heap");
    }
    public boolean isEmpty () {
        return this.size == 0;
    }
    public void clear () {
        this.size = 0;
        Arrays.fill(keys, 0);
    }
    public void print () {
        for (int i = 1; i <= size; i++)
            System.out.print(array[i]+" ");
        System.out.println();
    }

}

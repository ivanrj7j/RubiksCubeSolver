package helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class DecreaseKeyPriorityQueue<T> {

    private static class Entry<T> {
        T item;
        int priority;
        long order;

        Entry(T item, int priority, long order) {
            this.item = item;
            this.priority = priority;
            this.order = order;
        }
    }

    private final ArrayList<Entry<T>> heap = new ArrayList<>();
    private final HashMap<T, Integer> positions = new HashMap<>();

    private long nextOrder = 0;

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public boolean contains(T item) {
        return positions.containsKey(item);
    }

    public void add(T item, int priority) {
        Integer index = positions.get(item);

        // New item
        if (index == null) {
            Entry<T> entry = new Entry<>(item, priority, nextOrder++);

            heap.add(entry);

            int newIndex = heap.size() - 1;
            positions.put(item, newIndex);

            bubbleUp(newIndex);
            return;
        }

        // Existing item: decrease priority if better
        Entry<T> entry = heap.get(index);

        if (priority < entry.priority) {
            entry.priority = priority;
            bubbleUp(index);
        }
    }

    public T peek() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        return heap.get(0).item;
    }

    public T poll() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        T result = heap.get(0).item;
        positions.remove(result);

        if (heap.size() == 1) {
            heap.remove(0);
            return result;
        }

        Entry<T> last = heap.remove(heap.size() - 1);

        heap.set(0, last);
        positions.put(last.item, 0);

        bubbleDown(0);

        return result;
    }

    public int getPriority(T item) {
        Integer index = positions.get(item);

        if (index == null) {
            throw new NoSuchElementException("Item not found");
        }

        return heap.get(index).priority;
    }

    private boolean hasHigherPriority(Entry<T> a, Entry<T> b) {
        if (a.priority != b.priority) {
            return a.priority < b.priority;
        }

        return a.order < b.order;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (hasHigherPriority(heap.get(parent), heap.get(index))) {
                break;
            }

            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int best = index;

            if (left < heap.size()
                    && hasHigherPriority(heap.get(left), heap.get(best))) {
                best = left;
            }

            if (right < heap.size()
                    && hasHigherPriority(heap.get(right), heap.get(best))) {
                best = right;
            }

            if (best == index) {
                break;
            }

            swap(index, best);
            index = best;
        }
    }

    private void swap(int i, int j) {
        Entry<T> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        positions.put(heap.get(i).item, i);
        positions.put(heap.get(j).item, j);
    }
}
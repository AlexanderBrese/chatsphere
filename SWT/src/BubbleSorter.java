public class BubbleSorter {
    private SortStrategy strategy;

    public BubbleSorter(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void sortAscending(Object[] array) {
        if (array.length <= 1) {
            return;
        }
        for (int nextToLast = array.length - 2; nextToLast >= 0; nextToLast--) {
            for (int index = 0; index <= nextToLast; index++) {
                if (strategy.compareElements(array[index], array[index + 1]) > 0)
                    swapElements(array, index, index + 1);
            }
        }
    }

    public void swapElements(Object[] array, int index1, int index2) {
        Object temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
}

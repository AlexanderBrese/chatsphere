public class DoubleSortStrategy implements SortStrategy {

    @Override
    public int compareElements(Object element1, Object element2) {
        if ((double)element1 > (double)element2) {
            return 1;
        } else if ((double)element1 == (double)element2) {
            return 0;
        }else{
            return -1;
        }
    }
}

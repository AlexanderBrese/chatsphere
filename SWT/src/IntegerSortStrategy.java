public class IntegerSortStrategy implements SortStrategy  {

    @Override
    public int compareElements(Object element1, Object element2) {
        return (int)element1 - (int)element2;
    }
}

public class IntegerBubbleSorter {

    public static void main(String args[]) {
        Object[] testArray = {3.0, 4.0, 1.0};
        new BubbleSorter(new DoubleSortStrategy()).sortAscending(testArray);
        for (Object i : testArray) System.out.println(i);
    }
}
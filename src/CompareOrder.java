import java.util.Comparator;

public class CompareOrder implements Comparator<MinHeapNode> {
  @Override
   public int compare(MinHeapNode left, MinHeapNode right) {
    return (int)(left.frequency - right.frequency);
  }
}

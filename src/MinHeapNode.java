public class MinHeapNode {
  	public char character;
  	public int frequency;
  	public MinHeapNode left,right;

  	public MinHeapNode(char character, int frequency) {
  		left = right = null;
  		this.character = character;
  		this.frequency = frequency;
  	}
  }

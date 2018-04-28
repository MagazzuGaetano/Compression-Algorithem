import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Deflate {
	
	static String GenerateInputForHuffman(ArrayList<Token> dict, int[] offsets, int[] lengths, char[] literals) {
		for(int i = 0; i < dict.size(); i++) {
			offsets[i] = dict.get(i).getOffset();
		    lengths[i] = dict.get(i).getLength();
		    literals[i] = (dict.get(i).getClass().getName() == "Literal") ? 
		    		((Literal)(dict.get(i))).getLiteral() : '0';
		}
		String input = "";
		for(int i = 0; i < dict.size(); i++)
	    	input += offsets[i];
	    for(int i = 0; i < dict.size(); i++)
	    	input += lengths[i];
	    for(int i = 0; i < dict.size(); i++)
	    	input += literals[i];
	    return input;
	}
	
	public static void main(String args[]) {
		
		//Getting Input text
		Scanner sc = new Scanner(System.in);
	    System.out.print("Insert a string: ");
	    String text = Huffman.readFile(sc.nextLine());
	    
		//LZ77 encoding
	    ArrayList<Token> dict = LZ77.createDictionary(text);
	    //LZ77.printDictionary(dict);
	    
	    //Transformation between LZ77 and Huffman
	    int[] offsets = new int[dict.size()];
	    int[] lengths = new int[dict.size()];
	    char[] literals = new char[dict.size()];
	    String input = GenerateInputForHuffman(dict, offsets, lengths, literals);
	    System.out.println("input: " + input);
	    
	    //Huffman encoding
	    HashMap<Character, Integer> freq = Huffman.countFrequencies(input);
	    //Huffman.printFrequencies(freq);
	    HashMap<Character, String> codes = new HashMap<Character, String>();
	    PriorityQueue<MinHeapNode> minHeap = new PriorityQueue<MinHeapNode>(new CompareOrder());
	    Huffman.huffman(minHeap, freq, codes);
	    //Huffman.printPrefixCodes(codes);
	    String encoded = Huffman.encode(input, codes);
	    System.out.println("encoded: " + encoded);
	    
	    //Writing the compressed file
	    String header = Huffman.createHeader(encoded.length(), codes);
	    String FILE_NAME = "out.bin";
	    //System.out.println("header: " + header);
	    String textToWrite = Huffman.fromStringToBinary(header) + "0000000000000000" + encoded;
	    //System.out.println("\nwrittenToFile: " + textToWrite);
	    boolean[] bitArray = Huffman.fromStringToBitsArray(textToWrite);
	    Huffman.writeBits(bitArray, FILE_NAME);
	    
	    //Reset Huffman encoding
	    freq = null;
	    minHeap = null;
	    codes = new HashMap<Character, String>();
	    
	    //Decompress
	    bitArray = Huffman.readBits(textToWrite.length(), FILE_NAME);
	    String textRead = Huffman.fromBitsArrayToString(bitArray);
	    //System.out.println("\nreadFromFile: " + textRead);
	    int index = Huffman.GetSplitHeaderIndex(textRead);
	    header = Huffman.fromBinaryStringToString(textRead.substring(0, index - 7));
	    textRead = textRead.substring(index + 6, textRead.length());
	    //System.out.println("\nheader: " + header);
	    //System.out.println("data: " + textRead);
	    Huffman.setHeader(header, codes);
	    String decoded = Huffman.decode(textRead, codes);
	    System.out.println("decoded: " + decoded);
	    
	    //Turn Back from Huffman to LZ77
	    offsets = new int[dict.size()];
	    lengths = new int[dict.size()];
	    literals = new char[dict.size()];
	    for(int i = 0; i < dict.size(); i++)
	    	offsets[i] = Integer.parseInt(decoded.charAt(i) + "");
	    for(int i = 0; i < dict.size(); i++)
	    	lengths[i] = Integer.parseInt(decoded.charAt(dict.size() + i) + "");
	    for(int i = 0; i < dict.size(); i++)
	    	literals[i] = decoded.charAt(dict.size() + dict.size() + i);
	    
	    /*for(int i = 0; i < dict.size(); i++) {
	    	System.out.println(offsets[i] + " " + lengths[i] + " " + literals[i]);
	    }*/
	    
	    //Turn Back from LZ77 to The original text
	    String s = "";
	    for(int i = 0; i < dict.size(); i++) {
	    	if(offsets[i] == 0 && lengths[i] == 0 && literals[i] != '0') s += literals[i];
	    	else {
	    		
	    		int length = (offsets[i] == 0)? lengths[i] - 1 : lengths[i];
	    		for(int j = offsets[i]; j <= length; j++)
	    			s += s.charAt(j) + "";
	    	}
	    }
	    System.out.println(s);
	    
	}
}

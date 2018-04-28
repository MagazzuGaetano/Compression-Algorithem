import java.util.HashMap;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class Huffman {
  public static HashMap<Character, Integer> countFrequencies(String input) {
    HashMap<Character, Integer> hmap = new HashMap<Character, Integer>();
    for(int i = 0; i < input.length(); i++) {
      Character currentCharacter = input.charAt(i);
      if(hmap.containsKey(currentCharacter)) {
        int count = hmap.get(currentCharacter);
        hmap.put(currentCharacter, ++count);
      }
      else
        hmap.put(currentCharacter, 1);
    }
    return hmap;
  }
  public static void printFrequencies(HashMap<Character, Integer> freq) {
    System.out.println("Frequencies:");
    for(HashMap.Entry<Character, Integer> entry : freq.entrySet())
      System.out.println("[ "+ entry.getKey() + " : " +  entry.getValue() + " ]");
  }
  public static void huffman(PriorityQueue<MinHeapNode> minHeap, HashMap<Character, Integer> freq, HashMap<Character, String> codes) {
    MinHeapNode left, right, top;
    for (HashMap.Entry<Character, Integer> f : freq.entrySet())
      minHeap.add(new MinHeapNode(f.getKey(), f.getValue()));
      while (minHeap.size() != 1)
      {
          left = minHeap.peek();
          minHeap.poll();
          right = minHeap.peek();
          minHeap.poll();
          top = new MinHeapNode('$', left.frequency + right.frequency);
          top.left = left;
          top.right = right;
          minHeap.add(top);
      }
      storeCodes(minHeap.peek(), "", codes);
  }
  public static void storeCodes(MinHeapNode root, String str, HashMap<Character, String> codes) {
	    if (root == null)
	        return;
	    if (root.character != '$')
	        codes.put(root.character, str);
	    storeCodes(root.left, str + "0", codes);
	    storeCodes(root.right, str + "1", codes);
	}
  public static void printPrefixCodes(HashMap<Character, String> codes) {
    System.out.println("Prefix codes:");
    for (HashMap.Entry<Character, String> entry : codes.entrySet())
      System.out.println("[ "+ entry.getKey() + " : " +  entry.getValue() + " ]");
  }
  public static String encode(String textToEncode, HashMap<Character, String> codes) {
    String textEncoded = "";
    for (int i = 0; i < textToEncode.length(); i++)
          textEncoded += codes.get(textToEncode.charAt(i));
    return textEncoded;
  }
  public static String decode(String encoded, HashMap<Character, String> codes) {
    String decompressed = "";
    String temp = "";
    for(int i = 0; i < encoded.length(); i++) {
      temp += encoded.charAt(i);
      for(HashMap.Entry<Character, String> entry : codes.entrySet())
        if(temp.equals(entry.getValue())) {
          decompressed += entry.getKey();
          temp = "";
        }
    }
    return decompressed;
  }
  public static boolean[] fromStringToBitsArray(String str) {
    boolean[] array = new boolean[str.length()];
    for (int k = 0; k < array.length; k++) {
      array[k] = str.charAt(k) != '0';
    }
    return array;
  }
  public static String fromBitsArrayToString(boolean[] bitsArray) {
    String str = "";
    for (int k = 0; k < bitsArray.length; k++) {
      str += (bitsArray[k] == true)? '1' : '0';
    }
    return str;
  }
  public static void writeBits(boolean[] bits, String fileName) {
    BitOutputStream fout = null;
    try {
      fout = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
      for (int index = 0; index < bits.length; index++){
        fout.write(bits[index]);
      }
        fout.close();
    } catch(FileNotFoundException ex) {
        System.out.println(
            "Unable to open file '" +
            fileName + "'");
    } catch(IOException ex) {
        System.out.println(
            "Error reading file '"
            + fileName + "'");
    }
  }
  public static boolean[] readBits(int length, String fileName) {
    BitInputStream fin = null;
    boolean[] inputArray = null;
    try{
      fin = new BitInputStream(new BufferedInputStream(new FileInputStream(fileName)));
      inputArray = new boolean[length];
      for (int index = 0; index < inputArray.length; index++)
        inputArray[index] = fin.read();
      fin.close();
      return inputArray;
    } catch(FileNotFoundException ex) {
        System.out.println(
            "Unable to open file '" +
            fileName + "'");
    } catch(IOException ex) {
        System.out.println(
            "Error reading file '"
            + fileName + "'");
    }
    return inputArray;
  }
  public static String readFile(String fileName) {
    String line = null;
    String text = "";
    try {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        while((line = bufferedReader.readLine()) != null)
          text += line + "\n";
        bufferedReader.close();
        text = text.substring(0, text.length()-1);
        return text;
    }
    catch(FileNotFoundException ex) {
        System.out.println("Unable to open file '" + fileName + "'");
    }
    catch(IOException ex) {
        System.out.println("Error reading file '" + fileName + "'");
    }
    return text;
  }
  public static void writeFile(String text, String filePath, String fileName) {
    BufferedWriter writer = null;
    try {
        writer = new BufferedWriter(new FileWriter(new File(filePath, fileName)));
        writer.write(text);
        writer.close();
    } catch(FileNotFoundException ex) {
        System.out.println("Unable to open file");
    } catch(IOException ex) {
        System.out.println("Error reading file");
    }
  }
  public static String fromStringToBinary(String s) {
    String bin = "";
    for(int i = 0; i < s.length(); i++)
      bin += fromCharToBinaryString(s.charAt(i));
    return bin;
  }
  public static String fromBinaryStringToString(String s) {
    String str = "";
    String sub = "";
    int k = 0;
    for(int i = 0; i < s.length(); i++) {
      if(k == 7) {
        k = 0;
        sub = s.substring(i - 7, i + 1);
        str += fromBinaryStringToChar(sub);
      } else {
          k++;
        }
    }
    return str;
  }
  public static String fromCharToBinaryString(char c) {
    String bin = "";
    int n = c;
    for(int i = 0; i < 8; i++) {
      bin = n % 2 + bin;
      n = n / 2;
    }
    return bin;
  }
  public static char fromBinaryStringToChar(String s) {
    int n = 0;
    int k = 0;
    for(int i = s.length() - 1; i >= 0; i--) {
      n += (s.charAt(i) - '0') * (int)Math.pow(2, k);
      k++;
    }
    return (char)n;
  }
  public static void setHeader(String textToDecode, HashMap<Character, String> codes){
    String[] splitHeader = textToDecode.split("  ");
    for (int k = 1; k < splitHeader.length; k++) {
      String car = splitHeader[k];
      codes.put(car.split("\t")[0].charAt(0), car.split("\t")[1]);
    }
  }
  public static String createHeader(int textEncodedLength, HashMap<Character, String> codes) {
    String header = textEncodedLength + "  ";
    for (HashMap.Entry<Character, String> code : codes.entrySet()) {
     header +=  code.getKey() + "\t" + code.getValue() + "  ";
    }
    return header;
  }
  public static int GetSplitHeaderIndex(String s) {
    int k = 0;
    int index = 0;
    for(int i = 0; i < s.length() && k < 16; i++) {
      if(s.charAt(i) == '0')
        k++;
      else if(s.charAt(i) != '0') k = 0;
      if(k == 16) index = i;
    }
    return index;
  }
}

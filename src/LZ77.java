import java.util.ArrayList;

public class LZ77 {
  
  public static ArrayList<Token> createDictionary(String text) {
	  ArrayList<Token> dictionary = new ArrayList<Token>();
	  int i = 0;
	  int j = 0;
	  while(i < text.length()) {
		  j = i - 1;
		  if(j < 0 || text.substring(0, j).indexOf(text.charAt(i)) == -1) {
			  dictionary.add(new Literal(0, 0, text.charAt(i)));
			  i++;
		  }
		  else {
			  String match = text.charAt(i) + "";
			  int pos = 0;
			  int lastPos = pos;
			  int k = 1;
			  while((pos = text.substring(0, i).indexOf(match)) >= 0 && (i + k) < text.length()) {
				  match += text.charAt(i + k) + "";
				  lastPos = pos;
				  k++;
			  }
			  int length = (pos >= 0 && pos < text.length()) ? match.length() : match.length() - 1;
			  pos = lastPos;//text.substring(0, j).length() - lastPos + 1;
			  dictionary.add(new Token(pos, length));
			  i += length;
			  match = "";
		  }
		  //System.out.println(text.substring(0, i) + " | " + text.substring(i));
	  }
	  return dictionary;
  }
  
  public static void printDictionary(ArrayList<Token> dictionary) {
    for (Token t : dictionary) {
      if(t.getClass().getName() == "Literal") {
        Literal l = (Literal)t;
        System.out.println("< " + t.getOffset() + ", " + t.getLength() + ", " + l.getLiteral() + " >");
      }
      else
        System.out.println("< " + t.getOffset() + ", " + t.getLength() + " >");
    }
  }
  
}

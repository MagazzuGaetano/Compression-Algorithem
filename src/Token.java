public class Token {
  private int offset;
  private int length;

  public Token() {}

  public Token(int offset, int length) {
    setOffset(offset);
    setLength(length);
  }

  private void setOffset(int offset) {
    if(offset >= 0)
      this.offset = offset;
  }
  public int getOffset() {
    return offset;
  }

  private void setLength(int length) {
    if(length >= 0)
      this.length = length;
  }
  public int getLength() {
    return length;
   }
}

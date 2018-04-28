public class Literal extends Token {
  private char literal;

  public Literal(int offset, int length, char literal) {
    super(offset, length);
    setLiteral(literal);
  }

  public char getLiteral() {
    return literal;
  }
  private void setLiteral(char literal) {
    this.literal = literal;
  }
}

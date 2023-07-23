/* -*- c++ -*- */

package teeny;

import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;

class Reader {
  StreamTokenizer input;

  Reader (InputStream _input) {
    input = new StreamTokenizer (_input);
    input.resetSyntax ();
    input.wordChars (0, 255);
    input.parseNumbers ();
    input.ordinaryChar ('\'');
    input.ordinaryChar ('(');
    input.ordinaryChar (')');
    input.ordinaryChar ('#');
    input.commentChar (';');
    input.eolIsSignificant (false);
    input.lowerCaseMode (true);
    input.quoteChar ('"');
    input.whitespaceChars (' ', ' ');
    input.whitespaceChars ('\t', '\t');
    input.whitespaceChars ('\n', '\n');
    input.whitespaceChars ('\r', '\r');
  }

  private Object must_read () { 
    Object result = read (); 
    if (result == Teeny.eof)
      throw new Teeny_error ("Unexpected EOF");
    return result;
  }

  private int nextToken () { 
    try {
      return input.nextToken ();
    } catch (IOException e) {
      throw new Teeny_error ("I/O error", e);
    }
  }

  Object read () {
    int token = nextToken ();
    switch (token) {

    case StreamTokenizer.TT_EOF:     input.pushBack (); return Teeny.eof;
    case StreamTokenizer.TT_NUMBER:  return new Double (input.nval);
    case StreamTokenizer.TT_WORD:    return Symbol.make (input.sval);

    case '"': return new String (input.sval);

    case '-':    // StreamTokenizer doesn't return TT_WORD here, which we want
                 // (any other such cases?)
      return Symbol.make ("-");

    case '(': 
      List result = List.nil;
      for (;;) {
	int next = nextToken ();
	switch (next) {
	case StreamTokenizer.TT_EOF:
	  input.pushBack ();
	  throw new Teeny_error ("Unexpected EOF");
	case ')':
	  return result.nreverse (); 
	default:
	  input.pushBack ();
	  result = new Pair (must_read (), result);
	}
      }

    case '#':
      {
	int t = nextToken ();
	switch (t) {

	case StreamTokenizer.TT_EOF:
	  throw new Teeny_error ("Unexpected EOF");

	case '(':
	  input.pushBack ();
	  return ((List) read ()).to_vector ();

	case StreamTokenizer.TT_WORD:
	  if (input.sval.equals ("f"))
	    return Boolean.FALSE;
	  if (input.sval.equals ("t"))
	    return Boolean.TRUE;
	  /* fall through */

	default:
	  throw new Teeny_error ("Bad '#' sequence in reader: " + input.sval);
	}
      }

    case '\'':
      return new Pair (Symbol.make ("quote"), 
		       new Pair (must_read (), List.nil));

    default:
      throw new Teeny_error ("Unexpected '" + (char) token + "'");
    }
  }
}

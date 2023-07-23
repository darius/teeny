/* -*- c++ -*- */

package teeny;

public class Teeny_error extends RuntimeException {

  public Object irritant;

  public Teeny_error (String _message, Object _irritant) {
    super (_message); 
    irritant = _irritant;
  }

  public Teeny_error (String _message) {
    super (_message);
    irritant = Boolean.FALSE;
  }

  public String toString () {
    return getMessage () + " -- " + irritant;
  }

}

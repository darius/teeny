/* -*- c++ -*- */

package teeny;

import java.io.*;

public class Test {

  public static void main (String[] args) {
    PrimitiveProc.install (Teeny.getInstance ().get_global_env ());
    echo ();
  }

  static void echo () {
    Reader reader = new Reader (System.in);
    for (;;) {
      System.out.print (";-) ");
      Object o;
      try { o = reader.read (); }
      catch (Teeny_error e) { System.out.println (e.getMessage ()); break; }

      if (o == Teeny.eof)
	break;
      System.out.println (
	Teeny.toString (
	  Teeny.evaluate (o, Teeny.getInstance ().get_global_env ())));
    }
  }

}





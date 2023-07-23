package teeny;

import java.io.*;

public class Interpreter {

  public static void main (String[] args) throws IOException {
    PrimitiveProc.install (Teeny.getInstance ().get_global_env ());
    for (int i = 0; i < args.length; i++)
      evaluate_file (args[i]);
  }

  /**
   * Evaluate each form in the file, in sequence.
   * @param filename The name of the file, or "-" for standard-in
   */
  public static void 
  evaluate_file (String filename) 
  throws IOException {
    if (!filename.equals("-"))
      evaluate_stream (new FileInputStream (filename), false);
    else
      evaluate_stream(System.in, true);
  }

  public static void 
  evaluate_stream (InputStream in, boolean interactive) {

    Reader reader = new Reader (in);
    while (true) {
      if (interactive)
	System.out.print ("teeny> ");
      Object o, p;
      try { o = reader.read (); }
      catch (Teeny_error e) { 
	System.err.println ("Read error: " + e);
	e.printStackTrace();
	break; 
      }

      if (o == Teeny.eof)
	break;
      try { 
	p = Teeny.evaluate (o, Teeny.getInstance ().get_global_env());
      }
      catch (Teeny_error e) { 
	System.err.println ("Eval error: " + e);
	e.printStackTrace();
	break; 
      }
      if (interactive)
	System.out.println (Teeny.toString (p));
    }
  }

}

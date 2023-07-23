/* -*- c++ -*- */

package teeny;

import java.util.Hashtable;
import java.util.NoSuchElementException;

public class Symbol implements Evaluable {
  static Hashtable symbol_table = new Hashtable ();

  public static synchronized Symbol make (String _name) {
    Symbol result = (Symbol) symbol_table.get (_name);
    if (result == null)
      symbol_table.put (_name, result = new Symbol (_name));
    return result;
  }

  public Object evaluate (Environment env) {
    try {
      return env.get (this);
    }
    catch (NoSuchElementException e) {
      throw new Teeny_error ("Unbound variable", this);
    }
  }

  public String toString () { return name; }

  public boolean equals (Object obj) { return this == obj; }

  public int hashCode () { return name.hashCode (); }

  String name;

  Symbol (String _name) { name = _name; }

  /*
  Exp compile () { return new Var_ref (this); }
  */
}


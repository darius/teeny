/* -*- c++ -*- */

package teeny;

import java.util.NoSuchElementException;

import java.util.Hashtable;

public class GlobalEnvironment extends Environment {

  Hashtable ht = new Hashtable ();

  public GlobalEnvironment () { }

  public Object get (Symbol sym) throws NoSuchElementException {
    Object value = ht.get (sym);
    if (value == null)
      throw new NoSuchElementException ();
    return value;
  }

  public void put (Symbol sym, Object value) {
    ht.put (sym, value);
  }

}


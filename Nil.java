// -*- C++ -*-

package teeny;

/*
 * 
 */

public class Nil extends List {

  Nil () { }

  public boolean equals (Object obj) { return obj instanceof Nil; }

  public int hashCode () { return 42; }

  public int length () { return 0; }

  static private Object[] empty_vec = { };

  public Object[] to_vector () { return empty_vec; }

  public List nreverse () { return this; }

}


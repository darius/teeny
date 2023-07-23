/* -*- c++ -*- */

package teeny;

import java.util.NoSuchElementException;

/*
 * An environment is a mutable map from symbols to objects.
 */

public abstract class Environment {

  public abstract Object get (Symbol sym) throws NoSuchElementException;

  public abstract void put (Symbol sym, Object value);

}


/* -*- c++ -*- */

package teeny;

import java.util.NoSuchElementException;

/*
 * An environment is a mutable map from symbols to objects.
 */

public class EnvFrame extends Environment {

  Symbol[] syms;
  Object[] vals;
  Environment env;

  public EnvFrame (Symbol[] symbols, Object[] values, Environment enclosing) {
    if (symbols.length != values.length)
      throw new RuntimeException ("BUG: wrong number of arguments");
    syms = symbols;
    vals = values;
    env = enclosing;
  }

  public Object get (Symbol sym) throws NoSuchElementException {
    for (int i = syms.length - 1; 0 <= i; --i)
      if (sym == syms [i])
	return vals [i];
    return env.get (sym);
  }

  public void put (Symbol sym, Object value) {
    for (int i = syms.length - 1; 0 <= i; --i)
      if (sym == syms [i]) {
	vals [i] = value;
	return;
      }
    env.put (sym, value);
  }

}


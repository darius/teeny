// -*- C++ -*-

package teeny;

import java.util.NoSuchElementException;

/*
 * 
 */

public class Pair extends List implements Evaluable {

  Object car;
  List cdr;

  public Pair (Object car, List cdr) 
  {
    this.car = car;
    this.cdr = cdr;
  }

  public Object car () { return this.car; }
  public List   cdr () { return this.cdr; }

  public void set_car (Object obj) { car = obj; }
  public void set_cdr (List list)  { cdr = list; }

  public boolean equals (Object obj) 
  {
    return obj instanceof Pair
        && car ().equals (((Pair) obj).car ())
        && cdr ().equals (((Pair) obj).cdr ());
  }

  public int hashCode () 
  { 
    return car ().hashCode () * 17 + cdr ().hashCode ();
  }

  public int length () { return 1 + cdr ().length (); }

  public List nreverse () 
  {
    List result = List.nil;
    List list = this;
    while (list instanceof Pair) 
      {
	Pair temp = (Pair) list;
	List tail = temp.cdr ();
	temp.set_cdr (result);
	result = list;
	list = tail;
      }
    return result;
  }

  public Object[] to_vector ()
  {
    Object[] vec = new Object [length ()];
    ListEnumeration e = new ListEnumeration (this);
    for (int i = 0; e.hasMoreElements (); ++i) 
      vec [i] = e.nextElement ();
    return vec;
  }

  private static Symbol sym_quote  = Symbol.make ("quote");
  private static Symbol sym_lambda = Symbol.make ("lambda");
  private static Symbol sym_setq   = Symbol.make ("set!");
  private static Symbol sym_if     = Symbol.make ("if");
  private static Symbol sym_begin  = Symbol.make ("begin");
  private static Symbol sym_while  = Symbol.make ("while");

  public Object evaluate (Environment env) 
  {
    Object op = car ();
    List rands = cdr ();

    if (op instanceof Symbol) 
      {
	if (op == sym_quote) 
	  {
	    check_argcount (rands, 1);
	    return ((Pair) rands).car ();
	  } 
	else if (op == sym_setq) 
	  {
	    check_argcount (rands, 2);
	    Object rand1 = ((Pair) rands).car ();
	    Object rand2 = ((Pair) ((Pair) rands).cdr ()).car ();
	    if (!(rand1 instanceof Symbol))
	      throw new Teeny_error ("set! of non-variable", this);
	    Object arg2 = Teeny.evaluate (rand2, env);
	    env.put ((Symbol) rand1, arg2);
	    return arg2;
	  } 
	else if (op == sym_lambda) 
	  {
	    check_argcount (rands, 2);
	    Object rand1 = ((Pair) rands).car ();
	    Object rand2 = ((Pair) ((Pair) rands).cdr ()).car ();
	    if (!(rand1 instanceof List))
	      throw new Teeny_error ("Lambda-list not a list", this);
	    Symbol[] params = new Symbol [((List) rand1).length ()];
	    ListEnumeration e = new ListEnumeration ((List) rand1);
	    for (int i = 0; e.hasMoreElements (); ++i) 
	      {
		Object p = e.nextElement ();
		if (!(p instanceof Symbol))
		  throw new Teeny_error ("Non-variable parameter", this);
		params [i] = (Symbol) p;
	      }
	    return new Closure (env, params, rand2);
	  }
	else if (op == sym_if) 
	  {
	    check_argcount (rands, 3);
	    Object rand1 = ((Pair) rands).car ();
	    Pair cdr1 = (Pair) ((Pair) rands).cdr ();
	    return Teeny.evaluate(Teeny.is_true (Teeny.evaluate (rand1, env))
				   ? cdr1.car ()
				   : ((Pair) cdr1.cdr ()).car (),
				  env);
	  }
	else if (op == sym_begin) 
	  {
	    Object result = Boolean.TRUE;
	    ListEnumeration e = new ListEnumeration (rands);
	    for (int i = 0; e.hasMoreElements (); ++i) 
	      // This isn't properly tail-recursive:
	      result = Teeny.evaluate (e.nextElement (), env);
	    return result;
	  }
	else if (op == sym_while) 
	  {
	    if (rands.length () < 2)
	      throw new Teeny_error ("Bad syntax", this);
	    Object rand1 = ((Pair) rands).car ();
	    Pair cdr1 = (Pair) ((Pair) rands).cdr ();
	    while (Teeny.is_true (Teeny.evaluate (rand1, env)))
	      {
		ListEnumeration e = new ListEnumeration (cdr1);
		for (int i = 0; e.hasMoreElements (); ++i) 
		  Teeny.evaluate(e.nextElement (), env);
	      }
	    return Boolean.TRUE;
	  }

	// Okay, it wasn't a keyword; let's see if it's a macro.

      check_macro:
	{
	  Callable macro_expander;
	  try 
	    { 
	      macro_expander = 
		(Callable)
		Teeny.getInstance ().get_macro_env ().get ((Symbol) op); 
	    }
	  catch (NoSuchElementException e)
	    { break check_macro; }

	  {
	    Object[] args = new Object [1];
	    args [0] = rands;
	    return Teeny.evaluate (Teeny.apply (macro_expander, args), env);
	  }
	}

	// It wasn't a macro; fall through to the procedure-call case.
      }

    Object proc = Teeny.evaluate (op, env);
    Object[] args = new Object [rands.length ()];
    ListEnumeration r = new ListEnumeration (rands);
    for (int i = 0; i < args.length; ++i)
      args [i] = Teeny.evaluate (r.nextElement (), env);
    return Teeny.apply (proc, args);
  }

  private void check_argcount (List rands, int expected) 
  {
    if (rands.length () != expected)
      throw new Teeny_error ("Wrong # of args to special form", rands);
  }

  public static Pair as_pair (Object obj)
  {
    if (!(obj instanceof Pair))
      throw new Teeny_error ("Bad argument type", obj);
    return (Pair) obj;
  }

}


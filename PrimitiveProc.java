/* -*- c++ -*- */

package teeny;

/*
 */

public class PrimitiveProc implements Callable {
  
  public static final int CAR     =  0;
  public static final int CDR     =  1;
  public static final int CONS    =  2;
  public static final int NULL    =  3;
  public static final int EQ      =  4;
  public static final int EQUALP  =  5;
  public static final int ATOMP   =  6;
  public static final int ADD     =  7;
  public static final int SUB     =  8;
  public static final int MUL     =  9;
  public static final int DIV     = 10;
  public static final int QUOT    = 11;
  public static final int MOD     = 12; 
  public static final int LT      = 13;
  public static final int GET     = 14;
  public static final int PUT     = 15;
  public static final int CADR    = 16;
  public static final int CDDR    = 17;
  public static final int LIST    = 18;
  public static final int SYMBOLP = 19;
  public static final int PAIRP   = 20;
  public static final int NUMBERP = 21;
  public static final int SETCAR  = 22;
  public static final int SETCDR  = 23;
  public static final int APPLY   = 24;
  public static final int EVAL    = 25;
  public static final int NREV    = 26;
  public static final int WRITE   = 27;
  public static final int NEWLINE = 28;
  public static final int READ    = 29;
  public static final int RANDOM  = 30;
  public static final int GENSYM  = 31;
  public static final int ERROR   = 32;
  public static final int DEFMAC  = 33;
  public static final int LOAD    = 34;
  public static final int ASK     = 35;
  public static final int ASK_S   = 36;
  public static final int GETF_S  = 37;
  public static final int MAKE    = 38;
  public static final int GETF    = 39;
  public static final int MK_VEC  = 40;
  public static final int VEC_REF = 41;
  public static final int VEC_SET = 42;
  public static final int VEC_LEN = 43;
  public static final int VECTORP = 44;
  public static final int VECTOR  = 45;
  public static final int VEC2LIST = 46;
  public static final int LIST2VEC = 47;

  int id, param_count;

  static int gensym_counter = 0;

  public PrimitiveProc (int _id, int _param_count) 
  { 
    id = _id; 
    param_count = _param_count;
  }

  public String toString () { return "#<primitive " + id + ">"; }

  private static void inst (Environment e, String name, int number, int nargs) {
    e.put (Symbol.make (name), new PrimitiveProc (number, nargs));
  }

  public static void install (Environment e) {
    inst (e, "car",        CAR,     1);
    inst (e, "cdr",        CDR,     1);
    inst (e, "cons",       CONS,    2);
    inst (e, "null?",      NULL,    1);
    inst (e, "eq?",        EQ,      2);
    inst (e, "equal?",     EQUALP,  2);
    inst (e, "atom?",      ATOMP,   1);
    inst (e, "+",          ADD,     2);
    inst (e, "-",          SUB,     2);
    inst (e, "*",          MUL,     2);
    inst (e, "/",          DIV,     2);
    inst (e, "quotient",   QUOT,    2);
    inst (e, "remainder",  MOD,     2);
    inst (e, "<",          LT,      2);
    inst (e, "get",        GET,     2);
    inst (e, "put",        PUT,     3);
    inst (e, "cadr",       CADR,    1);
    inst (e, "cddr",       CDDR,    1);
    inst (e, "list",       LIST,   -1);
    inst (e, "symbol?",    SYMBOLP, 1);
    inst (e, "pair?",      PAIRP,   1);
    inst (e, "number?",    NUMBERP, 1);
    inst (e, "set-car!",   SETCAR,  2);
    inst (e, "set-cdr!",   SETCDR,  2);
    inst (e, "apply",      APPLY,   2);
    inst (e, "eval",       EVAL,    1);
    inst (e, "reverse!",   NREV,    1);
    inst (e, "write",      WRITE,   1);
    inst (e, "newline",    NEWLINE, 0);
    inst (e, "read",       READ,    0);
    inst (e, "random",     RANDOM,  1);
    inst (e, "gensym",     GENSYM,  0);
    inst (e, "error",      ERROR,  -1);
    inst (e, "define-macro", DEFMAC, 2);
    inst (e, "load",       LOAD,    1);
    inst (e, "ask",        ASK,     3);
    inst (e, "ask-class",  ASK_S,   3);
    inst (e, "get-static", GETF_S,  2);
    inst (e, "make",       MAKE,    2);
    inst (e, "get-field",  GETF,    2);
    inst (e, "make-vector",MK_VEC,  2);
    inst (e, "vector-ref", VEC_REF, 2);
    inst (e, "vector-set!",VEC_SET, 3);
    inst (e, "vector-length", VEC_LEN, 1);
    inst (e, "vector?",    VECTORP, 1);
    inst (e, "vector",     VECTOR, -1);
    inst (e, "vector->list", VEC2LIST, 1);
    inst (e, "list->vector", LIST2VEC, 1);
  }

  public Object apply (Object[] args)
  {
    if (0 <= param_count && args.length != param_count)
      throw new Teeny_error ("Bad arg count to primitive", this);

    switch (id) {
    case CONS:     return new Pair (args [0], List.as_list (args [1]));
    case NULL:     return Teeny.make_flag (args [0] instanceof Nil);
    case EQ:       return Teeny.make_flag (args [0] == args [1]);
    case EQUALP:   return Teeny.make_flag (args [0].equals (args [1]));
    case ATOMP:    return Teeny.make_flag (!(args [0] instanceof Pair));
    case SYMBOLP:  return Teeny.make_flag (args [0] instanceof Symbol);
    case PAIRP:    return Teeny.make_flag (args [0] instanceof Pair);
    case EVAL:     return 
		     Teeny.evaluate (args [0], 
				     Teeny.getInstance ().get_global_env ());
    case WRITE:    System.out.print (Teeny.toString (args [0]));
                   System.out.print (' '); 
		   System.out.flush (); 
		   return Boolean.TRUE;
    case NEWLINE:  System.out.println (); 
                   return Boolean.TRUE;
    case READ:     return Teeny.getInstance ().get_reader ().read ();
    case GENSYM:   return new Symbol ("#G" + gensym_counter++);
    case ERROR:    throw new Teeny_error ("User error", args);

    case CAR:      return Pair.as_pair (args [0]).car ();
    case CDR:      return Pair.as_pair (args [0]).cdr ();
    case CADR:     return Pair.as_pair (Pair.as_pair (args [0]).cdr ()).car ();
    case CDDR:     return Pair.as_pair (Pair.as_pair (args [0]).cdr ()).cdr ();
    case SETCAR:   Pair.as_pair (args [0]).set_car (args [1]); 
                   return args [1];
    case SETCDR:   Pair.as_pair (args [0]).set_cdr (List.as_list (args [1])); 
                   return args [1];
    case NREV:     return List.as_list (args [0]).nreverse ();
    case LIST:     return List.from_vector (args);
    case APPLY:    return Teeny.apply (args [0], 
				      List.as_list (args [1]).to_vector ());
    case NUMBERP:  return Teeny.make_flag (args [0] instanceof Double);
    case ADD:
      return new Double (((Double) args [0]).doubleValue() + 
                         ((Double) args [1]).doubleValue());
    case SUB: 
      return new Double (((Double) args [0]).doubleValue() - 
                         ((Double) args [1]).doubleValue());
    case MUL:
      return new Double (((Double) args [0]).doubleValue() * 
                         ((Double) args [1]).doubleValue());
    case DIV:
    { double denom = ((Double) args [1]).doubleValue();
      if (denom == 0)        // use try/catch instead?
	throw new Teeny_error ("Divide by 0");
      return new Double (((Double)args [0]).doubleValue() / denom);
    }
    case QUOT:
    { double numer = ((Double) args [0]).doubleValue();
      if (Math.floor (numer) != numer)
	throw new Teeny_error("");
      double denom = ((Double) args [1]).doubleValue();
      if (denom == 0 || Math.floor (denom) != denom)
	throw new Teeny_error("");
      return new Double ((double) ((long) numer / (long) denom));
    }
    case MOD:
    { double numer = ((Double) args [0]).doubleValue();
      if (Math.floor (numer) != numer)
	throw new Teeny_error("");
      double denom = ((Double) args [1]).doubleValue();
      if (denom == 0 || Math.floor (denom) != denom)
	throw new Teeny_error("");
      return new Double ((double) ((long) numer % (long) denom));
      // return new Num (Math.IEEEremainder (numer, denom));
      // what does that do with negative numbers? 
    }
    case LT:
      return Teeny.make_flag (((Double) args [0]).doubleValue() < 
			      ((Double) args [1]).doubleValue());
    case RANDOM:
      return new Double (
		  Math.floor (((Double) args [0]).doubleValue() 
			      * Math.random())); //i.random.nextDouble ()));

    case DEFMAC:
      if (!(args [0] instanceof Symbol))
	throw new Teeny_error ("Bad argument type", args [0]);
      if (!(args [1] instanceof Callable))
	throw new Teeny_error ("Bad argument type", args [1]);
      Teeny.getInstance ().get_macro_env ().put ((Symbol) args [0], args [1]); 
      return Boolean.TRUE;
    
    case LOAD:
      if (!(args [0] instanceof String))
	throw new Teeny_error ("Bad argument type", args [0]);
      try {
	Interpreter.evaluate_file ((String) args [0]);
      }
      catch (java.io.IOException e) { 
        throw new Teeny_error ("I/O error: " + e);
      }
      return Boolean.TRUE;

    case ASK:
      try {
	return Java_invocation.invoke_method (
		 args [0], 
		 (String) args [1],
		 ((List) args [2]).to_vector ()
	       );
      }
      catch (Exception e) { e.printStackTrace(); return Boolean.FALSE; }

    case ASK_S:
      try {
	return Java_invocation.invoke_method (
		 Class.forName ((String) args [0]),
		 null, 
		 (String) args [1], 
		 ((List) args [2]).to_vector ()
	       );
      }
      catch (Exception e) { System.out.println(e); return Boolean.FALSE; }

    case GETF_S:
      try {
	return Class.forName ((String) args [0])
	            .getField ((String) args [1])
	            .get (null);
      } catch (Exception e) { System.out.println(e); return Boolean.FALSE; }

    case GETF:
      try {
	return Class.forName ((String) args [0])
	            .getField ((String) args [1]) 
                    .get (args [2]);
      } catch (Exception e) { System.out.println(e); return Boolean.FALSE; }

    case MAKE:
      try {
	return Java_invocation.instantiate_object (
		 Class.forName ((String) args [0]),
		 ((List) args [1]).to_vector ()
	       );
      }
      catch (Exception e) { System.out.println(e); return Boolean.FALSE; }

    case MK_VEC: 
      {
	int n = (int) (((Double) args [0]).doubleValue ());
	if (n < 0)
	  throw new Teeny_error ("Negative array size", args [0]);
	Object[] vec = new Object [n];
	for (int i = 0; i < n; ++i)
	  vec [i] = args [1];
	return vec;
      }

    case VEC_REF:
      {
	Object[] vec = (Object[]) args [0];
	int n = (int) (((Double) args [1]).doubleValue ());
	return vec [n];
      }

    case VEC_SET:
      {
	Object[] vec = (Object[]) args [0];
	int n = (int) (((Double) args [1]).doubleValue ());
	return vec [n] = args [2];
      }

    case VEC_LEN:
      return new Double (((Object[]) args [0]).length);

    case VECTORP:
      return Teeny.make_flag (args [0] instanceof Object[]);

    case VECTOR:
      return args;

    case VEC2LIST:
      return List.from_vector ((Object[]) args [0]);

    case LIST2VEC:
      return ((List) args [0]).to_vector ();

    default:
      throw new Teeny_error ("BUG: unknown primitive procedure id", this);
    }
  }  

}


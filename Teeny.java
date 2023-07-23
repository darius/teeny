// -*- C++ -*-

package teeny;

public class Teeny {

  static Teeny the_instance = new Teeny ();

  public static Teeny getInstance () { return the_instance; }

  public static final Object eof = new Symbol ("#!eof");  // another hack...

  Environment global_env = new GlobalEnvironment ();
  public Environment get_global_env () { return global_env; }

  Environment macro_env = new GlobalEnvironment ();
  public Environment get_macro_env () { return macro_env; }

  Reader reader = new Reader (System.in);
  public Reader get_reader () { return reader; }

  public static String toString (Object obj) 
  {
    if (obj == null)
      return "#!null";
    if (obj instanceof Boolean)
      return "#" + (((Boolean) obj).booleanValue () ? "t" : "f");
    if (obj instanceof Object[])
      return "#" + List.from_vector ((Object[]) obj).toString ();
    if (obj instanceof String)
      return "\"" + obj.toString () + "\""; // need to insert escape sequences
    return obj.toString ();
  }

  public static Object evaluate (Object expression, Environment env) 
  {
    if (expression instanceof Evaluable)
      return ((Evaluable) expression).evaluate (env);
    return expression;
  }

  public static Object apply (Object procedure, Object[] args)
  {
    if (procedure instanceof Callable)
      return ((Callable) procedure).apply (args);
    throw new Teeny_error ("Call to a non-procedure", procedure);
  }

  public static Boolean make_flag (boolean b)
  {
    return b ? Boolean.TRUE : Boolean.FALSE;
  }

  public static boolean is_true (Object obj)
  {
    return !(obj instanceof Boolean)
      || ((Boolean) obj).booleanValue ();
  }

}

/* -*- c++ -*- */

package teeny;

/*
 */

public class Closure implements Callable {

  Environment env;
  Symbol[] params;
  Object body;

  public Closure (Environment _env, Symbol[] _params, Object _body)
  { 
    env = _env;
    params = _params;
    body = _body;
  }

  public String toString () { return "#<procedure " + body + ">"; }

  public Object apply (Object[] args)
  {
    if (args.length != params.length)
      throw new Teeny_error ("Bad arg count to procedure", this);

    return Teeny.evaluate (body, new EnvFrame (params, args, env));
  }  

}


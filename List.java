// -*- C++ -*-

package teeny;

/*
 * 
 */

public abstract class List {

  public static final Nil nil = new Nil ();

  public abstract int length ();

  public abstract List nreverse ();

  public abstract Object[] to_vector ();

  public String toString () { 
    StringBuffer buf = new StringBuffer ();
    buf.append ('(');
    ListEnumeration rest = new ListEnumeration (this);
    if (rest.hasMoreElements ()) {
      buf.append (Teeny.toString (rest.nextElement ()));
      while (rest.hasMoreElements ()) {
	buf.append (' ');
	buf.append (Teeny.toString (rest.nextElement ()));
      }
    }
    buf.append (')');
    return buf.toString ();
  }

  public static List as_list (Object obj)
  {
    if (!(obj instanceof List))
      throw new Teeny_error ("Bad argument type", obj);
    return (List) obj;
  }

  public static List from_vector (Object[] vec)
  {
    List result = nil;
    for (int i = vec.length - 1; 0 <= i; --i)
      result = new Pair (vec [i], result);
    return result;
  }

}


// -*- C++ -*-

package teeny;

import java.util.*; 

/*
 * 
 */

public class ListEnumeration implements Enumeration {

  List list;

  public ListEnumeration (List _list) { list = _list; }

  public boolean hasMoreElements () {
    return list instanceof Pair;   // yeah, it sucks...
  }

  public Object nextElement () {
     if (!(list instanceof Pair))
       throw new NoSuchElementException ();
     Pair p = (Pair) list;
     list = p.cdr ();
     return p.car ();
  }

}


package teeny;

import java.lang.reflect.*;
import java.util.*;

public class Java_invocation {

  public static Object 
  invoke_method (Object target, String method_name)
    throws NoSuchMethodException, 
           InvocationTargetException, 
           IllegalAccessException 
  {
    Object[] args = {};
    return invoke_method (target.getClass (), target, method_name, args);
  }

  public static Object 
  invoke_method (Object target, String method_name, Object arg)
    throws NoSuchMethodException, 
           InvocationTargetException, 
           IllegalAccessException 
  {
    Object[] args = { arg };
    return invoke_method (target.getClass (), target, method_name, args);
  }

  public static Object 
  invoke_method (Object target, String method_name, Object[] args)
    throws NoSuchMethodException, 
           InvocationTargetException, 
           IllegalAccessException 
  {
    return invoke_method (target.getClass (), target, method_name, args);
  }

  public static Object 
  invoke_method (Class target_class,
		 Object target,
		 String method_name, 
		 Object[] args)
    throws NoSuchMethodException, 
           InvocationTargetException, 
           IllegalAccessException  
  {
    Method[] methods = target_class.getMethods ();
    Method match = null;

    for (int i = 0; i < methods.length; i++) {
      if ((methods [i].getName ().equals (method_name)) &&
	  (types_match (methods [i].getParameterTypes (), args)))
	match = methods[i];
    }

    if (match == null)
      throw new NoSuchMethodException (target_class.getName()+"."+method_name);

    retype_args (match.getParameterTypes(), args);

    Object result = match.invoke (target, args);

    return result;
  }

  public static Object 
  instantiate_object (Class target_class, Object[] args)
    throws NoSuchMethodException, 
           InvocationTargetException, 
           IllegalAccessException,
	   InstantiationException
  {
    Constructor[] constructors = target_class.getDeclaredConstructors ();
    Constructor match = null;

    for (int i = 0; i < constructors.length; i++) {
      if (types_match (constructors [i].getParameterTypes (), args))
	match = constructors [i];
    }

    if (match == null)
      throw new NoSuchMethodException ("No matching constructor.");

    retype_args (match.getParameterTypes (), args);

    Object result = match.newInstance (args);
    return result;
  }

  protected static boolean 
  types_match (Class[] types, Object[] objects) 
  {
    if (types.length != objects.length)
      return false;

    for (int i = 0; i < types.length; i++) {

      if (types[i].isPrimitive ()) {
	if (types [i].equals (Boolean.TYPE) &&
	    !(objects [i] instanceof Boolean))
	  return false;
	if (types [i].equals (Character.TYPE) &&
	    !(objects [i] instanceof Character))
	  return false;
	if (!(objects [i] instanceof Number))
	  return false;
      }
      else if (types [i].isArray () &&
	       objects [i].getClass ().isArray ()) {
	Class component_type = types [i].getComponentType ();
	for (int j = 0; j < Array.getLength (objects [i]); j++) {
	  Class array_type = Array.get (objects [i], j).getClass ();
	  if (!(component_type.isAssignableFrom (array_type)))
	    return false;
	}
      }
      else if (objects [i] != null && !(types [i].isInstance (objects [i])))
	return false;
    }

    return true;
  }

  protected static void 
  retype_args (Class[] types, Object[] args) 
  {
    for (int i = 0; i < types.length; i++) {

      if (types [i].isArray ()) {
	Class type = types [i].getComponentType ();
	Object array = Array.newInstance (type, Array.getLength (args [i]));

	for (int j = 0; j < Array.getLength (args [i]); j++)
	  Array.set (array, j, Array.get (args [i], j));

	args[i] = array;
      }

      if (types [i].isPrimitive ()) {
	if (types [i].equals (Boolean.TYPE)) continue;

	if (types [i].equals (Character.TYPE)) continue;

	Number number = (Number) args [i];
	if      (types [i].equals (Integer.TYPE))
	  args [i] = new Integer (number.intValue ());
	else if (types [i].equals (Long.TYPE))
	  args [i] = new Long (number.longValue ());
	else if (types [i].equals (Float.TYPE))
	  args [i] = new Float (number.floatValue ());
	else if (types [i].equals (Double.TYPE))
	  args [i] = new Double (number.doubleValue ());
	else if (types [i].equals (Short.TYPE))
	  args [i] = new Short (number.shortValue ());
	else
	  throw new Teeny_error ("BUG: bad primitive type");
      }
    }
  }

}



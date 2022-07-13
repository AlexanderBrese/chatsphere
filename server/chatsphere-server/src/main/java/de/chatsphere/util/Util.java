package de.chatsphere.util;

import de.chatsphere.api.shared.transfer.Input;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;

/**
 * The Util class provides helper methods shared across all classes.
 */
public class Util {

  /**
   * Gets the package name of a directory.
   *
   * @param dir the directory
   *
   * @return the package name
   */
  public static String getPackageName(String dir) {
    if (!dir.contains("/")) {
      return null;
    }
    dir = dir.replace("/", ".");
    return dir.substring(1);
  }

  /**
   * Helper method that looks up every file matching a certain file type.
   *
   * @param root the root path to look from
   * @param type the file type
   *
   * @return found files
   */
  public static List<File> getFilesByType(File root, String type) {
    List<File> found = new ArrayList<>();

    if (isEligibleFile(root, type)) {
      found.add(root);
    } else if (root.isDirectory()) {
      for (File file : Objects.requireNonNull(root.listFiles())) {
        found.addAll(getFilesByType(file, type));
      }
    }

    return found;
  }

  /**
   * Checks if the file matches file type.
   *
   * @param file the file
   * @param type the file type
   *
   * @return true/false
   */
  public static boolean isEligibleFile(File file, String type) {
    return file.isFile() && file.getName().endsWith("." + type);
  }

  /**
   * Compiles a class from a file.
   *
   * @param file the file
   */
  public static void compileClass(File file) {
    String path = FilenameUtils.removeExtension(file.getPath()) + ".java";

    try {
      Runtime.getRuntime().exec("javac " + path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the class.
   *
   * @param packageName the package name
   *
   * @return the class
   */
  public static Class<?> getClass(String packageName) {
    Class<?> clazz = null;
    try {
      clazz = Class.forName(packageName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return clazz;
  }

  public static Class<?> getClass(File file, String packageName) {
    Util.compileClass(file);
    return Util.getClass(packageName);
  }

  /**
   * Gets the class instance.
   *
   * @param clazz the class
   *
   * @return the class instance
   */
  public static Object getInstance(Class<?> clazz) {
    Object instance = null;

    try {
      assert clazz != null;
      instance = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return instance;
  }


  /**
   * Gets a file from a file name.
   *
   * @param fileName the file name
   *
   * @return the file
   */
  public static File getFileFromName(String fileName) {
    File file = null;
    try {
      URI uri = Objects.requireNonNull(Util.class.getClassLoader().getResource(fileName)).toURI();
      file = new File(uri);
    } catch (URISyntaxException | NullPointerException e) {
      e.printStackTrace();
    }
    return file;
  }

  /**
   * Converts a received map into a desired input object.
   *
   * @param arguments the arguments containing the map
   * @param clazz     the input type
   * @param <T>       generic input type
   *
   * @return the input object
   */
  public static <T extends Input> T convertMap(Map<String, Object> arguments, Class<T> clazz)
    throws NullPointerException {
    String argumentName = toCamelCase(clazz.getSimpleName());

    @SuppressWarnings("unchecked")
    Map<String, Object> map = (HashMap<String, Object>) arguments.get(argumentName);
    T input = null;
    try {
      input = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return clazz.cast(Objects.requireNonNull(input).fromMap(map));
  }

  /**
   * Gives a first character lower-case representation of a class name.
   *
   * @param className the class name
   *
   * @return the representation
   */
  private static String toCamelCase(String className) {
    char[] c = className.toCharArray();
    c[0] = Character.toLowerCase(c[0]);
    return new String(c);
  }

  /**
   * The whole query is converted to Map&lt;String, Object&gt;, however as the whole query contains
   * additional nested objects those require to be parsed as well.
   *
   * <p>So convert the "Object"-part of the Map&lt;String, Object&gt; to another map.
   *
   * @param object - which was nested inside
   *
   * @return map
   */
  public static Map<String, Object> toMap(Object object) {
    if (object instanceof Map) {
      Map<?, ?> inputVars = (Map<?, ?>) object;
      Map<String, Object> vars = new HashMap<>();
      inputVars.forEach((k, v) -> vars.put(String.valueOf(k), v));
      return vars;
    }
    return JsonBuilder.toMap(String.valueOf(object));
  }

  /**
   * Checks if the map has the object.
   *
   * @param map    the map
   * @param object the object
   *
   * @return true/false
   */
  public static boolean hasObject(Map<String, Object> map, String object) {
    return map.containsKey(object)
      && map.get(object) != null
      && !map.get(object).toString().equals("{}");
  }
}

package com.hms_networks.americas.sc.extensions.util;

/**
 * Utility class for comparing raw number values. A raw {@link Number} value is a value stored as an
 * {@link Object} that is one of the following types:
 *
 * <ul>
 *   <li>{@link Integer}
 *   <li>{@link Long}
 *   <li>{@link Float}
 *   <li>{@link Double}
 *   <li>{@link Short}
 *   <li>{@link Byte}
 * </ul>
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.15.7
 */
public class RawNumberValueUtils {

  /**
   * Get and return the minimum value of the two specified values.
   *
   * <p>Map of input type (either value) to return/output types:
   *
   * <ul>
   *   <li>{@link Double} =&gt; {@link Double}
   *   <li>{@link Float} =&gt; {@link Float}
   *   <li>{@link Long} =&gt; {@link Long}
   *   <li>{@link Integer} =&gt; {@link Integer}
   *   <li>{@link Short} =&gt; {@link Integer}
   *   <li>{@link Byte} =&gt; {@link Integer}
   * </ul>
   *
   * @param value1 first value
   * @param value2 second value
   * @return minimum value
   * @throws IllegalArgumentException if either value is {@code null}, or if either value is not a
   *     {@link Number}.
   * @since 1.15.7
   */
  public static Number getValueMin(Object value1, Object value2) {
    // Check for nulls
    if (value1 == null || value2 == null) {
      throw new IllegalArgumentException("The values must be non-null.");
    }

    // Check if values are numbers
    if (!(value1 instanceof Number) || !(value2 instanceof Number)) {
      throw new IllegalArgumentException(
          "The values must be numbers (instances of Number superclass).");
    }

    // Get values as numbers
    Number value1Number = (Number) value1;
    Number value2Number = (Number) value2;

    // Perform comparison
    Number result;
    if (value1 instanceof Double || value2 instanceof Double) {
      result = new Double(Math.min(value1Number.doubleValue(), value2Number.doubleValue()));
    } else if (value1 instanceof Float || value2 instanceof Float) {
      result = new Float(Math.min(value1Number.floatValue(), value2Number.floatValue()));
    } else if (value1 instanceof Long || value2 instanceof Long) {
      result = new Long(Math.min(value1Number.longValue(), value2Number.longValue()));
    } else {
      // Integer, Short, or Byte (all promote to Integer)
      result = new Integer(Math.min(value1Number.intValue(), value2Number.intValue()));
    }

    return result;
  }

  /**
   * Get and return the maximum value of the two specified values.
   *
   * <p>Map of input type (either value) to return/output types:
   *
   * <ul>
   *   <li>{@link Double} =&gt; {@link Double}
   *   <li>{@link Float} =&gt; {@link Float}
   *   <li>{@link Long} =&gt; {@link Long}
   *   <li>{@link Integer} =&gt; {@link Integer}
   *   <li>{@link Short} =&gt; {@link Integer}
   *   <li>{@link Byte} =&gt; {@link Integer}
   * </ul>
   *
   * @param value1 first value
   * @param value2 second value
   * @return maximum value
   * @throws IllegalArgumentException if either value is {@code null}, or if either value is not a
   *     {@link Number}.
   * @since 1.15.7
   */
  public static Number getValueMax(Object value1, Object value2) {
    // Check for nulls
    if (value1 == null || value2 == null) {
      throw new IllegalArgumentException("The values must be non-null.");
    }

    // Check if values are numbers
    if (!(value1 instanceof Number) || !(value2 instanceof Number)) {
      throw new IllegalArgumentException(
          "The values must be numbers (instances of Number superclass).");
    }

    // Get values as numbers
    Number value1Number = (Number) value1;
    Number value2Number = (Number) value2;

    // Perform comparison
    Number result;
    if (value1 instanceof Double || value2 instanceof Double) {
      result = new Double(Math.max(value1Number.doubleValue(), value2Number.doubleValue()));
    } else if (value1 instanceof Float || value2 instanceof Float) {
      result = new Float(Math.max(value1Number.floatValue(), value2Number.floatValue()));
    } else if (value1 instanceof Long || value2 instanceof Long) {
      result = new Long(Math.max(value1Number.longValue(), value2Number.longValue()));
    } else {
      // Integer, Short, or Byte (all promote to Integer)
      result = new Integer(Math.max(value1Number.intValue(), value2Number.intValue()));
    }

    return result;
  }

  /**
   * Get and return the sum of the specified values.
   *
   * <p>Map of input type (any value) to return/output types:
   *
   * <ul>
   *   <li>{@link Double} =&gt; {@link Double}
   *   <li>{@link Float} =&gt; {@link Float}
   *   <li>{@link Long} =&gt; {@link Long}
   *   <li>{@link Integer} =&gt; {@link Integer}
   *   <li>{@link Short} =&gt; {@link Integer}
   *   <li>{@link Byte} =&gt; {@link Integer}
   * </ul>
   *
   * @param values values to sum
   * @return sum of values
   * @throws IllegalArgumentException if values is {@code null}, if the values array is empty, if
   *     any value in the array is null, or if any value is not a {@link Number}.
   * @since 1.15.7
   */
  public static Number getValueSum(Object[] values) {
    boolean defaultPromoteOverflow = false;
    return getValueSum(values, defaultPromoteOverflow);
  }

  /**
   * Get and return the sum of the specified values with the option to promote the returned result
   * to the next largest type ({@link Long}) if an integer overflow occurs.
   *
   * <p>Map of input type (any value) to return/output types:
   *
   * <ul>
   *   <li>{@link Double} =&gt; {@link Double}
   *   <li>{@link Float} =&gt; {@link Float}
   *   <li>{@link Long} =&gt; {@link Long}
   *   <li>{@link Integer} =&gt; {@link Integer}
   *   <li>{@link Short} =&gt; {@link Integer}
   *   <li>{@link Byte} =&gt; {@link Integer}
   * </ul>
   *
   * @param values values to sum
   * @param promoteOverflow {@code true} to promote to the returned result to the next largest type
   *     ({@link Long}) if an integer overflow occurs. Otherwise, {@code false} will return the sum
   *     as an {@link Integer} even if an integer overflow occurs, which is the default behavior and
   *     mimics the JVM behavior should an integer overflow occur.
   * @return sum of values
   * @throws IllegalArgumentException if values is {@code null}, if the values array is empty, if
   *     any value in the array is null, or if any value is not a {@link Number}.
   * @since 1.15.7
   */
  public static Number getValueSum(Object[] values, boolean promoteOverflow) {
    // Check for nulls
    if (values == null) {
      throw new IllegalArgumentException("The values array must be non-null.");
    }

    // Check for empty array
    if (values.length == 0) {
      throw new IllegalArgumentException("The values array must not be empty.");
    }

    // Create variables to track sum/types
    double sumDouble = 0.0;
    float sumFloat = 0.0f;
    long sumLong = 0;
    int sumInt = 0;
    boolean isDouble = false;
    boolean isFloat = false;
    boolean isLong = false;
    boolean isIntOverflow = false;

    // Loop through values
    for (int i = 0; i < values.length; i++) {
      // Get value
      Object value = values[i];

      // Check for null
      if (value == null) {
        throw new IllegalArgumentException("The values must be non-null.");
      }

      // Check if value is a number
      if (!(value instanceof Number)) {
        throw new IllegalArgumentException(
            "The values must be numbers (instance of Number superclass).");
      }

      // Get value as number
      Number valueNumber = (Number) value;

      // Check for integer overflow
      if (willAddOverflowInt(sumInt, valueNumber.intValue())) {
        isIntOverflow = true;
      }

      // Add value to sum
      sumDouble += valueNumber.doubleValue();
      sumFloat += valueNumber.floatValue();
      sumLong += valueNumber.longValue();
      sumInt += valueNumber.intValue();

      // Check if value is double
      if (value instanceof Double) {
        isDouble = true;
      }

      // Check if value is float
      if (value instanceof Float) {
        isFloat = true;
      }

      // Check if value is long
      if (value instanceof Long) {
        isLong = true;
      }
    }

    // Create result variable (promote to long if overflow and promoteOverflow is true)
    boolean promoteOverflowToLong = promoteOverflow && isIntOverflow;
    Number result;
    if (isDouble) {
      result = new Double(sumDouble);
    } else if (isFloat) {
      result = new Float(sumFloat);
    } else if (isLong || promoteOverflowToLong) {
      result = new Long(sumLong);
    } else if (isIntOverflow) {
      result = new Integer((int) sumLong);
    } else {
      result = new Integer(sumInt);
    }
    return result;
  }

  /**
   * Private utility method to check if adding the specified values will cause an integer overflow.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @return {@code true} if adding the values will cause an integer overflow, {@code false}
   *     otherwise
   * @since 1.15.7
   */
  private static boolean willAddOverflowInt(int value1, int value2) {
    return value1 > 0 && value2 > 0 && value1 + value2 < 0
        || value1 < 0 && value2 < 0 && value1 + value2 > 0;
  }

  /**
   * Get and return the average of the specified values.
   *
   * <p>Map of input type (any value) to return/output types:
   *
   * <ul>
   *   <li>{@link Double} =&gt; {@link Double}
   *   <li>{@link Float} =&gt; {@link Float}
   *   <li>{@link Long} =&gt; {@link Long}
   *   <li>{@link Integer} =&gt; {@link Integer}
   *   <li>{@link Short} =&gt; {@link Integer}
   *   <li>{@link Byte} =&gt; {@link Integer}
   * </ul>
   *
   * @param values values to average
   * @return average of values
   * @throws IllegalArgumentException if values is {@code null}, if the values array is empty, if
   *     any value in the array is null, or if any value is not a {@link Number}.
   * @since 1.15.7
   */
  public static Number getValueAvg(Object[] values) {
    // Check for nulls
    if (values == null) {
      throw new IllegalArgumentException("The values array must be non-null.");
    }

    // Check for empty array
    if (values.length == 0) {
      throw new IllegalArgumentException("The values array must not be empty.");
    }

    // Get sum of values
    Number sum = getValueSum(values);

    // Get and return average of values
    return getValueDivide(sum, new Integer(values.length));
  }

  /**
   * Get and return the value of the specified value divided by the specified divisor.
   *
   * <p>Map of input type (either value or divisor) to return/output types:
   *
   * <ul>
   *   <li>{@link Double} =&gt; {@link Double}
   *   <li>{@link Float} =&gt; {@link Float}
   *   <li>{@link Long} =&gt; {@link Long}
   *   <li>{@link Integer} =&gt; {@link Integer}
   *   <li>{@link Short} =&gt; {@link Integer}
   *   <li>{@link Byte} =&gt; {@link Integer}
   * </ul>
   *
   * @param value the value to divide
   * @param divisor the divisor
   * @return the value divided by the divisor
   * @throws IllegalArgumentException if the value or divisor is {@code null}, or if the value or
   *     divisor is not a {@link Number}.
   * @since 1.15.7
   */
  public static Number getValueDivide(Object value, Object divisor) {
    // Check for nulls
    if (value == null || divisor == null) {
      throw new IllegalArgumentException("The value and/or divisor must be non-null.");
    }

    // Check if value and divisor are numbers
    if (!(value instanceof Number) || !(divisor instanceof Number)) {
      throw new IllegalArgumentException(
          "The value and divisor must be numbers (instance of Number superclass).");
    }

    // Check if divisor is zero
    if (((Number) divisor).doubleValue() == 0.0) {
      throw new IllegalArgumentException("The divisor cannot be zero.");
    }

    // Get value and divisor as numbers
    Number valueNumber = (Number) value;
    Number divisorNumber = (Number) divisor;

    // Perform division
    Number result;
    if (value instanceof Double || divisor instanceof Double) {
      result = new Double(valueNumber.doubleValue() / divisorNumber.doubleValue());
    } else if (value instanceof Float || divisor instanceof Float) {
      result = new Float(valueNumber.floatValue() / divisorNumber.floatValue());
    } else if (value instanceof Long || divisor instanceof Long) {
      result = new Long(valueNumber.longValue() / divisorNumber.longValue());
    } else {
      // Integer, Short, or Byte (all promote to Integer)
      result = new Integer(valueNumber.intValue() / divisorNumber.intValue());
    }

    return result;
  }
}

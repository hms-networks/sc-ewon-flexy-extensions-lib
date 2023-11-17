package com.hms_networks.americas.sc.extensions.util;

import java.util.Random;
import junit.framework.TestCase;

/**
 * Library test class for the {@link
 * RawNumberValueUtils} class in the Ewon Flexy
 * Extensions Library.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.15.7
 * @version 1.0.0
 */
public class RawNumberValueUtilsTest extends TestCase {

  /**
   * Number of test runs to perform for each test case.
   *
   * @since 1.0.0
   */
  private static final int NUM_TEST_RUNS = 100;

  /**
   * Number of test values to use for sum and average tests.
   *
   * @since 1.0.0
   */
  private static final int NUM_TEST_VALUES = 100;

  /**
   * Random generator for creating random test values.
   *
   * @since 1.0.0
   */
  private static final Random RANDOM = new Random();

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMin(Object, Object)} method
   * returns the expected value for two {@link Double} objects as input. The returned value should
   * be the minimum of the two input values and should be of type {@link Double}.
   *
   * @since 1.0.0
   */
  public void testGetValueMinDoubleDouble() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      double a = RANDOM.nextDouble();
      double b = RANDOM.nextDouble();
      double expected = Math.min(a, b);
      Number actual = RawNumberValueUtils.getValueMin(new Double(a), new Double(b));
      assertTrue(actual instanceof Double);
      Double actualDouble = (Double) actual;
      final double equalsDelta = 0.0;
      assertEquals(expected, actualDouble.doubleValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMin(Object, Object)} method
   * returns the expected value for two {@link Float} objects as input. The returned value should be
   * the minimum of the two input values and should be of type {@link Float}.
   *
   * @since 1.0.0
   */
  public void testGetValueMinFloatFloat() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      float a = RANDOM.nextFloat();
      float b = RANDOM.nextFloat();
      float expected = Math.min(a, b);
      Number actual = RawNumberValueUtils.getValueMin(new Float(a), new Float(b));
      assertTrue(actual instanceof Float);
      Float actualFloat = (Float) actual;
      final float equalsDelta = 0.0f;
      assertEquals(expected, actualFloat.floatValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMin(Object, Object)} method
   * returns the expected value for two {@link Long} objects as input. The returned value should be
   * the minimum of the two input values and should be of type {@link Long}.
   *
   * @since 1.0.0
   */
  public void testGetValueMinLongLong() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      long a = RANDOM.nextLong();
      long b = RANDOM.nextLong();
      long expected = Math.min(a, b);
      Number actual = RawNumberValueUtils.getValueMin(new Long(a), new Long(b));
      assertTrue(actual instanceof Long);
      Long actualLong = (Long) actual;
      assertEquals(expected, actualLong.longValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMin(Object, Object)} method
   * returns the expected value for two {@link Integer} objects as input. The returned value should
   * be the minimum of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueMinIntInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      int a = RANDOM.nextInt();
      int b = RANDOM.nextInt();
      int expected = Math.min(a, b);
      Number actual = RawNumberValueUtils.getValueMin(new Integer(a), new Integer(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.intValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMin(Object, Object)} method
   * returns the expected value for two {@link Short} objects as input. The returned value should be
   * the minimum of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueMinShortInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      short a = (short) RANDOM.nextInt();
      short b = (short) RANDOM.nextInt();
      short expected = (short) Math.min(a, b);
      Number actual = RawNumberValueUtils.getValueMin(new Short(a), new Short(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.shortValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMin(Object, Object)} method
   * returns the expected value for two {@link Byte} objects as input. The returned value should be
   * the minimum of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueMinByteInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      byte a = (byte) RANDOM.nextInt();
      byte b = (byte) RANDOM.nextInt();
      byte expected = (byte) Math.min(a, b);
      Number actual = RawNumberValueUtils.getValueMin(new Byte(a), new Byte(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.byteValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMax(Object, Object)} method
   * returns the expected value for two {@link Double} objects as input. The returned value should
   * be the maximum of the two input values and should be of type {@link Double}.
   *
   * @since 1.0.0
   */
  public void testGetValueMaxDoubleDouble() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      double a = RANDOM.nextDouble();
      double b = RANDOM.nextDouble();
      double expected = Math.max(a, b);
      Number actual = RawNumberValueUtils.getValueMax(new Double(a), new Double(b));
      assertTrue(actual instanceof Double);
      Double actualDouble = (Double) actual;
      final double equalsDelta = 0.0;
      assertEquals(expected, actualDouble.doubleValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMax(Object, Object)} method
   * returns the expected value for two {@link Float} objects as input. The returned value should be
   * the maximum of the two input values and should be of type {@link Float}.
   *
   * @since 1.0.0
   */
  public void testGetValueMaxFloatFloat() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      float a = RANDOM.nextFloat();
      float b = RANDOM.nextFloat();
      float expected = Math.max(a, b);
      Number actual = RawNumberValueUtils.getValueMax(new Float(a), new Float(b));
      assertTrue(actual instanceof Float);
      Float actualFloat = (Float) actual;
      final float equalsDelta = 0.0f;
      assertEquals(expected, actualFloat.floatValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMax(Object, Object)} method
   * returns the expected value for two {@link Long} objects as input. The returned value should be
   * the maximum of the two input values and should be of type {@link Long}.
   *
   * @since 1.0.0
   */
  public void testGetValueMaxLongLong() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      long a = RANDOM.nextLong();
      long b = RANDOM.nextLong();
      long expected = Math.max(a, b);
      Number actual = RawNumberValueUtils.getValueMax(new Long(a), new Long(b));
      assertTrue(actual instanceof Long);
      Long actualLong = (Long) actual;
      assertEquals(expected, actualLong.longValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMax(Object, Object)} method
   * returns the expected value for two {@link Integer} objects as input. The returned value should
   * be the maximum of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueMaxIntInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      int a = RANDOM.nextInt();
      int b = RANDOM.nextInt();
      int expected = Math.max(a, b);
      Number actual = RawNumberValueUtils.getValueMax(new Integer(a), new Integer(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.intValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMax(Object, Object)} method
   * returns the expected value for two {@link Short} objects as input. The returned value should be
   * the maximum of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueMaxShortInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      short a = (short) RANDOM.nextInt();
      short b = (short) RANDOM.nextInt();
      short expected = (short) Math.max(a, b);
      Number actual = RawNumberValueUtils.getValueMax(new Short(a), new Short(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.shortValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueMax(Object, Object)} method
   * returns the expected value for two {@link Byte} objects as input. The returned value should be
   * the maximum of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueMaxByteInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      byte a = (byte) RANDOM.nextInt();
      byte b = (byte) RANDOM.nextInt();
      byte expected = (byte) Math.max(a, b);
      Number actual = RawNumberValueUtils.getValueMax(new Byte(a), new Byte(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.byteValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueSum(Object[])} method
   * returns the expected value for an array of {@link Double} objects as input. The returned value
   * should be the sum of the input values and should be of type {@link Double}.
   *
   * @since 1.0.0
   */
  public void testGetValueSumDoubleDouble() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      double expected = 0.0;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        double value = RANDOM.nextDouble();
        values[j] = new Double(value);
        expected += value;
      }

      Number actual = RawNumberValueUtils.getValueSum(values);
      assertTrue(actual instanceof Double);
      Double actualDouble = (Double) actual;
      final double equalsDelta = 0.0;
      assertEquals(expected, actualDouble.doubleValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueSum(Object[])} method
   * returns the expected value for an array of {@link Float} objects as input. The returned value
   * should be the sum of the input values and should be of type {@link Float}.
   *
   * @since 1.0.0
   */
  public void testGetValueSumFloatFloat() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      float expected = 0.0f;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        float value = RANDOM.nextFloat();
        values[j] = new Float(value);
        expected += value;
      }

      Number actual = RawNumberValueUtils.getValueSum(values);
      assertTrue(actual instanceof Float);
      Float actualFloat = (Float) actual;
      final float equalsDelta = 0.0f;
      assertEquals(expected, actualFloat.floatValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueSum(Object[])} method
   * returns the expected value for an array of {@link Long} objects as input. The returned value
   * should be the sum of the input values and should be of type {@link Long}.
   *
   * @since 1.0.0
   */
  public void testGetValueSumLongLong() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      long expected = 0;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        long value = RANDOM.nextLong();
        values[j] = new Long(value);
        expected += value;
      }

      Number actual = RawNumberValueUtils.getValueSum(values);
      assertTrue(actual instanceof Long);
      Long actualLong = (Long) actual;
      assertEquals(expected, actualLong.longValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueSum(Object[])} method
   * returns the expected value for an array of {@link Integer} objects as input. The returned value
   * should be the sum of the input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueSumIntInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      int expected = 0;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        int value = RANDOM.nextInt();
        values[j] = new Integer(value);
        expected += value;
      }

      Number actual = RawNumberValueUtils.getValueSum(values);
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.intValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueSum(Object[])} method
   * returns the expected value for an array of {@link Short} objects as input. The returned value
   * should be the sum of the input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueSumShortInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      short expected = 0;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        short value = (short) RANDOM.nextInt();
        values[j] = new Short(value);
        expected += value;
      }

      Number actual = RawNumberValueUtils.getValueSum(values);
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.shortValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueSum(Object[])} method
   * returns the expected value for an array of {@link Byte} objects as input. The returned value
   * should be the sum of the input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueSumByteInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      byte expected = 0;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        byte value = (byte) RANDOM.nextInt();
        values[j] = new Byte(value);
        expected += value;
      }

      Number actual = RawNumberValueUtils.getValueSum(values);
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.byteValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueAvg(Object[])} method
   * returns the expected value for an array of {@link Double} objects as input. The returned value
   * should be the average of the input values and should be of type {@link Double}.
   *
   * @since 1.0.0
   */
  public void testGetValueAvgDoubleDouble() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      double expected = 0.0;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        double value = RANDOM.nextDouble();
        values[j] = new Double(value);
        expected += value;
      }
      expected /= NUM_TEST_VALUES;

      Number actual = RawNumberValueUtils.getValueAvg(values);
      assertTrue(actual instanceof Double);
      Double actualDouble = (Double) actual;
      final double equalsDelta = 0.0;
      assertEquals(expected, actualDouble.doubleValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueAvg(Object[])} method
   * returns the expected value for an array of {@link Float} objects as input. The returned value
   * should be the average of the input values and should be of type {@link Float}.
   *
   * @since 1.0.0
   */
  public void testGetValueAvgFloatFloat() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      float expected = 0.0f;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        float value = RANDOM.nextFloat();
        values[j] = new Float(value);
        expected += value;
      }
      expected /= NUM_TEST_VALUES;

      Number actual = RawNumberValueUtils.getValueAvg(values);
      assertTrue(actual instanceof Float);
      Float actualFloat = (Float) actual;
      final float equalsDelta = 0.0f;
      assertEquals(expected, actualFloat.floatValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueAvg(Object[])} method
   * returns the expected value for an array of {@link Long} objects as input. The returned value
   * should be the average of the input values and should be of type {@link Long}.
   *
   * @since 1.0.0
   */
  public void testGetValueAvgLongLong() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      long expected = 0;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        long value = RANDOM.nextLong();
        values[j] = new Long(value);
        expected += value;
      }
      expected /= NUM_TEST_VALUES;

      Number actual = RawNumberValueUtils.getValueAvg(values);
      assertTrue(actual instanceof Long);
      Long actualLong = (Long) actual;
      assertEquals(expected, actualLong.longValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueAvg(Object[])} method
   * returns the expected value for an array of {@link Integer} objects as input. The returned value
   * should be the average of the input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueAvgIntInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      int expected = 0;
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        int value = RANDOM.nextInt();
        values[j] = new Integer(value);
        expected += value;
      }
      expected /= NUM_TEST_VALUES;

      Number actual = RawNumberValueUtils.getValueAvg(values);
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.intValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueAvg(Object[])} method
   * returns the expected value for an array of {@link Short} objects as input. The returned value
   * should be the average of the input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueAvgShortInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      int expected = 0; // Expected is int because short is promoted to int
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        short value = (short) RANDOM.nextInt();
        values[j] = new Short(value);
        expected += value;
      }
      expected /= NUM_TEST_VALUES;

      Number actual = RawNumberValueUtils.getValueAvg(values);
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.intValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueAvg(Object[])} method
   * returns the expected value for an array of {@link Byte} objects as input. The returned value
   * should be the average of the input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueAvgByteInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      Object[] values = new Object[NUM_TEST_VALUES];
      int expected = 0; // Expected is int because byte is promoted to int
      for (int j = 0; j < NUM_TEST_VALUES; j++) {
        byte value = (byte) RANDOM.nextInt();
        values[j] = new Byte(value);
        expected += value;
      }
      expected /= NUM_TEST_VALUES;

      Number actual = RawNumberValueUtils.getValueAvg(values);
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.intValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueDivide(Object, Object)}
   * method returns the expected value for two {@link Double} objects as input. The returned value
   * should be the quotient of the two input values and should be of type {@link Double}.
   *
   * @since 1.0.0
   */
  public void testGetValueDivideDoubleDouble() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      double a = RANDOM.nextDouble();
      double b = RANDOM.nextDouble();
      while (b == 0) {
        b = RANDOM.nextDouble();
      }
      double expected = a / b;
      Number actual = RawNumberValueUtils.getValueDivide(new Double(a), new Double(b));
      assertTrue(actual instanceof Double);
      Double actualDouble = (Double) actual;
      final double equalsDelta = 0.0;
      assertEquals(expected, actualDouble.doubleValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueDivide(Object, Object)}
   * method returns the expected value for two {@link Float} objects as input. The returned value
   * should be the quotient of the two input values and should be of type {@link Float}.
   *
   * @since 1.0.0
   */
  public void testGetValueDivideFloatFloat() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      float a = RANDOM.nextFloat();
      float b = RANDOM.nextFloat();
      while (b == 0) {
        b = RANDOM.nextFloat();
      }
      float expected = a / b;
      Number actual = RawNumberValueUtils.getValueDivide(new Float(a), new Float(b));
      assertTrue(actual instanceof Float);
      Float actualFloat = (Float) actual;
      final float equalsDelta = 0.0f;
      assertEquals(expected, actualFloat.floatValue(), equalsDelta);
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueDivide(Object, Object)}
   * method returns the expected value for two {@link Long} objects as input. The returned value
   * should be the quotient of the two input values and should be of type {@link Long}.
   *
   * @since 1.0.0
   */
  public void testGetValueDivideLongLong() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      long a = RANDOM.nextLong();
      long b = RANDOM.nextLong();
      while (b == 0) {
        b = RANDOM.nextLong();
      }
      long expected = a / b;
      Number actual = RawNumberValueUtils.getValueDivide(new Long(a), new Long(b));
      assertTrue(actual instanceof Long);
      Long actualLong = (Long) actual;
      assertEquals(expected, actualLong.longValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueDivide(Object, Object)}
   * method returns the expected value for two {@link Integer} objects as input. The returned value
   * should be the quotient of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueDivideIntInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      int a = RANDOM.nextInt();
      int b = RANDOM.nextInt();
      while (b == 0) {
        b = RANDOM.nextInt();
      }
      int expected = a / b;
      Number actual = RawNumberValueUtils.getValueDivide(new Integer(a), new Integer(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.intValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueDivide(Object, Object)}
   * method returns the expected value for two {@link Short} objects as input. The returned value
   * should be the quotient of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueDivideShortInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      short a = (short) RANDOM.nextInt();
      short b = (short) RANDOM.nextInt();
      while (b == 0) {
        b = (short) RANDOM.nextInt();
      }
      short expected = (short) (a / b);
      Number actual = RawNumberValueUtils.getValueDivide(new Short(a), new Short(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.shortValue());
    }
  }

  /**
   * Test case to verify whether the {@link RawNumberValueUtils#getValueDivide(Object, Object)}
   * method returns the expected value for two {@link Byte} objects as input. The returned value
   * should be the quotient of the two input values and should be of type {@link Integer}.
   *
   * @since 1.0.0
   */
  public void testGetValueDivideByteInt() {
    for (int i = 0; i < NUM_TEST_RUNS; i++) {
      byte a = (byte) RANDOM.nextInt();
      byte b = (byte) RANDOM.nextInt();
      while (b == 0) {
        b = (byte) RANDOM.nextInt();
      }
      byte expected = (byte) (a / b);
      Number actual = RawNumberValueUtils.getValueDivide(new Byte(a), new Byte(b));
      assertTrue(actual instanceof Integer);
      Integer actualInteger = (Integer) actual;
      assertEquals(expected, actualInteger.byteValue());
    }
  }
}

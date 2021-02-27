package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.helpers.RuntimeInstrumentation;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static edu.caltech.cs2.project03.Project03TestOrdering.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("B")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CircularArrayFixedSizeQueueTests implements FixedSizeQueueTests {
  private static String FIXED_QUEUE_SOURCE ="src/edu/caltech/cs2/datastructures/CircularArrayFixedSizeQueue.java";

  private Constructor circFixedSizeQueueConstructor = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
  private int DEFAULT_CAPACITY = 10;

  public IQueue<Object> newQueue() {
    return Reflection.newInstance(circFixedSizeQueueConstructor, DEFAULT_CAPACITY);
  }

  public IQueue<Object> newQueue(int capacity) {
    return Reflection.newInstance(circFixedSizeQueueConstructor, capacity);
  }

  public IFixedSizeQueue<Object> newFixedSizeQueue(int capacity) {
    return Reflection.newInstance(circFixedSizeQueueConstructor, capacity);
  }

  // FIXED QUEUE-SPECIFIC TESTS ----------------------------------------

  @Order(classSpecificTestLevel)
  @DisplayName("Does not use or import disallowed classes")
  @Test
  public void testForInvalidClasses() {
    List<String> regexps = List.of("java\\.util\\.(?!Iterator)", "java\\.lang\\.reflect", "java\\.io");
    Inspection.assertNoImportsOf(FIXED_QUEUE_SOURCE, regexps);
    Inspection.assertNoUsageOf(FIXED_QUEUE_SOURCE, regexps);
  }

  @Order(classSpecificTestLevel)
  @DisplayName("There are no static fields")
  @Test
  public void testConstantFields() {
    Reflection.assertFieldsEqualTo(CircularArrayFixedSizeQueue.class, "static", 0);
  }

  @Order(classSpecificTestLevel)
  @DisplayName("The overall number of fields is small")
  @Test
  public void testSmallNumberOfFields() {
    Reflection.assertFieldsLessThan(CircularArrayFixedSizeQueue.class, "private", 4);
  }

  @Order(classSpecificTestLevel)
  @DisplayName("There are no public fields")
  @Test
  public void testNoPublicFields() {
    Reflection.assertNoPublicFields(CircularArrayFixedSizeQueue.class);
  }

  @Order(classSpecificTestLevel)
  @DisplayName("The public interface is correct")
  @Test
  public void testPublicInterface() {
    Reflection.assertPublicInterface(CircularArrayFixedSizeQueue.class, List.of(
            "enqueue",
            "dequeue",
            "peek",
            "iterator",
            "size",
            "isFull",
            "capacity",
            "toString"
    ));
  }

  @Order(classSpecificTestLevel)
  @DisplayName("Uses this(...) notation in all but one constructor")
  @Test
  public void testForThisConstructors() {
    Inspection.assertConstructorHygiene(FIXED_QUEUE_SOURCE);
  }


  // TOSTRING TESTS ---------------------------------------------------

  @Order(toStringTestLevel)
  @DisplayName("toString is correctly overridden")
  @Test
  public void testToStringOverride() {
    Reflection.assertMethodCorrectlyOverridden(ArrayDeque.class, "toString");
  }

  @Order(toStringTestLevel)
  @DisplayName("toString() matches java.util.ArrayDeque")
  @ParameterizedTest(name = "Test toString() on [{arguments}]")
  @ValueSource(strings = {
          "0, 1, 2, 3", "5, 4, 3, 2, 1", "8, 3, 5, 7, 4, 3, 12, 12, 1"
  })
  public void testToString(String inputs) {
    java.util.ArrayDeque<String> reference = new java.util.ArrayDeque<String>();
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
    IFixedSizeQueue<String> me = Reflection.newInstance(c, inputs.length());
    for (String value : inputs.trim().split(", ")) {
      assertEquals(reference.toString(), me.toString(), "toString outputs should be the same");
      reference.addLast(value);
      me.enqueue(value);
    }
  }

  // TIME COMPLEXITY TESTS ------------------------------------------------

  @Order(complexityTestLevel)
  @DisplayName("enqueue() and dequeue() take constant time")
  @Test()
  public void testQueueOperationComplexity() {
    Function<Integer, IFixedSizeQueue<Integer>> provide = (Integer numElements) -> {
      Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
      IFixedSizeQueue<Integer> q = Reflection.newInstance(c, numElements*2);
      for (int i = 0; i < numElements; i++) {
        q.enqueue(i);
      }
      return q;
    };
    Consumer<IFixedSizeQueue<Integer>> enqueue = (IFixedSizeQueue<Integer> q) -> q.enqueue(0);
    Consumer<IFixedSizeQueue<Integer>> dequeue = (IFixedSizeQueue<Integer> q) -> q.dequeue();

    RuntimeInstrumentation.assertAtMost("enqueue", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, enqueue, 8);
    RuntimeInstrumentation.assertAtMost("dequeue", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, dequeue, 8);
  }


  @Order(complexityTestLevel)
  @DisplayName("peek() takes constant time")
  @Test()
  public void testPeekComplexity() {
    Function<Integer, IFixedSizeQueue<Integer>> provide = (Integer numElements) -> {
      Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
      IFixedSizeQueue<Integer> q = Reflection.newInstance(c, numElements*2);
      for (int i = 0; i < numElements; i++) {
        q.enqueue(i);
      }
      return q;
    };
    Consumer<IFixedSizeQueue<Integer>> peek = (IFixedSizeQueue<Integer> q) -> q.peek();

    RuntimeInstrumentation.assertAtMost("peek", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, peek, 8);
  }
}
package edu.luc.cs.consoleapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestSlidingQueueInteractive {

  @Test
  public void testInteractiveBehavior() {
    // the test input
    final var input = List.of("asdf", "qwer", "oiui", "zxcv");
    // the expected interaction trace
    final var expectedTrace = List.<IOEvent>of(new InputEvent("asdf"), new OutputEvent("asdf"), new InputEvent("qwer"),
      new OutputEvent("asdf", "qwer"), new InputEvent("oiui"), new OutputEvent("asdf", "qwer", "oiui"),
      new InputEvent("zxcv"), new OutputEvent("qwer", "oiui", "zxcv"));
    // create and exercise the SUT
    final var sut = new SUTWithTracing(3, input);
    sut.process();
    // // make sure the expected and actual traces match
    final var actualTrace = sut.getTrace();
    assertEquals(expectedTrace, actualTrace);
  }
}

// A mini-framework for trace-based testing of interactive behavior (WIP)

/** A common interface for user I/O events. */
interface IOEvent {
}

/** A trace event representing user input. */
record InputEvent(String value) implements IOEvent {
  @Override
  public String toString() {
    return "InputEvent(" + value + ")";
  }
}

/** A trace event representing system output. */
record OutputEvent(List<String> value) implements IOEvent {
  public OutputEvent(String... values) {
    this(Arrays.asList(values));
  }

  @Override
  public String toString() {
    return "OutputEvent(" + value + ")";
  }
}

/**
 * A wrapper (decorator) around the SUT for for tracing the precise interaction
 * between the SUT and its environment. This requires wrapping the input
 * iterator to log each input item as it is consumed, providing an output
 * handler that logs each output item as it is produced, and creating a internal
 * SUT instance with these wrapped input and output components.
 */
class SUTWithTracing {
  private final SlidingQueue sut;

  private final LinkedList<IOEvent> trace = new LinkedList<>();

  SUTWithTracing(final int capacity, final Collection<String> input) {
    this.sut = new SlidingQueue(capacity, wrapInput(input), outputToTrace());
  }

  void process() {
    sut.process();
  }

  List<IOEvent> getTrace() {
    return Collections.unmodifiableList(trace);
  }

  Iterator<String> wrapInput(final Collection<String> input) {
    return input.stream().map(value -> {
      trace.add(new InputEvent(value));
      return value;
    }).iterator();
  }

  OutputHandler outputToTrace() {
    return value -> {
      final var snapshot = new LinkedList<>(value);
      trace.add(new OutputEvent(snapshot.toArray(new String[]{})));
    };
  }
}

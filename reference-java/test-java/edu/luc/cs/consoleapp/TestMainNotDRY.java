package edu.luc.cs.consoleapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import org.junit.jupiter.api.Test;

public class TestMainNotDRY {

  @Test
  public void testSlidingWindowEmpty() {
    final var input = List.<String>of().iterator();
    final var output = new OutputToList();
    final var queue = new CircularFifoQueue<String>(3);
    input.forEachRemaining(word -> {
      queue.add(word); // the oldest item automatically gets evicted
      output.accept(queue); // send updated queue to output handler
    });
    final var result = output.result;
    assertTrue(result.isEmpty());
  }

  @Test
  public void testSlidingWindowNonempty() {
    final var input = List.of("asdf", "qwer", "oiui", "zxcv").iterator();
    final var output = new OutputToList();
    final var queue = new CircularFifoQueue<String>(3);
    input.forEachRemaining(word -> {
      queue.add(word); // the oldest item automatically gets evicted
      output.accept(queue); // send updated queue to output handler
    });
    final var result = output.result;
    assertEquals(4, result.size());
    assertEquals(List.of("asdf"), result.get(0));
    assertEquals(List.of("asdf", "qwer"), result.get(1));
    assertEquals(List.of("asdf", "qwer", "oiui"), result.get(2));
    assertEquals(List.of("qwer", "oiui", "zxcv"), result.get(3));
  }
}

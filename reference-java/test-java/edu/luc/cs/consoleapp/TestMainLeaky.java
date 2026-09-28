package edu.luc.cs.consoleapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TestMainLeaky {

  @Test
  public void testSlidingWindowEmpty() {
    final var input = List.<String>of().iterator();
    final var sut = new MainLeaky.LeakyQueue(3, input);
    final var result = sut.process();
    assertTrue(result.isEmpty());
  }

  @Test
  public void testSlidingWindowNonempty() {
    final var input = List.of("asdf", "qwer", "oiui", "zxcv").iterator();
    final var sut = new MainLeaky.LeakyQueue(3, input);
    final var result = sut.process();
    assertEquals(4, result.size());
    assertEquals(List.of("asdf"), result.get(0));
    assertEquals(List.of("asdf", "qwer"), result.get(1));
    assertEquals(List.of("asdf", "qwer", "oiui"), result.get(2));
    assertEquals(List.of("qwer", "oiui", "zxcv"), result.get(3));
  }
}

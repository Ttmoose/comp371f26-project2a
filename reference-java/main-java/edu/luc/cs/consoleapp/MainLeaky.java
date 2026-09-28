package edu.luc.cs.consoleapp;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import edu.luc.cs.consoleapp.MainLeaky.LeakyQueue;

// see https://stackoverflow.com/questions/1963806/#21699069
// why we're using this implementation instead of java.util.ArrayQueue!

public class MainLeaky {

  public static final int LAST_N_WORDS = 10;

  public static void main(final String[] args) {
    // perform argument validity checking
    if (args.length > 1) {
      System.err.println("usage: ./target/universal/stage/bin/consoleapp [ last_n_words ]");
      System.exit(2);
    }

    var lastNWords = LAST_N_WORDS;
    try {
      if (args.length == 1) {
        lastNWords = Integer.parseInt(args[0]);
        if (lastNWords < 1) {
          throw new NumberFormatException();
        }
      }
    } catch (final NumberFormatException ex) {
      System.err.println("argument should be a natural number");
      System.exit(4);
    }

    final var input = new Scanner(System.in).useDelimiter("(?U)[^\\p{Alpha}0-9']+");
    final var coreLogic = new LeakyQueue(lastNWords, input);
    final var result = coreLogic.process();

    result.forEach(value -> {
      System.out.println(value);
      // terminate on I/O error such as SIGPIPE
      if (System.out.checkError()) {
        System.exit(1);
      }
    });
  }

  /**
   * A sliding window queue that retains the last N elements. This component is
   * independent of the user interface and can be tested independently.
   * Nevertheless, it violates an important nonfunctional requirement: it leaks
   * memory. In addition, it violates the functional requirement of interactively
   * responding to each input.
   */
  static class LeakyQueue {

    private final Queue<String> queue;

    private final Iterator<String> input;

    public LeakyQueue(final int capacity, final Iterator<String> input) {
      this.queue = new CircularFifoQueue<String>(capacity);
      this.input = input;
    }

    public List<Queue<String>> process() {
      final var result = new LinkedList<Queue<String>>();
      input.forEachRemaining(word -> {
        queue.add(word); // the oldest item automatically gets evicted
        final var snapshot = new LinkedList<>(queue);
        result.add(snapshot);
      });
      return Collections.unmodifiableList(result);
    }
  }
}

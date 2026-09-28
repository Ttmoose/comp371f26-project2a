package edu.luc.cs.consoleapp;

import java.util.Iterator;
import java.util.Queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * A sliding window queue that retains the last N elements. This component is
 * independent of the user interface and can be tested independently. It takes
 * an OutputHandler to route updates to the appropriate destination.
 */
class SlidingQueue {

  protected final Queue<String> queue;

  protected final Iterator<String> input;

  protected final OutputHandler output;

  public SlidingQueue(final int queueSize, final Iterator<String> input, final OutputHandler output) {
    this.queue = new CircularFifoQueue<>(queueSize);
    this.input = input;
    this.output = output;
  }

  public void process() {
    input.forEachRemaining(word -> {
      queue.add(word); // the oldest item automatically gets evicted
      output.accept(queue); // send updated queue to output handler
    });
  }
}

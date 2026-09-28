package edu.luc.cs.consoleapp

import java.util.Queue
import org.apache.commons.collections4.queue.CircularFifoQueue

/**
 * A sliding window queue that retains the last N elements. This component is
 * independent of the user interface and can be tested independently. It takes
 * an OutputHandler to route updates to the appropriate destination.
 */
class SlidingQueue(
  queueSize: Int,
  input: Iterator[String],
  output: OutputHandler
):

  protected val queue: Queue[String] = new CircularFifoQueue[String](queueSize)

  def process(): Unit =
    while input.hasNext do
      queue.add(input.next()) // the oldest item automatically gets evicted
      output.accept(queue)    // send updated queue to output handler

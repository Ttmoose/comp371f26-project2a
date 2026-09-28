package edu.luc.cs.consoleapp

import java.util.Queue

/**
 * Observer for decoupling sliding window logic from routing updates.
 */
trait OutputHandler:
  def accept(queue: Queue[String]): Unit

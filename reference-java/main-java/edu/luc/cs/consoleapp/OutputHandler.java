package edu.luc.cs.consoleapp;

import java.util.Queue;
import java.util.function.Consumer;

/**
 * Observer for decoupling sliding window logic from routing updates. The
 * consumer processes the output and returns void.
 */
interface OutputHandler extends Consumer<Queue<String>> {
}

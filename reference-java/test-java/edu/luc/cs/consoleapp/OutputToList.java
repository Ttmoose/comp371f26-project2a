package edu.luc.cs.consoleapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class OutputToList implements OutputHandler {

  final List<Queue<String>> result = new ArrayList<>();

  @Override
  public void accept(final Queue<String> value) {
    final var snapshot = new LinkedList<>(value);
    result.add(snapshot);
  }
}

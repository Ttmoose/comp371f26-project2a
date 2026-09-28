package edu.luc.cs.consoleapp

import java.util.Scanner
import org.apache.commons.collections4.queue.CircularFifoQueue
import org.apache.commons.cli.{CommandLine, CommandLineParser, DefaultParser, HelpFormatter, Options, ParseException}

// see https://stackoverflow.com/questions/1963806/#21699069
// why we're using this implementation instead of java.util.ArrayQueue!

object Main:

  val LAST_N_WORDS: Int = 10

  def main(args: Array[String]): Unit =

    // Command-line option parsing using Apache Commons CLI
    val options = new Options()
    options.addOption("n", "lastNWords", true, "Number of last words to keep (must be a natural number)")
    options.addOption("h", "help", false, "Print this help message")

    val parser: CommandLineParser = new DefaultParser()
    val formatter = new HelpFormatter()

    val lastNWords =
      try
        val cmd = parser.parse(options, args)

        if cmd.hasOption("h") then
          formatter.printHelp("consoleapp [ last_n_words ]", options)
          sys.exit(0)

        val remainingArgs = cmd.getArgs
        val n =
          if cmd.hasOption("n") then
            if remainingArgs.length > 0 then
              throw new ParseException("Cannot specify both -n option and positional argument")
            cmd.getOptionValue("n").toInt
          else if remainingArgs.length == 1 then
            remainingArgs(0).toInt
          else if remainingArgs.length > 1 then
            throw new ParseException("Too many arguments provided")
          else
            LAST_N_WORDS

        if n < 1 then
          throw new NumberFormatException()
        n
      catch
        case e: ParseException =>
          System.err.println(s"Error parsing command-line arguments: ${e.getMessage}")
          formatter.printHelp("consoleapp [ last_n_words ]", options)
          sys.exit(2)
        case _: NumberFormatException =>
          System.err.println("argument should be a natural number")
          sys.exit(4)

    val input = new Scanner(System.in).useDelimiter("(?U)[^\\p{Alpha}0-9']+")
    val queue = new CircularFifoQueue[String](lastNWords)

    // this is the core functionality, but it mixes
    // I/O and core processing logic and therefore is hard to test
    while input.hasNext do
      val word = input.next()
      queue.add(word) // the oldest item automatically gets evicted
      println(queue)
      // terminate on I/O error such as SIGPIPE
      if System.out.checkError() then
        sys.exit(1)

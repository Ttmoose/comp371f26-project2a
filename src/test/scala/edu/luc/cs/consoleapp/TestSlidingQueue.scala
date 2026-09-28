package edu.luc.cs.consoleapp

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import java.util.Queue
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

class TestSlidingQueue extends AnyFlatSpec with Matchers:

  class OutputToList extends OutputHandler:
    val result: ListBuffer[List[String]] = ListBuffer[List[String]]()
    override def accept(queue: Queue[String]): Unit =
      val snapshot = queue.asScala.toList
      result += snapshot

  "SlidingQueue" should "handle empty input" in:
    val input = Iterator.empty[String]
    val output = OutputToList()
    val sut = SlidingQueue(3, input, output)
    sut.process()
    output.result shouldBe empty

  it should "handle nonempty input with sliding window eviction" in:
    val input = List("asdf", "qwer", "oiui", "zxcv").iterator
    val output = OutputToList()
    val sut = SlidingQueue(3, input, output)
    sut.process()

    output.result should have size 4
    output.result(0) shouldBe List("asdf")
    output.result(1) shouldBe List("asdf", "qwer")
    output.result(2) shouldBe List("asdf", "qwer", "oiui")
    output.result(3) shouldBe List("qwer", "oiui", "zxcv")

  it should "correctly evict elements when queue capacity is smaller than word count" in:
    val input = List("one", "two", "three", "four", "five").iterator
    val output = OutputToList()
    val sut = SlidingQueue(2, input, output)
    sut.process()

    output.result should have size 5
    output.result(0) shouldBe List("one")
    output.result(1) shouldBe List("one", "two")
    output.result(2) shouldBe List("two", "three")
    output.result(3) shouldBe List("three", "four")
    output.result(4) shouldBe List("four", "five")

  it should "work when capacity equals 1" in:
    val input = List("a", "b", "c").iterator
    val output = OutputToList()
    val sut = SlidingQueue(1, input, output)
    sut.process()

    output.result should have size 3
    output.result(0) shouldBe List("a")
    output.result(1) shouldBe List("b")
    output.result(2) shouldBe List("c")

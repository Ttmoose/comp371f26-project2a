# AI Disclosure / Affidavit of LLM Assistance

## Course Information
- **Course:** COMP 371/471 - Theory and Practice of Programming Languages
- **Project:** Project 2a (`comp371f26-project2a`)
- **AI Tool Used:** Google Gemini (Antigravity AI Coding Assistant)

---

## Project Overview
This project implements an efficient, constant-space stream-processing console application in Scala 3 that maintains a running FIFO sliding window queue of the last $N$ words read from standard input (`stdin`). The project translates the reference Java console application (`consoleapp-java`) into idiomatic Scala 3 using significant indentation (without curly braces), decouples I/O from core processing logic via the Callback/Observer design pattern, and integrates automated unit testing for extra credit.

---

## Questions Asked and Work Created

### Interaction 1: Initial Port from Java to Scala 3
- **User Prompt:**
  Provided the assignment description and requested the implementation of `Main.java` in Scala 3 with standard sbt project structure and dependencies.
- **Work Created:**
  - Created `src/main/scala/edu/luc/cs/consoleapp/Main.scala` using Scala 3 significant indentation.
  - Set up `build.sbt` with `commons-collections4` and `scalatest` dependencies.
  - Created `scala.sbt` with compiler options (`-deprecation`, `-feature`, `-unchecked`).
  - Configured `project/build.properties` (sbt 1.10.7) and `project/plugins.sbt` (`sbt-native-packager` and `sbt-scoverage`).
  - Configured `.github/workflows/scala.yml` for automated GitHub Actions CI.

---

### Interaction 2: Re-implementing with Idiomatic Scala 3 Patterns
- **User Prompt:**
  Asked to re-implement `Main.scala` to ensure idiomatic Scala 3 style.
- **Work Created:**
  - Replaced mutable variables and manual length checks with Scala pattern matching on `args` (`case Array()`, `case Array(arg)`, `case _`).
  - Transitioned from Java consumer callbacks to native imperative streaming loop `while input.hasNext do`.
  - Replaced Java `System.exit` with Scala `sys.exit`.
  - Restored clean state of `src/main/java/edu/luc/cs/consoleapp/Main.java`.

---

### Interaction 3: Implementing the CLI Options TODO
- **User Prompt:**
  Requested accomplishing the `TODO consider using a command-line option library`.
- **Work Created:**
  - Integrated Apache Commons CLI (`commons-cli:1.9.0`) into `build.sbt` and `build.gradle.kts`.
  - Added `-n` / `--lastNWords` option flag and `-h` / `--help` option flag using `Options`, `DefaultParser`, and `HelpFormatter`.
  - Preserved backward-compatible positional argument handling (`consoleapp 3`) to ensure compatibility with existing test scripts and README examples.
  - Handled invalid arguments with error status 2 and non-natural numbers with status 4.

---

### Interaction 4: Resolving `JavaAppPackaging` Build Error
- **User Prompt:**
  Reported compilation/loading error: `not found: value JavaAppPackaging`.
- **Work Created:**
  - Diagnosed that `project/plugins.sbt` had not been populated.
  - Added `addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.10.4")` and `addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "2.1.0")` to `project/plugins.sbt`.
  - Populated `scala.sbt` with `scalaVersion := "3.3.4"` and compiler flags.

---

### Interaction 5: Resolving Duplicate Class Symbol Conflicts
- **User Prompt:**
  Reported sbt error: `Main is already defined as object Main in ... Main.java` and similar clashes for `SlidingQueue` and `OutputHandler`.
- **Work Created:**
  - Diagnosed that sbt was compiling both `src/main/java` and `src/main/scala` simultaneously, causing duplicate type definitions.
  - Safely moved Java reference files into `reference-java/` so they remain accessible for review without interfering with the Scala compiler.
  - Configured `Compile / unmanagedSourceDirectories` in `build.sbt` to target only Scala sources.

---

### Interaction 6: Execution Guidance & Staging
- **User Prompt:**
  Asked how to run the project and reported command syntax errors when chaining `sbt stage` with executable execution.
- **Work Created:**
  - Clarified the separation between sbt compilation (`sbt stage`) and direct binary execution (`./target/universal/stage/bin/main 3`).
  - Corrected directory typo (`universe` vs. `universal`).
  - Verified local execution against empty input, README traces, and invalid argument handling.

---

### Interaction 7: Scalability & Memory Profiling Instructions
- **User Prompt:**
  Requested instructions for running the `htop` scalability test with infinite streams.
- **Work Created:**
  - Documented Terminal 1 (`yes | ./target/universal/stage/bin/main > /dev/null`) and Terminal 2 (`htop`) commands.
  - Provided guidance on sorting by `PERCENT_CPU` (F6) and monitoring `VIRT` and `RES` columns over time.
  - Linked screenshot placeholders in `doc/scalability.md` to `scalability_1.png` and `scalability_2.png`.

---

## Summary of Work Created

### 1. Scala 3 Source Code
- `src/main/scala/edu/luc/cs/consoleapp/Main.scala`:
  Main console entry point with command-line option parsing, input stream reading via `Scanner`, sliding FIFO queue management, and signal termination on broken pipes (`checkError`).
- `src/main/scala/edu/luc/cs/consoleapp/SlidingQueue.scala`:
  Decoupled sliding window queue component implementing the Callback/Observer design pattern.
- `src/main/scala/edu/luc/cs/consoleapp/OutputHandler.scala`:
  Observer trait defining the `accept(queue: Queue[String]): Unit` callback contract.

### 2. Automated Unit Tests (Extra Credit)
- `src/test/scala/edu/luc/cs/consoleapp/TestSlidingQueue.scala`:
  Comprehensive automated unit tests using ScalaTest covering:
  - Empty input stream handling.
  - Non-empty input matching the README scenario with capacity 3.
  - Custom capacity testing with multiple eviction steps.
  - Edge case testing with capacity 1.

### 3. Build & Configuration
- `build.sbt`: Configured `JavaAppPackaging`, dependencies (`commons-collections4`, `commons-cli`, `scalatest`), main class, and staged executable name (`main`).
- `scala.sbt`: Scala 3.3.4 configuration with `-deprecation`, `-feature`, and `-unchecked` compiler options.
- `project/build.properties`: Declares sbt version 1.10.7.
- `project/plugins.sbt`: Declares plugins for `sbt-native-packager` and `sbt-scoverage`.
- `.github/workflows/scala.yml`: GitHub Actions CI workflow to run automated tests and build stages on push/PR.
- `.gitignore`: Standard ignore rules for sbt, IDE, and operating system temporary files.

### 4. Documentation & Verification Reports
- `doc/ai-disclosure.md`: This comprehensive AI disclosure detailing all interactions, code generated, and verification steps.
- `doc/manual-testing.md`: Test traces and screenshots for empty input and nonempty input scenarios.
- `doc/scalability.md`: Scalability analysis explaining constant space complexity ($O(1)$ memory) and `htop` profiling screenshots.

---

## Affidavit / Verification Statement
I affirm that all AI-generated code and build configurations were thoroughly examined, compiled, and tested locally. The code strictly satisfies all functional requirements (correct stream tokenization, queue capacity handling, exit codes) and nonfunctional requirements (constant space complexity, modular separation of concerns, testability, and clean signal termination).

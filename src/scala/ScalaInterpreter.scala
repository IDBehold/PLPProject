package scala

import java.security.InvalidParameterException
import java.util
import java.util.ArrayList

import sample.{Painter}
import sample.Util.{ColorUtils, Command}

import scala.collection.JavaConverters._

object ScalaInterpreter {

  // Header functions. Links to java
  def splitIntoCommands(rawInput: String): ArrayList[String] = {
    return new ArrayList[String](splitIntoCommandsInternal(rawInput).reverse.asJava) // its the only way
  }

  def getCommandParameters(command: String): ArrayList[String] = {
    return new ArrayList[String](getCommandParametersInternal(command).reverse.asJava)
  }

  def createCommands(commands: List[String]): ArrayList[Command] = {
    return new ArrayList[Command](createCommandsInternal(commands).asJava) // its the only way
  }

  def getCommandParametersInHigherOrderFunctions(rawInput: String): ArrayList[String] = {
    return new ArrayList[String](getCommandParametersInHigherOrderFunctionsInternal(rawInput).asJava) // its the only way
  }

  // Only used for testing purposes. God have mercy on our souls
  def createCommandsTest(raw: String): ArrayList[Command] = {
    val commandstrings = splitIntoCommandsInternal(raw)
    return ScalaInterpreter.createCommands(commandstrings)
  }

  def getCommandName(command: String): String = {
    val split = command.split("[\\s\\(]")
    if (split.length > 0 && split(0).matches("[a-zA-Z\\-]+")) return split(0)
    throw new IllegalStateException("No valid command name was found")
  }

  def splitIntoCommandsInternal(rawInput: String, commands: List[String] = Nil, openBracketsCounter: Int = 0, pos: Int = 0, startIndex: Int = 0, endIndex: Int = 0): List[String] = {
    if (pos < rawInput.length()) {
      var currentStartIndex = startIndex
      var currentEndIndex = endIndex
      var currentBrackets = openBracketsCounter
      var currentCommands = commands

      if (rawInput.charAt(pos) == '(' && currentBrackets == 0) {
        currentStartIndex = pos + 1
        currentBrackets = openBracketsCounter + 1
      }
      else if (rawInput.charAt(pos) == '(') currentBrackets += 1
      else if (rawInput.charAt(pos) == ')') currentBrackets -= 1

      if (currentBrackets == 0 && rawInput.charAt(pos) == ')') {
        currentEndIndex = pos
        currentCommands = rawInput.substring(currentStartIndex, currentEndIndex) :: commands
      }

      splitIntoCommandsInternal(rawInput, currentCommands, currentBrackets, pos + 1, currentStartIndex, currentEndIndex)
    }
    else {
      return commands
    }
  }

  def getCommandParametersInternal(command: String, commandParameters: List[String] = Nil, openBracketsCounter: Int = 0, pos: Int = 0, startIndex: Int = 0, endIndex: Int = 0, lookForStartingSpace: Boolean = true): List[String] = {
    if (pos < command.length()) {
      var currentStartIndex = startIndex
      var currentEndIndex = endIndex
      var currentBrackets = openBracketsCounter
      var currentlyLookingForStartingSpace = lookForStartingSpace
      var currentCommandParameters = commandParameters

      if (!Character.toString(command.charAt(pos)).matches("[a-zA-Z0-9\\s\\-()\\.\\%]")) throw new IllegalStateException("Invalid command")

      if (currentBrackets == 0) if (command.charAt(pos) == ' ' && currentlyLookingForStartingSpace) {
        currentlyLookingForStartingSpace = false
        currentStartIndex = pos + 1
      }
      else if (command.charAt(pos) == ' ' && !currentlyLookingForStartingSpace) {
        currentlyLookingForStartingSpace = true
        currentEndIndex = pos
        currentCommandParameters = command.substring(currentStartIndex, currentEndIndex) :: commandParameters
        if (Character.toString(command.charAt(pos + 1)).matches("[0-9]")) {
          currentStartIndex = pos + 1
          currentlyLookingForStartingSpace = false
        }
      }
      else if (pos == command.length - 1 && Character.toString(command.charAt(pos)).matches("[a-zA-Z0-9\\.\\%]") && !currentlyLookingForStartingSpace) {
        currentEndIndex = pos + 1
        currentCommandParameters = command.substring(currentStartIndex, currentEndIndex) :: commandParameters
      }

      if (command.charAt(pos) == '(') {
        currentlyLookingForStartingSpace = true
        currentBrackets += 1
        currentStartIndex = pos + 1
      }
      else if (command.charAt(pos) == ')') currentBrackets -= 1

      if (currentBrackets == 0 && command.charAt(pos) == ')') {
        currentEndIndex = pos
        currentCommandParameters = command.substring(currentStartIndex, currentEndIndex) :: commandParameters
      }
      getCommandParametersInternal(command, currentCommandParameters, currentBrackets, pos + 1, currentStartIndex, currentEndIndex, currentlyLookingForStartingSpace)
    }
    else {
      return commandParameters
    }
  }

  def getCommandParametersInHigherOrderFunctionsInternal(command: String): List[String] = {
    val params: List[String] = getCommandParametersInternal(command)
    val regex: String = "[a-zA-Z]+"
    return params.filter(_.matches(regex))
  }

  def createCommandsInternal(commands: List[String], result: List[Command] = Nil): List[Command] = commands match {
    case Nil => result
    case head :: tail => {
      var currentResult = result
      var commandName = getCommandName(head)
      if (commandName.toLowerCase == "draw" || commandName.toLowerCase == "fill") {
        val nestedCommands = ScalaInterpreter.splitIntoCommandsInternal(head)
        val parameters = ScalaInterpreter.getCommandParametersInHigherOrderFunctions(head)
        val e = new Command(getCommandName(head))
        e.setHigherOrderFunctions(new ArrayList[Command](createCommands(nestedCommands)))
        e.setParameters(parameters)
        currentResult = e :: result
      }
      else {
        val e = new Command(getCommandName(head))
        e.setParameters(getCommandParameters(head))
        currentResult = e :: result
      }
      createCommandsInternal(tail, currentResult)
    }
  }

  def getShapeToFill(command: Command) = command.getName.toLowerCase match {
    case "rectangle" =>
      val parameter1 = command.getParameter(0)
      val split1 = parameter1.split(" ")
      val parameter2 = command.getParameter(1)
      val split2 = parameter2.split(" ")
      new Draw.Rectangle(split1(0).toInt, split1(1).toInt, split2(0).toInt, split2(1).toInt)

    case "circle" =>
      val parameter1 = command.getParameter(0)
      val split1 = parameter1.split(" ")
      val parameter2 = command.getParameter(1)
      new Draw.Circle(split1(0).toInt, split1(1).toInt, parameter2.toInt)

    case _ =>
      throw new IllegalArgumentException(command.getName + " is not a valid command parameter for FILL")
  }

  def appendShape(command: Command, tail: Draw.ShapeList) = command.getName.toLowerCase match {
    case "line" =>
      val parameter1 = command.getParameter(0)
      val split1 = parameter1.split(" ")
      val parameter2 = command.getParameter(1)
      val split2 = parameter2.split(" ")
      new Draw.Cons(new Draw.Line(split1(0).toInt, split1(1).toInt, split2(0).toInt, split2(1).toInt), tail)

    case "rectangle" =>
      val parameter1 = command.getParameter(0)
      val split1 = parameter1.split(" ")
      val parameter2 = command.getParameter(1)
      val split2 = parameter2.split(" ")
      new Draw.Cons(new Draw.Rectangle(split1(0).toInt, split1(1).toInt, split2(0).toInt, split2(1).toInt), tail)

    case "text-at" =>
      val parameter1 = command.getParameter(0)
      val split1 = parameter1.split(" ")
      val parameter2 = command.getParameter(1)
      new Draw.Cons(new Draw.TextAt(split1(0).toInt, split1(1).toInt, parameter2), tail)

    case "circle" =>
      val parameter1 = command.getParameter(0)
      val split1 = parameter1.split(" ")
      val parameter2 = command.getParameter(1)
      new Draw.Cons(new Draw.Circle(split1(0).toInt, split1(1).toInt, parameter2.toInt), tail)

    case _ =>
      throw new IllegalArgumentException(command.getName + " is not a valid command parameter for DRAW")

  }

  def interpret(input: String, painter: Painter): Unit = {
    if (input.trim.length == 0) throw new InvalidParameterException("Input field is empty")

    val numberOfOpenBrackets = input.length - input.replace("(", "").length
    val numberOfClosedBrackets = input.length - input.replace(")", "").length

    if (numberOfClosedBrackets < numberOfOpenBrackets) throw new InvalidParameterException("Missing closing parenthesis")
    else if (numberOfClosedBrackets > numberOfOpenBrackets) throw new InvalidParameterException("Too many closing parenthesis")

    val commandstrings = splitIntoCommandsInternal(input)
    val commands = createCommandsInternal(commandstrings)

    if (!(commands.head.getName.toLowerCase == "bounding-box")) throw new InvalidParameterException("Input has to be initialized with a bounding-box")
    interpretCommands(commands, painter)
  }

  def interpretCommands(commands: List[Command], painter: Painter): Unit = commands match {
    case Nil => Unit
    case head :: tail => {
      matchCommand(head, painter)
      interpretCommands(tail, painter)
    }
  }

  def matchCommand(command: Command, painter: Painter): Unit = command.getName.toLowerCase match {
    case "bounding-box" =>
      val parameter1: String = command.getParameter(0)
      val split1: Array[String] = parameter1.split(" ")
      val parameter2: String = command.getParameter(1)
      val split2: Array[String] = parameter2.split(" ")
      painter.setBoundingBox(split1(0).toInt, split1(1).toInt, split2(0).toInt, split2(1).toInt)

    case "line" =>
      val parameter1: String = command.getParameter(0)
      val split1: Array[String] = parameter1.split(" ")
      val parameter2: String = command.getParameter(1)
      val split2: Array[String] = parameter2.split(" ")
      painter.drawLine(split1(0).toInt, split1(1).toInt, split2(0).toInt, split2(1).toInt)

    case "circle" =>
      val parameter1: String = command.getParameter(0)
      val split1: Array[String] = parameter1.split(" ")
      val parameter2: String = command.getParameter(1)
      painter.drawCircle(split1(0).toInt, split1(1).toInt, parameter2.toInt)

    case "rectangle" =>
      val parameter1: String = command.getParameter(0)
      val split1: Array[String] = parameter1.split(" ")
      val parameter2: String = command.getParameter(1)
      val split2: Array[String] = parameter2.split(" ")
      painter.drawRectangle(split1(0).toInt, split1(1).toInt, split2(0).toInt, split2(1).toInt)

    case "text-at" =>
      val parameter1: String = command.getParameter(0)
      val split1: Array[String] = parameter1.split(" ")
      val parameter2: String = command.getParameter(1)
      painter.drawTextAt(split1(0).toInt, split1(1).toInt, parameter2)

    case "draw" =>
      val color: String = command.getParameter(0)
      var list: Draw.ShapeList = new Draw.Nil
      import scala.collection.JavaConversions._
      for (c <- command.getHigherOrderFunctions) {
        list = appendShape(c, list)
      }
      painter.drawShapes(ColorUtils.parseColor(color), list)
    case "fill" =>
      val color: String = command.getParameter(0)
      val shape: Draw.Shape = getShapeToFill(command.getHigherOrderFunction(0))
      painter.fillShape(ColorUtils.parseColor(color), shape)

    case _ =>
      throw new IllegalArgumentException(command.getName + " is not a valid command")
  }
}

package messages

import kuger.loganalyzer.core.api.{Input, LogStatement}

sealed trait LoganalyzerEvent

case object StartFlow extends LoganalyzerEvent

case class LogStatementEvent(logStatement: LogStatement) extends LoganalyzerEvent

case class InputDrainedEvent(input: Input) extends LoganalyzerEvent
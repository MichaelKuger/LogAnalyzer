package filter


import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging
import kuger.loganalyzer.core.api.Filter
import messages.{InputDrainedEvent, LogStatementEvent, LoganalyzerEvent}

object FilterActor {
  def props(filter: Filter, downstream: Array[ActorRef]): Props = {
    Props(new FilterActor(filter, downstream))
  }
}

class FilterActor(filter: Filter, downstream: Array[ActorRef]) extends Actor {
  val log = Logging(context.system, this)

  override def receive = {
    case LogStatementEvent(message) => {
      val doFilter = filter.filter(message)
      if (!doFilter) {
        passMessage(LogStatementEvent(message))
      }
    }
    case InputDrainedEvent(input) =>
      log.info("Drained event received. passing on..")
      passMessage(InputDrainedEvent(input))
    case default =>
      log.warning("Could not handle message: " + default)
  }

  def passMessage(message: LoganalyzerEvent) = {
    downstream foreach (_ ! message)
  }
}

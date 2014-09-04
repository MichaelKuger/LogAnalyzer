package output

import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging
import kuger.loganalyzer.core.api.Sink
import messages.{InputDrainedEvent, LogStatementEvent}


object SinkActor {
  def props(controlActor: ActorRef, sinks: Array[Sink]) = {
    Props(new SinkActor(controlActor, sinks))
  }
}

class SinkActor(controlActor: ActorRef, sinks: Array[Sink]) extends Actor {
  val log = Logging(context.system, this)

  override def receive = {
    case LogStatementEvent(message) => {
      sinks foreach (_.add(message))
    }
    case InputDrainedEvent(input) => {
      log.info("Input closed: " + input)
      controlActor ! InputDrainedEvent(input)
    }
    case default =>
      log.warning("Could not handle message: " + default)
  }
}
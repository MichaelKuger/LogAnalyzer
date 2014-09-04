package control

import java.util
import java.util.Collection

import akka.actor.{Actor, Props}
import akka.event.Logging
import kuger.loganalyzer.core.api.{Input, InputIdentifier, Pipeline}
import messages.InputDrainedEvent

import scala.collection.mutable.HashSet

object ControlActor {
  def props(configuredInputs: util.Collection[InputIdentifier], pipeline: Pipeline): Props = {
    Props(new ControlActor(configuredInputs, pipeline))
  }
}

class ControlActor(configuredInputs: util.Collection[InputIdentifier], pipeline: Pipeline) extends Actor {
  val log = Logging(context.system, this)
  val inputState = HashSet[Input]()

  override def receive: Receive = {
    case InputDrainedEvent(input) =>
      log.info("Sink shutdown message received. Input: " + input)
      inputState += input
      checkState
    case default =>
      log.warning("Could not handle message: " + default)
  }


  def checkState {
    if (inputState.size == configuredInputs.size()) {
      log.info("All " + configuredInputs.size() + " input(s) closed. Shutting down actor system.")
      context.system.shutdown()
      pipeline.finished()
    } else {
      log.info(inputState.size + " input(s) closed, " + configuredInputs.size() + " in total.")
    }
  }
}

package util

import java.util.Collection

import akka.actor.{ActorRef, ActorSystem}
import control.ControlActor
import filter.FilterActor
import kuger.loganalyzer.core.api._
import output.SinkActor

object ScalaHelper {

  def createActorSystem(name: String): ActorSystem = {
    ActorSystem(name)
  }

  def createFilterActor(system: ActorSystem, filter: Filter, downstream: Array[ActorRef]): ActorRef = {
    println("Creating Filter Actor. Filter: " + filter + "; Downstream: " +arrayAsString(downstream))
    system.actorOf(FilterActor.props(filter, downstream), filter.getName)
  }

  def createSinkActor(name: String, system: ActorSystem, controlActor: ActorRef, sinks: Array[Sink]): ActorRef = {
    system.actorOf(SinkActor.props(controlActor, sinks), name)
  }

  def createControlActor(name: String, system: ActorSystem, configuredInputs: Collection[InputIdentifier], pipeline: Pipeline): ActorRef = {
    system.actorOf(ControlActor.props(configuredInputs, pipeline), name)
  }

  def arrayAsString(input: Array[ActorRef]) = {
    val result = new StringBuilder
    input foreach (result ++= _.toString())
    result.mkString
  }
}

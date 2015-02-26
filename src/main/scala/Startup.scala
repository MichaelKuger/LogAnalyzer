import java.io.File
import java.time.{ZoneOffset, ZonedDateTime}

import akka.actor.{ActorSystem, Props}
import filter.FilterActor
import filter.regex.RegexFilter
import filter.temporal.TemporalFilter
import input.StatementParser
import kuger.loganalyzer.core.api.TemporalType

object Startup {

  def start {
//    val system = ActorSystem("LoganalyzerSystem")
//    val f = new File("C:\\workspaces\\intellij\\loganalyzer\\src\\main\\resources\\logs\\vs3_debug.log")
//    val threshold = ZonedDateTime.of(2014, 5, 14, 22, 42, 0, 0, ZoneOffset.UTC).toInstant
//    val logWorker = system.actorOf(Props[LogWorker], "LogWorker")
//    logWorker ! "Test"
//    val temporalFilter = new TemporalFilter(threshold, TemporalType.AFTER)
//    val logWorkerArray = Array(logWorker)
//    val regexFilter = new RegexFilter("BLUE")
//    val regexFilterActor = system.actorOf(FilterActor.props(regexFilter, logWorkerArray), "RegexFilter")
//    val regexFilterArray = Array(regexFilterActor)
//    val tempFilterActor = system.actorOf(FilterActor.props(temporalFilter, regexFilterArray), "TemporalBefore")
//    val tempFilterActorArray = Array(tempFilterActor)
//    val fi = new FileInput(f, tempFilterActorArray)
//    fi.start()
  }
}

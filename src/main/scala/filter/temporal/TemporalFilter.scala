package filter.temporal

import java.time.Instant

import kuger.loganalyzer.core.api.{Filter, LogStatement, TemporalType}

class TemporalFilter(threshold: Instant, temporalType: TemporalType) extends Filter {
  override def filter(stmt: LogStatement): Boolean = {
    temporalType match {
      case TemporalType.AFTER =>
        threshold isAfter stmt.getTimestamp
      case TemporalType.BEFORE =>
        threshold isBefore stmt.getTimestamp
    }
  }

  override def getName: String = threshold.toString

  override def toString: String = {
    "TemporalFilter{" + "threshold=" + threshold + ", temporalType=" + temporalType + "}"
  }
}
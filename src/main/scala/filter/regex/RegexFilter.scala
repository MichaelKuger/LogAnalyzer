package filter.regex


import kuger.loganalyzer.core.api.{Filter, LogStatement, RegexType}

class RegexFilter(patternLiteral: String, regexType: RegexType) extends Filter {
  val pattern = patternLiteral.r

  override def filter(statement: LogStatement): Boolean = {
    val message = statement.getMessage
    val matches = pattern.findFirstIn(message) == None
    if (regexType == RegexType.INCLUDE)
      matches
    else if (regexType == RegexType.EXCLUDE)
      !matches
    else
      throw new IllegalStateException("Invalid configuration: " + regexType)
  }

  override def getName: String = {
    patternLiteral
  }

  override def toString: String = {
    "RegexFilter{pattern=" + pattern + "}"
  }
}
package com.vend.infrastructure

import com.typesafe.scalalogging.StrictLogging
import doobie.util.log.{ExecFailure, LogHandler, ProcessingFailure, Success}

trait DbLogHandler extends StrictLogging {
  implicit val doobieLogHandler: LogHandler = LogHandler {
    case Success(sql, args, exec, processing) =>
      logger.debug(s"sql: $sql, args: $args, query (execution: $exec, processing: $processing)")
    case ProcessingFailure(sql, args, exec, processing, failure) =>
      logger.error(s"Processing failure (execution: $exec, processing: $processing): $sql | args: $args", failure)
    case ExecFailure(sql, args, exec, failure) =>
      logger.error(s"Execution failure (execution: $exec): $sql | args: $args", failure)
  }

}

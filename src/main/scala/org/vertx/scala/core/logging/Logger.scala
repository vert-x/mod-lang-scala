package org.vertx.scala.core.logging

/**
 * Small helper class to check for log level and delegate it to the real logger if enabled.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class Logger(delegate: org.vertx.java.core.logging.Logger) {

  def isInfoEnabled(): Boolean = delegate.isInfoEnabled()
  def isDebugEnabled(): Boolean = delegate.isDebugEnabled()
  def isTraceEnabled(): Boolean = delegate.isTraceEnabled()

  def trace(message: => AnyRef) = withTrace(delegate.trace(message))
  def trace(message: => AnyRef, t: => Throwable) = withTrace(delegate.trace(message, t))
  def debug(message: => AnyRef) = withDebug(delegate.debug(message))
  def debug(message: => AnyRef, t: => Throwable) = withDebug(delegate.debug(message, t))
  def info(message: => AnyRef) = withInfo(delegate.info(message))
  def info(message: => AnyRef, t: => Throwable) = withInfo(delegate.info(message, t))

  def warn(message: => AnyRef) = delegate.warn(message)
  def warn(message: => AnyRef, t: => Throwable) = delegate.warn(message, t)
  def error(message: => AnyRef) = delegate.error(message)
  def error(message: => AnyRef, t: => Throwable) = delegate.error(message, t)
  def fatal(message: => AnyRef) = delegate.fatal(message)
  def fatal(message: => AnyRef, t: => Throwable) = delegate.fatal(message, t)

  private def withTrace(fn: => Unit) = if (delegate.isTraceEnabled) { fn }
  private def withDebug(fn: => Unit) = if (delegate.isDebugEnabled) { fn }
  private def withInfo(fn: => Unit) = if (delegate.isInfoEnabled) { fn }

}
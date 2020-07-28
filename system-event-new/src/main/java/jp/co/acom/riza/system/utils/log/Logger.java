//
package jp.co.acom.riza.system.utils.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * Logger.
 * 
 * @author teratani
 */
public class Logger {

	private final org.slf4j.Logger destination;

	/**
	 * @param target
	 * @return
	 */
	public static Logger getLogger(String target) {
		return (new Logger(LoggerFactory.getLogger(target)));
	}

	/**
	 * @param target
	 * @return
	 */
	public static Logger getLogger(Class<?> target) {
		return (new Logger(LoggerFactory.getLogger(target)));
	}

	/**
	 * @param destination
	 */
	private Logger(org.slf4j.Logger destination) {
		this.destination = destination;
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#getName()
	 */
	public String getName() {
		return destination.getName();
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return destination.isTraceEnabled();
	}

	/**
	 * @param msg
	 * @see org.slf4j.Logger#trace(java.lang.String)
	 */
	public void trace(String msg) {
		destination.trace(msg);
	}

	/**
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object)
	 */
	public void trace(String format, Object arg) {
		destination.trace(format, arg);
	}

	/**
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public void trace(String format, Object arg1, Object arg2) {
		destination.trace(format, arg1, arg2);
	}

	/**
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[])
	 */
	public void trace(String format, Object... arguments) {
		destination.trace(format, arguments);
	}

	/**
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable)
	 */
	public void trace(String msg, Throwable t) {
		destination.trace(msg, t);
	}

	/**
	 * @param marker
	 * @return
	 * @see org.slf4j.Logger#isTraceEnabled(org.slf4j.Marker)
	 */
	public boolean isTraceEnabled(Marker marker) {
		return destination.isTraceEnabled(marker);
	}

	/**
	 * @param marker
	 * @param msg
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String)
	 */
	public void trace(Marker marker, String msg) {
		destination.trace(marker, msg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public void trace(Marker marker, String format, Object arg) {
		destination.trace(marker, format, arg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		destination.trace(marker, format, arg1, arg2);
	}

	/**
	 * @param marker
	 * @param format
	 * @param argArray
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public void trace(Marker marker, String format, Object... argArray) {
		destination.trace(marker, format, argArray);
	}

	/**
	 * @param marker
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void trace(Marker marker, String msg, Throwable t) {
		destination.trace(marker, msg, t);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return destination.isDebugEnabled();
	}

	/**
	 * @param msg
	 * @see org.slf4j.Logger#debug(java.lang.String)
	 */
	public void debug(String msg) {
		destination.debug(msg);
	}

	/**
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object)
	 */
	public void debug(String format, Object arg) {
		destination.debug(format, arg);
	}

	/**
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public void debug(String format, Object arg1, Object arg2) {
		destination.debug(format, arg1, arg2);
	}

	/**
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[])
	 */
	public void debug(String format, Object... arguments) {
		destination.debug(format, arguments);
	}

	/**
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable)
	 */
	public void debug(String msg, Throwable t) {
		destination.debug(msg, t);
	}

	/**
	 * @param marker
	 * @return
	 * @see org.slf4j.Logger#isDebugEnabled(org.slf4j.Marker)
	 */
	public boolean isDebugEnabled(Marker marker) {
		return destination.isDebugEnabled(marker);
	}

	/**
	 * @param marker
	 * @param msg
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String)
	 */
	public void debug(Marker marker, String msg) {
		destination.debug(marker, msg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public void debug(Marker marker, String format, Object arg) {
		destination.debug(marker, format, arg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		destination.debug(marker, format, arg1, arg2);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public void debug(Marker marker, String format, Object... arguments) {
		destination.debug(marker, format, arguments);
	}

	/**
	 * @param marker
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void debug(Marker marker, String msg, Throwable t) {
		destination.debug(marker, msg, t);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return destination.isInfoEnabled();
	}

	/**
	 * @param msg
	 * @see org.slf4j.Logger#info(java.lang.String)
	 */
	public void info(String msg) {
		destination.info(msg);
	}

	/**
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object)
	 */
	public void info(String format, Object arg) {
		destination.info(format, arg);
	}

	/**
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public void info(String format, Object arg1, Object arg2) {
		destination.info(format, arg1, arg2);
	}

	/**
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[])
	 */
	public void info(String format, Object... arguments) {
		destination.info(format, arguments);
	}

	/**
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable)
	 */
	public void info(String msg, Throwable t) {
		destination.info(msg, t);
	}

	/**
	 * @param marker
	 * @return
	 * @see org.slf4j.Logger#isInfoEnabled(org.slf4j.Marker)
	 */
	public boolean isInfoEnabled(Marker marker) {
		return destination.isInfoEnabled(marker);
	}

	/**
	 * @param marker
	 * @param msg
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String)
	 */
	public void info(Marker marker, String msg) {
		destination.info(marker, msg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public void info(Marker marker, String format, Object arg) {
		destination.info(marker, format, arg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		destination.info(marker, format, arg1, arg2);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public void info(Marker marker, String format, Object... arguments) {
		destination.info(marker, format, arguments);
	}

	/**
	 * @param marker
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void info(Marker marker, String msg, Throwable t) {
		destination.info(marker, msg, t);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return destination.isWarnEnabled();
	}

	/**
	 * @param msg
	 * @see org.slf4j.Logger#warn(java.lang.String)
	 */
	public void warn(String msg) {
		destination.warn(msg);
	}

	/**
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object)
	 */
	public void warn(String format, Object arg) {
		destination.warn(format, arg);
	}

	/**
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[])
	 */
	public void warn(String format, Object... arguments) {
		destination.warn(format, arguments);
	}

	/**
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public void warn(String format, Object arg1, Object arg2) {
		destination.warn(format, arg1, arg2);
	}

	/**
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable)
	 */
	public void warn(String msg, Throwable t) {
		destination.warn(msg, t);
	}

	/**
	 * @param marker
	 * @return
	 * @see org.slf4j.Logger#isWarnEnabled(org.slf4j.Marker)
	 */
	public boolean isWarnEnabled(Marker marker) {
		return destination.isWarnEnabled(marker);
	}

	/**
	 * @param marker
	 * @param msg
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String)
	 */
	public void warn(Marker marker, String msg) {
		destination.warn(marker, msg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public void warn(Marker marker, String format, Object arg) {
		destination.warn(marker, format, arg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		destination.warn(marker, format, arg1, arg2);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public void warn(Marker marker, String format, Object... arguments) {
		destination.warn(marker, format, arguments);
	}

	/**
	 * @param marker
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void warn(Marker marker, String msg, Throwable t) {
		destination.warn(marker, msg, t);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return destination.isErrorEnabled();
	}

	/**
	 * @param msg
	 * @see org.slf4j.Logger#error(java.lang.String)
	 */
	public void error(String msg) {
		destination.error(msg);
	}

	/**
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object)
	 */
	public void error(String format, Object arg) {
		destination.error(format, arg);
	}

	/**
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public void error(String format, Object arg1, Object arg2) {
		destination.error(format, arg1, arg2);
	}

	/**
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[])
	 */
	public void error(String format, Object... arguments) {
		destination.error(format, arguments);
	}

	/**
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	public void error(String msg, Throwable t) {
		destination.error(msg, t);
	}

	/**
	 * @param marker
	 * @return
	 * @see org.slf4j.Logger#isErrorEnabled(org.slf4j.Marker)
	 */
	public boolean isErrorEnabled(Marker marker) {
		return destination.isErrorEnabled(marker);
	}

	/**
	 * @param marker
	 * @param msg
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String)
	 */
	public void error(Marker marker, String msg) {
		destination.error(marker, msg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public void error(Marker marker, String format, Object arg) {
		destination.error(marker, format, arg);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		destination.error(marker, format, arg1, arg2);
	}

	/**
	 * @param marker
	 * @param format
	 * @param arguments
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public void error(Marker marker, String format, Object... arguments) {
		destination.error(marker, format, arguments);
	}

	/**
	 * @param marker
	 * @param msg
	 * @param t
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void error(Marker marker, String msg, Throwable t) {
		destination.error(marker, msg, t);
	}

}

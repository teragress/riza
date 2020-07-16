/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package jp.co.acom.riza.system.utils.log;

import static ch.qos.logback.core.CoreConstants.CODES_URL;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.status.ErrorStatus;

/**
 * OutputStreamAppender appends events to a {@link OutputStream}. This class
 * provides basic services that other appenders build upon.
 * 
 * For more information about this appender, please refer to the online manual
 * at http://logback.qos.ch/manual/appenders.html#OutputStreamAppender
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class LogAssertAppender<E> extends UnsynchronizedAppenderBase<E> {

	/**
	 * It is the encoder which is ultimately responsible for writing the event to an
	 * {@link OutputStream}.
	 */
	protected Encoder<E> encoder;

	/**
	 * All synchronization in this class is done via the lock object.
	 */
	protected final ReentrantLock lock = new ReentrantLock(false);

	/**
	 * This is the {@link OutputStream outputStream} where output will be written.
	 */
	private OutputStream outputStream;

	private static List<String> logList = new ArrayList<String>();
	private static int current = 0;

	boolean immediateFlush = true;

	/**
	 * The underlying output stream used by this appender.
	 * 
	 * @return
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Checks that requires parameters are set and if everything is in order,
	 * activates this appender.
	 */
	public void start() {
		super.start();
	}

	public static void clear() {
		logList.clear();
		current = 0;
	}

	public static boolean assertString(String str) {
		try {
			while (true) {
				if (logList.size() > current) {
					if (logList.get(current).matches(str)) {
						current++;
						return true;
					}
					current++;
				} else {
					return false;
				}
			}

		} catch (Exception ex) {
			return false;
		}
	}

	public static String getMatchString(String str) {
		try {
			while (true) {
				if (logList.size() > current) {
					if (logList.get(current).matches(str)) {
						current++;
						logList.get(current);
					}
					current++;
				} else {
					return null;
				}
			}

		} catch (Exception ex) {
			return null;
		}
	}
	public void setLayout(Layout<E> layout) {
		addWarn("This appender no longer admits a layout as a sub-component, set an encoder instead.");
		addWarn("To ensure compatibility, wrapping your layout in LayoutWrappingEncoder.");
		addWarn("See also " + CODES_URL + "#layoutInsteadOfEncoder for details");
		LayoutWrappingEncoder<E> lwe = new LayoutWrappingEncoder<E>();
		lwe.setLayout(layout);
		lwe.setContext(context);
		this.encoder = lwe;
	}

	@Override
	protected void append(E eventObject) {
		logList.add(eventObject.toString());
		//System.out.println("*******appendTest" + eventObject.toString());
	}

	/**
	 * Stop this appender instance. The underlying stream or writer is also closed.
	 * 
	 * <p>
	 * Stopped appenders cannot be reused.
	 */
	public void stop() {
	}

	/**
	 * Close the underlying {@link OutputStream}.
	 */
	protected void closeOutputStream() {
	}

	void encoderClose() {
	}

	/**
	 * <p>
	 * Sets the @link OutputStream} where the log output will go. The specified
	 * <code>OutputStream</code> must be opened by the user and be writable. The
	 * <code>OutputStream</code> will be closed when the appender instance is
	 * closed.
	 * 
	 * @param outputStream An already opened OutputStream.
	 */
	public void setOutputStream(OutputStream outputStream) {
	}

	void encoderInit() {
	}

	protected void writeOut(E event) throws IOException {

	}

	private void writeBytes(byte[] byteArray) throws IOException {
	}

	/**
	 * Actual writing occurs here.
	 * <p>
	 * Most subclasses of <code>WriterAppender</code> will need to override this
	 * method.
	 * 
	 * @since 0.9.0
	 */
	protected void subAppend(E event) {
		if (!isStarted()) {
			return;
		}
		try {
			// this step avoids LBCLASSIC-139
			if (event instanceof DeferredProcessingAware) {
				((DeferredProcessingAware) event).prepareForDeferredProcessing();
			}
			// the synchronization prevents the OutputStream from being closed while we
			// are writing. It also prevents multiple threads from entering the same
			// converter. Converters assume that they are in a synchronized block.
			// lock.lock();

			byte[] byteArray = this.encoder.encode(event);
			writeBytes(byteArray);

		} catch (IOException ioe) {
			// as soon as an exception occurs, move to non-started state
			// and add a single ErrorStatus to the SM.
			this.started = false;
			addStatus(new ErrorStatus("IO failure in appender", this, ioe));
		}
	}

	public Encoder<E> getEncoder() {
		return encoder;
	}

	public void setEncoder(Encoder<E> encoder) {
		this.encoder = encoder;
	}

	public boolean isImmediateFlush() {
		return immediateFlush;
	}

	public void setImmediateFlush(boolean immediateFlush) {
		this.immediateFlush = immediateFlush;
	}

}
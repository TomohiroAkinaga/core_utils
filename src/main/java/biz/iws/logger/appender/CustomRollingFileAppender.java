package biz.iws.logger.appender;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4j Custom file appender.
 * <pre>
 * create log file appended date pattern from the begenning.
 * </pre>
 * @author Tomohiro Akinaga
 */
public class CustomRollingFileAppender extends FileAppender {

	private String baseName;

	private String datePattern = ".'yyyy-MM-dd";

	private Calendar nextRollOver = Calendar.getInstance();

	private int rollOverField;

	public CustomRollingFileAppender() {
		super();
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	@Override
	public void activateOptions() {
		if (fileName != null) {
			baseName = fileName;
			rollOverField = getRollOverField(datePattern);
			nextRollOver = DateUtils.truncate(nextRollOver, rollOverField);
			fileName += new SimpleDateFormat(datePattern).format(nextRollOver.getTime());
			nextRollOver.add(rollOverField, 1);
		} else {
			// TODO out log??
		}
		super.activateOptions();
	}

	private int getRollOverField(String datePattern) {
		char c = datePattern.charAt(datePattern.length() - 1);
		switch (c) {
		case 'y':
		case 'Y':
			return Calendar.YEAR;
		case 'M':
			return Calendar.MONTH;
		case 'd':
			return Calendar.DATE;
		case 'h':
		case 'H':
			return Calendar.HOUR;
		case 'm':
			return Calendar.MINUTE;
		case 's':
			return Calendar.SECOND;
		case 'S':
			return Calendar.MILLISECOND;
		default:
			return Calendar.DATE;
		}
	}

	/**
	 * change log file.
	 *
	 * @param eventTime log event occur time.
	 */
	private void rollOver(long eventTime) {
		String newFileName = null;
		while (nextRollOver.getTimeInMillis() <= eventTime) {
			newFileName = baseName + new SimpleDateFormat(datePattern).format(nextRollOver.getTime());
			nextRollOver.add(rollOverField, 1);
		}
		this.closeFile();
		try {
			setFile(newFileName, true, bufferedIO, rollOverField);
		} catch (IOException e) {
			LogLog.warn(String.format("Failure roll over log file. [ %s ]", newFileName), e);
		}
	}

	@Override
	protected void subAppend(LoggingEvent event) {
		long eventTime = event.getTimeStamp();
		if (nextRollOver.getTimeInMillis() <= eventTime) {
			rollOver(eventTime);
		}
		super.subAppend(event);
	}

}

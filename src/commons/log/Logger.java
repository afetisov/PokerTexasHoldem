package commons.log;

public class Logger {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);
	
	private Logger() {}
	

	public static void write(LoggingLevel type, String message) {
		switch(type) {
		case TRACE:
			logger.trace(message);
			break;
		case DEBUG:
			logger.debug(message);
			break;
		case INFO:
			logger.info(message);
			break;
		case WARN:
			logger.warn(message);
			break;
		case ERROR:
			logger.error(message);
			break;
		case FATAL:
			logger.fatal(message);
			break;
		}
	}

	public static void write(LoggingLevel type, String message, Throwable exc) {
		switch(type) {
		case TRACE:
			logger.trace(message, exc);
			break;
		case DEBUG:
			logger.debug(message, exc);
			break;
		case INFO:
			logger.info(message, exc);
			break;
		case WARN:
			logger.warn(message, exc);
			break;
		case ERROR:
			logger.error(message, exc);
			break;
		case FATAL:
			logger.fatal(message, exc);
			break;
		}
	}

}

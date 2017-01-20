package model;

/**
 * Exception thrown when modification could lead to data integrity violation.
 * @author joseph
 */
public class DataConsistencyViolationException extends RuntimeException
{
	/**
	 * Instantiates a new Data consistency violation exception.
	 * @param message the message
	 */
	public DataConsistencyViolationException(String message)
	{
		super(message);
	}
}
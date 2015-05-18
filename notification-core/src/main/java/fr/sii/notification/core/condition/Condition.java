package fr.sii.notification.core.condition;

/**
 * Interface that declares a condition. It indicates if the object is accepted
 * or not.
 * 
 * @author Aurélien Baudet
 *
 * @param <T>
 *            The type of the object to test for acceptance
 */
public interface Condition<T> {
	/**
	 * Check if the object is accepted or not.
	 * 
	 * @param obj
	 *            the object to test
	 * @return true if the object is accepted, false otherwise
	 */
	public boolean accept(T obj);
}

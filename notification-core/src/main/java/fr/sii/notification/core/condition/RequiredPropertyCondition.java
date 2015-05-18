package fr.sii.notification.core.condition;

import java.util.Properties;

/**
 * Condition that checks if the provided property is defined either in the
 * system property or in the provided properties.
 * 
 * @author Aurélien Baudet
 *
 * @param <T>
 *            The type of the object to test for acceptance. Has no effect on
 *            the acceptance
 */
public class RequiredPropertyCondition<T> implements Condition<T> {
	/**
	 * The properties to use for checking if property is defined or not
	 */
	private Properties properties;

	/**
	 * The key to check if defined in the properties
	 */
	private String key;

	/**
	 * Initialize the condition with the provided key. It will check the
	 * existence of this key into the system properties.
	 * 
	 * @param key
	 *            The key to check for existence
	 */
	public RequiredPropertyCondition(String key) {
		this(key, System.getProperties());
	}

	/**
	 * Initialize the condition with the provided key. It will check the
	 * existence of this key into the provided properties.
	 * 
	 * @param key
	 *            The key to check for existence
	 * @param properties
	 *            the properties to inspect
	 */
	public RequiredPropertyCondition(String key, Properties properties) {
		super();
		this.key = key;
		this.properties = properties;
	}

	@Override
	public boolean accept(T obj) {
		return properties.containsKey(key);
	}

}

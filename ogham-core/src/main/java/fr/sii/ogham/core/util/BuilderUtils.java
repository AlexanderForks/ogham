package fr.sii.ogham.core.util;

import java.util.Properties;

import fr.sii.ogham.core.builder.Builder;

/**
 * Helper class for {@link Builder} implementations. It separates the builder
 * implementations from the environment.
 * 
 * @author Aurélien Baudet
 * @see Builder
 */
public final class BuilderUtils {
	/**
	 * Provide the default properties. For now, it provides only
	 * {@link System#getProperties()}. But according to the environment or the
	 * future of the module, properties may come from other source.
	 * 
	 * @return the default properties
	 */
	public static Properties getDefaultProperties() {
		return System.getProperties();
	}
	
	private BuilderUtils() {
		super();
	}
}

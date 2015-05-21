package fr.sii.notification.template.thymeleaf;

import fr.sii.notification.core.exception.template.ContextException;
import fr.sii.notification.core.template.context.Context;

/**
 * Convert a {@link Context} abstraction used for all template engines into a
 * {@link org.thymeleaf.context.Context} specific to Thymeleaf.
 * 
 * @author Aurélien Baudet
 *
 */
public class ThymeleafContextConverter {
	/**
	 * Convert abstraction used for all template engines into a Thymeleaf
	 * context.
	 * 
	 * @param context
	 *            the context abstraction
	 * @return the Thymeleaf context
	 * @throws ContextException
	 *             when conversion couldn't be applied
	 */
	public org.thymeleaf.context.Context convert(Context context) throws ContextException {
		org.thymeleaf.context.Context thymeleafContext = new org.thymeleaf.context.Context();
		thymeleafContext.setVariables(context.getVariables());
		return thymeleafContext;
	}
}

package fr.sii.notification.core.template.context;

import java.util.Map;

import fr.sii.notification.core.exception.ContextException;

public interface Context {
	public Map<String, Object> getVariables() throws ContextException;
}

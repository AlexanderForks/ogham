package fr.sii.notification.template.thymeleaf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import fr.sii.notification.core.exception.template.EngineDetectionException;
import fr.sii.notification.core.template.Template;
import fr.sii.notification.core.template.context.Context;
import fr.sii.notification.core.template.detector.TemplateEngineDetector;

public class ThymeleafTemplateDetector implements TemplateEngineDetector {

	private static final Pattern NAMESPACE_PATTERN = Pattern.compile("xmlns[^=]+=\\s*\"http://www.thymeleaf.org\"");
	@Override
	public boolean canParse(String templateName, Context ctx, Template template) throws EngineDetectionException {
		try(InputStream stream = template.getInputStream()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line;
			boolean containsThymeleafNamespace;
			do {
				line = br.readLine();
				containsThymeleafNamespace = NAMESPACE_PATTERN.matcher(line).find();
			} while(line!=null && !containsThymeleafNamespace);
			return containsThymeleafNamespace;
		} catch (IOException e) {
			throw new EngineDetectionException("Failed to detect if template can be read by thymeleaf", e);
		}
	}

}

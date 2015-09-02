package fr.sii.ogham.it.email;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;

import fr.sii.ogham.core.builder.MessagingBuilder;
import fr.sii.ogham.core.exception.MessagingException;
import fr.sii.ogham.core.message.content.TemplateContent;
import fr.sii.ogham.core.service.MessagingService;
import fr.sii.ogham.email.message.Email;
import fr.sii.ogham.helper.email.AssertEmail;
import fr.sii.ogham.helper.email.ExpectedContent;
import fr.sii.ogham.helper.email.ExpectedEmail;
import fr.sii.ogham.helper.rule.LoggingTestRule;
import fr.sii.ogham.mock.context.SimpleBean;

public class EmailTemplatePrefixTest {
	private MessagingService oghamService;
	
	@Rule
	public final LoggingTestRule loggingRule = new LoggingTestRule();
	
	@Rule
	public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);
	
	@Before
	public void setUp() throws IOException {
		Properties props = new Properties(System.getProperties());
		props.load(getClass().getResourceAsStream("/application.properties"));
		props.setProperty("mail.smtp.host", ServerSetupTest.SMTP.getBindAddress());
		props.setProperty("mail.smtp.port", String.valueOf(ServerSetupTest.SMTP.getPort()));
		props.setProperty("ogham.email.template.prefix", "/template/thymeleaf/source/");
		props.setProperty("ogham.email.template.suffix", ".html");
		oghamService = new MessagingBuilder().useAllDefaults(props).build();
	}
	
	@Test
	public void withThymeleaf() throws MessagingException, javax.mail.MessagingException, IOException {
		oghamService.send(new Email("Template", new TemplateContent("simple", new SimpleBean("foo", 42)), "recipient@sii.fr"));
		AssertEmail.assertSimilar(new ExpectedEmail("Template", new ExpectedContent(getClass().getResourceAsStream("/template/thymeleaf/expected/simple_foo_42.html"), "text/html.*"), "test.sender@sii.fr", "recipient@sii.fr"), greenMail.getReceivedMessages());
	}

}

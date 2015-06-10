package fr.sii.notification.ut.email.sender.impl;

import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;

import fr.sii.notification.core.exception.MessageException;
import fr.sii.notification.email.builder.JavaMailBuilder;
import fr.sii.notification.email.message.Email;
import fr.sii.notification.email.message.EmailAddress;
import fr.sii.notification.email.sender.impl.JavaMailSender;
import fr.sii.notification.helper.email.AssertEmail;
import fr.sii.notification.helper.email.ExpectedEmail;
import fr.sii.notification.helper.rule.LoggingTestRule;

public class JavaMailSmtpTest {
	private JavaMailSender sender;
	
	@Rule
	public final LoggingTestRule loggingRule = new LoggingTestRule();
	
	@Rule
	public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);
	
	@Before
	public void setUp() throws IOException {
		Properties props = new Properties(System.getProperties());
		props.setProperty("mail.smtp.host", ServerSetupTest.SMTP.getBindAddress());
		props.setProperty("mail.smtp.port", String.valueOf(ServerSetupTest.SMTP.getPort()));
		sender = new JavaMailBuilder().useDefaults(props).build();
	}
	
	@Test
	public void simple() throws MessageException, MessagingException {
		sender.send(new Email("Subject", "Body", new EmailAddress("custom.sender@sii.fr"), "recipient@sii.fr"));
		AssertEmail.assertEquals(new ExpectedEmail("Subject", "Body", "custom.sender@sii.fr", "recipient@sii.fr"), greenMail.getReceivedMessages());
	}
	
	@Test
	public void attachment() throws MessageException, MessagingException {
//		sender.send(new Email("Subject", "Body", new EmailAddress("custom.sender@sii.fr"), "recipient@sii.fr", new ));
//		AssertEmail.assertEquals(new ExpectedEmail("Subject", "Body", "custom.sender@sii.fr", "recipient@sii.fr"), greenMail.getReceivedMessages());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalid() throws MessageException {
		sender.send(new Email("subject", "content"));
	}
}

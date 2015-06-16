package fr.sii.notification.ut.sms.sender.impl;

import java.io.IOException;

import org.jsmpp.bean.SubmitSm;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fr.sii.notification.core.exception.NotificationException;
import fr.sii.notification.core.sender.NotificationSender;
import fr.sii.notification.helper.rule.LoggingTestRule;
import fr.sii.notification.helper.sms.rule.JsmppServerRule;
import fr.sii.notification.helper.sms.rule.SmppServerRule;
import fr.sii.notification.sms.message.PhoneNumber;
import fr.sii.notification.sms.message.Recipient;
import fr.sii.notification.sms.message.Sender;
import fr.sii.notification.sms.message.Sms;
import fr.sii.notification.sms.message.addressing.AddressedPhoneNumber;
import fr.sii.notification.sms.message.addressing.translator.PhoneNumberTranslator;
import fr.sii.notification.sms.sender.impl.PhoneNumberTranslatorSender;

@RunWith(MockitoJUnitRunner.class)
public class PhoneNumberTranslatorSenderTest {
	private PhoneNumberTranslatorSender sender;

	@Rule
	public final LoggingTestRule loggingRule = new LoggingTestRule();

	@Rule
	public final SmppServerRule<SubmitSm> smppServer = new JsmppServerRule();

	@Mock(answer = Answers.RETURNS_SMART_NULLS)
	private PhoneNumberTranslator senderTranslatorMock;

	@Mock(answer = Answers.RETURNS_SMART_NULLS)
	private PhoneNumberTranslator recipientTranslatorMock;

	@Mock(answer = Answers.RETURNS_SMART_NULLS)
	private NotificationSender delegateMock;

	@Before
	public void setUp() throws IOException {
		sender = new PhoneNumberTranslatorSender(senderTranslatorMock,
				recipientTranslatorMock,
				delegateMock);
	}

	@Test
	public void simple() throws NotificationException, IOException {
		// given
		String givenContent = "sms content";
		PhoneNumber givenSenderNumber = new PhoneNumber("010203040506");
		PhoneNumber givenRecipientNumber = new PhoneNumber("0000000000");
		Sender givenSender = new Sender(givenSenderNumber);
		Recipient givenRecipient = new Recipient(givenRecipientNumber);
		Sms givenMessage = new Sms(givenContent, givenSender, givenRecipient);

		AddressedPhoneNumber mockSenderNumber = new AddressedPhoneNumber(null, null, null);
		AddressedPhoneNumber mockRecipientNumber = new AddressedPhoneNumber(null, null, null);
		
		BDDMockito.when(senderTranslatorMock.translate(givenSenderNumber)).thenReturn(mockSenderNumber);
		BDDMockito.when(recipientTranslatorMock.translate(givenRecipientNumber)).thenReturn(mockRecipientNumber);

		// when
		sender.send(givenMessage);

		// then
		Sms expexctedSms = new Sms(givenContent, new Sender(mockSenderNumber), new Recipient(mockRecipientNumber));

		Mockito.verify(delegateMock).send(expexctedSms);
	}
}

package fr.sii.notification.sms.sender;

import java.util.Map;

import fr.sii.notification.core.condition.Condition;
import fr.sii.notification.core.message.Message;
import fr.sii.notification.core.sender.MultiImplementationSender;
import fr.sii.notification.core.sender.NotificationSender;
import fr.sii.notification.sms.message.Sms;

public class SmsSender extends MultiImplementationSender<Sms> {

	public SmsSender() {
		super();
	}

	public SmsSender(Condition<Message> condition, NotificationSender implementation) {
		super(condition, implementation);
	}

	public SmsSender(Map<Condition<Message>, NotificationSender> implementations) {
		super(implementations);
	}
}

package fr.sii.notification.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fr.sii.notification.core.exception.builder.BuildException;
import fr.sii.notification.core.sender.ConditionalSender;
import fr.sii.notification.core.service.EverySupportingNotificationService;
import fr.sii.notification.core.service.NotificationService;
import fr.sii.notification.core.util.BuilderUtil;
import fr.sii.notification.email.builder.EmailBuilder;
import fr.sii.notification.sms.builder.SmsBuilder;

/**
 * Basic implementation of the builder that help to construct the notification
 * service. It relies on the specialized builders for:
 * <ul>
 * <li>{@link EmailBuilder} for helping to construct email sender</li>
 * <li>{@link SmsBuilder} for helping construct sms sender</li>
 * </ul>
 * 
 * @author Aurélien Baudet
 *
 */
public class NotificationBuilder implements NotificationServiceBuilder {

	/**
	 * The list of general builders used to construct senders. The constructed
	 * senders will be used by the notification service.
	 */
	private List<NotificationSenderBuilder<ConditionalSender>> builders;

	/**
	 * The specialized builder of sender for SMS messages
	 */
	private SmsBuilder smsBuilder;

	/**
	 * The specialized builder of sender for email messages
	 */
	private EmailBuilder emailBuilder;

	public NotificationBuilder() {
		super();
		builders = new ArrayList<NotificationSenderBuilder<ConditionalSender>>();
	}

	/**
	 * Build the notification service. The notification service relies on the
	 * generated senders. Each sender is able to manage one or multiple
	 * messages. The default implementation of the notification service is to
	 * ask each sender if it is able to handle the message and if it the case,
	 * then use this sender to really send the message. This implementation
	 * doesn't stop when the message is handled by a sender to possibly let
	 * another send the message through another channel.
	 * 
	 * @return the notification service instance
	 * @throws BuildException
	 *             when one of the sender couldn't be built
	 */
	public NotificationService build() throws BuildException {
		List<ConditionalSender> senders = new ArrayList<ConditionalSender>();
		for (NotificationSenderBuilder<ConditionalSender> builder : builders) {
			senders.add(builder.build());
		}
		return new EverySupportingNotificationService(senders);
	}

	/**
	 * Tells the builder to use all default behavior and values. The
	 * configuration values will be read from the system properties. The builder
	 * will construct the notification service with the following senders:
	 * <ul>
	 * <li>An email sender that is able to construct email content with or
	 * without template</li>
	 * <li>A SMS sender that is able to construct email content with or without
	 * template</li>
	 * </ul>
	 * 
	 * @return this builder instance for fluent use
	 * @see EmailBuilder#useDefaults() More information about created email
	 *      sender
	 * @see SmsBuilder#useDefaults() More information about created SMS
	 *      sender
	 */
	public NotificationBuilder useAllDefaults() {
		return useAllDefaults(BuilderUtil.getDefaultProperties());
	}

	/**
	 * Tells the builder to use all default behavior and values. The
	 * configuration values will be read from the provided properties. The
	 * builder will construct the notification service with the following
	 * senders:
	 * <ul>
	 * <li>An email sender that is able to construct email content with or
	 * without template</li>
	 * <li>A SMS sender that is able to construct email content with or without
	 * template</li>
	 * </ul>
	 * 
	 * @param properties
	 *            indicate which properties to use instead of using the system
	 *            ones
	 * @return this builder instance for fluent use
	 * @see EmailBuilder#useDefaults() More information about created email
	 *      sender
	 * @see SmsBuilder#useDefaults() More information about created SMS
	 *      sender
	 */
	public NotificationBuilder useAllDefaults(Properties properties) {
		useEmailDefaults(properties);
		useSmsDefaults(properties);
		return this;
	}

	/**
	 * Tells the builder to use default behaviors and values for email sender.
	 * The configuration values will be read from the provided properties.
	 * 
	 * This method is automatically called when using {@link #useAllDefaults()}.
	 * 
	 * @return this builder instance for fluent use
	 * @see EmailBuilder#useDefaults() More information about created email
	 *      sender
	 */
	public NotificationBuilder useEmailDefaults() {
		return useEmailDefaults(BuilderUtil.getDefaultProperties());
	}

	/**
	 * Tells the builder to use the default behaviors and values for email
	 * sender. The configuration values will be read from the provided
	 * properties.
	 * 
	 * This method is automatically called when using
	 * {@link #useAllDefaults(properties)}.
	 * 
	 * @param properties
	 *            indicate which properties to use instead of using the system
	 *            ones
	 * @return this builder instance for fluent use
	 * @see EmailBuilder#useDefaults() More information about created email
	 *      sender
	 */
	public NotificationBuilder useEmailDefaults(Properties properties) {
		withEmail();
		emailBuilder.useDefaults(properties);
		return this;
	}

	/**
	 * Tells the builder to use default behaviors and values for SMS sender. The
	 * configuration values will be read from the provided properties.
	 * 
	 * This method is automatically called when using {@link #useAllDefaults()}.
	 * 
	 * @return this builder instance for fluent use
	 * @see SmsBuilder#useDefaults() More information about created SMS
	 *      sender
	 */
	public NotificationBuilder useSmsDefaults() {
		return useSmsDefaults(BuilderUtil.getDefaultProperties());
	}

	/**
	 * Tells the builder to use the default behaviors and values for SMS sender.
	 * The configuration values will be read from the provided properties.
	 * 
	 * This method is automatically called when using
	 * {@link #useAllDefaults(properties)}.
	 * 
	 * @param properties
	 *            indicate which properties to use instead of using the system
	 *            ones
	 * @return this builder instance for fluent use
	 * @see SmsBuilder#useDefaults() More information about created SMS
	 *      sender
	 */
	public NotificationBuilder useSmsDefaults(Properties properties) {
		withSms();
		smsBuilder.useDefaults(properties);
		return this;
	}

	/**
	 * Tells the builder to activate the email sender.
	 * 
	 * This method is automatically called when using {@link #useAllDefaults()},
	 * {@link #useAllDefaults(properties)}, {@link #useEmailDefaults()} or
	 * {@link #useEmailDefaults(properties)}.
	 * 
	 * 
	 * @return this builder instance for fluent use
	 */
	public NotificationBuilder withEmail() {
		emailBuilder = new EmailBuilder();
		builders.add(emailBuilder);
		return this;
	}

	/**
	 * Tells the builder to activate the sms sender.
	 * 
	 * This method is automatically called when using {@link #useAllDefaults()},
	 * {@link #useAllDefaults(properties)}, {@link #useEmailDefaults()} or
	 * {@link #useEmailDefaults(properties)}.
	 * 
	 * @return this builder instance for fluent use
	 */
	public NotificationBuilder withSms() {
		smsBuilder = new SmsBuilder();
		builders.add(smsBuilder);
		return this;
	}

	/**
	 * Get access to the SMS specialized builder. The aim is to be able to fine
	 * tune the SMS sender.
	 * 
	 * @return The specialized builder for SMS sender.
	 */
	public SmsBuilder getSmsBuilder() {
		return smsBuilder;
	}

	/**
	 * Get access to the email specialized builder. The aim is to be able to
	 * fine tune the email sender.
	 * 
	 * @return The specialized builder for email sender.
	 */
	public EmailBuilder getEmailBuilder() {
		return emailBuilder;
	}
}

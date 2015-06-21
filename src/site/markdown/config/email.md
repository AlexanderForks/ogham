## Email configuration

### Globally configure sender address

You can configure sender address for all sent email by setting the property `ogham.email.from`. The value can either be an email address (`user@domain.host`) or an address with personal information (`User Name <user@domain.host>`). This property is used for every implementation.

Example:

```java
package fr.sii.ogham.sample.standard.email;

import java.util.Properties;

import fr.sii.ogham.core.builder.MessagingBuilder;
import fr.sii.ogham.core.exception.MessagingException;
import fr.sii.ogham.core.service.MessagingService;
import fr.sii.ogham.email.message.Email;

public class BasicSample {

	public static void main(String[] args) throws MessagingException {
		// configure properties (could be stored in a properties file or defined
		// in System properties)
		Properties properties = new Properties();
		// ...
		properties.put("ogham.email.from", "foo.bar@test.com");
		// Instantiate the messaging service using default behavior and
		// provided properties
		MessagingService service = new MessagingBuilder().useAllDefaults(properties).build();
		// send the email
		service.send(new Email("subject", "email content", "<recipient address>"));
		// => the sender address is "foo.bar@test.com"
	}

}
```

This global address is used only if nothing is specified in the email. If you explicitly set the sender address in the email constructor or using the setter, this value is used instead of the global one.

```java
package fr.sii.ogham.sample.standard.email;

import java.util.Properties;

import fr.sii.ogham.core.builder.MessagingBuilder;
import fr.sii.ogham.core.exception.MessagingException;
import fr.sii.ogham.core.service.MessagingService;
import fr.sii.ogham.email.message.Email;
import fr.sii.ogham.email.message.EmailAddress;

public class BasicSample {

	public static void main(String[] args) throws MessagingException {
		// configure properties (could be stored in a properties file or defined
		// in System properties)
		Properties properties = new Properties();
		// ...
		properties.put("ogham.email.from", "foo.bar@test.com");
		// Instantiate the messaging service using default behavior and
		// provided properties
		MessagingService service = new MessagingBuilder().useAllDefaults(properties).build();
		// send the email
		service.send(new Email("subject", "email content", new EmailAddress("override@test.com"), "<recipient address>"));
		// => the sender address is "override@test.com"
	}

}
```

### Configure username and password

Some SMTP servers need credentials. When using Java Mail API, you need to provide an `Authenticator`. 

Ogham has a shortcut to declare default authentication mechanism using a username and a password. Just set the two following properties:

 - ogham.email.authenticator.username
 - ogham.email.authenticator.password
 
It will automatically create an `Authenticator` with the provided values.

The Gmail sample shows how to use this feature:

```java
package fr.sii.ogham.sample.standard.email.gmail;

import java.util.Properties;

import fr.sii.ogham.core.builder.MessagingBuilder;
import fr.sii.ogham.core.exception.MessagingException;
import fr.sii.ogham.core.service.MessagingService;
import fr.sii.ogham.email.message.Email;

public class BasicGmailSSLSample {

	public static void main(String[] args) throws MessagingException {
		// configure properties (could be stored in a properties file or defined
		// in System properties)
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("ogham.email.authenticator.username", "<your gmail username>");
		properties.setProperty("ogham.email.authenticator.password", "<your gmail password>");
		properties.setProperty("ogham.email.from", "<your gmail address>");
		// Instantiate the messaging service using default behavior and
		// provided properties
		MessagingService service = new MessagingBuilder().useAllDefaults(properties).build();
		// send the email
		service.send(new Email("subject", "email content", "<recipient address>"));
	}

}
```

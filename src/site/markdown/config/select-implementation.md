## Select implementations

The library is able to handle many ways to send the same message. For example, sending an email can be done using the following ways:
 
 - Send email through SMTP using Java Mail API
 - Send email through SMTP using Apache Commons Email
 - Send email through SMTP using Spring Email
 - Send email through a SendGrid WebService
 - Send email through any other WebService


The library is using conditions to select the implementation to use at runtime. This section shows how the implementations are selected.
The box colored in grey are implementations that are not available for the moment.


### <a name="email"/>Email

![Email implementations selection](../images/email_implementations.png)

#### Send email using SMTP

To send email using SMTP, you need at least two mandatory properties:

 - mail.smtp.host: the host of the SMTP server
 - mail.smtp.port: the port of the SMTP server

There are many other properties that can be used by the Java Mail API for [configuring the SMTP session](https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html).

This example shows the minimal configuration for:

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
		properties.put("mail.smtp.host", "<your server host>");
		properties.put("mail.smtp.port", "<your server port>");
		properties.put("ogham.email.from", "<email address to display for the sender user>");
		// Instantiate the messaging service using default behavior and
		// provided properties
		MessagingService service = new MessagingBuilder().useAllDefaults(properties).build();
		// send the email
		service.send(new Email("subject", "email content", "<recipient address>"));
	}

}
```

#### Send email through SendGrid

SendGrid requires some authentication information for sending email. You can provide these values directly in properties:

 - ogham.email.sendgrid.username
 - ogham.email.sendgrid.password
 
SendGrid also allows to use directly an API key. You can provide this API key in properties:

 - ogham.email.sendgrid.api.key


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
		properties.put("ogham.email.sendgrid.username", "<your SendGrid username>");
		properties.put("ogham.email.sendgrid.password", "<your SendGrid password>");
		properties.put("ogham.email.from", "<email address to display for the sender user>");
		// Instantiate the messaging service using default behavior and
		// provided properties
		MessagingService service = new MessagingBuilder().useAllDefaults(properties).build();
		// send the email
		service.send(new Email("subject", "email content", "<recipient address>"));
	}

}
```

### <a name="sms"/>SMS

![SMS implementations selection](../images/sms_implementations.png)

Currently, only Cloudhopper SMPP is implemented. Other implementations will be available soon.


### Exclude dependency

You may want to exclude some implementation libraries due to license not appropriate for example. You can do it directly from your POM by excluding the unwanted dependencies. This is an example if you don't need to send email using SMTP and you don't want to have Java Mail dependency:

```xml
  ...
	<dependencies>
	  ...
		<dependency>
			<groupId>fr.sii.ogham</groupId>
			<artifactId>ogham-core</artifactId>
			<version>${ogham-module.version}</version>
			<exclusions>
				<exclusion>
					<groupId>com.sun.mail</groupId>
					<artifactId>javax.mail</artifactId>
				</exclusion>
				<exclusion>
					<groupId>javax.mail</groupId>
					<artifactId>javax.mail-api</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		...
	</dependencies>
	...
```


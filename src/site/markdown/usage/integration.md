## Integration

Current version: ${project.version}.

### Standalone usage

#### Maven dependency

To use Ogham, add it to your pom.xml:

```xml
  ...
	<dependencies>
	  ...
		<dependency>
			<groupId>fr.sii.ogham</groupId>
			<artifactId>ogham-core</artifactId>
			<version>${ogham-module.version}</version>
		</dependency>
		...
	</dependencies>
	...
```

See [how to use the module](index.html).

### <a name="integrate-with-spring-boot"/>Integrate with Spring Boot

To use Ogham and let Spring Boot auto-configuration mechanism handle integration, just add dependency to your pom.xml:

```xml
  ...
	<dependencies>
	  ...
		<dependency>
			<groupId>fr.sii.ogham</groupId>
			<artifactId>ogham-spring</artifactId>
			<version>${ogham-module.version}</version>
		</dependency>
		...
	</dependencies>
	...
```

It will automatically create and register MessagingService bean. It will also automatically use Spring Thymeleaf integration if it is in the classpath.
The Spring environment (configuration provided by configuration files for example) will be automatically used to configure the Ogham module.

Then to use it in your code, simply autowire the service:

```java
	public class UserService {
		@Autowired
		MessagingService messagingService;
		
		public UserDTO register(UserDTO user) {
			...
			messagingService.send(new Email("account registered", "email content", user.getEmailAddress()));
			...
		}
	}
```

See [how to use the module with Spring](spring.html).

### <a name="manual-integration-with-spring"/>Manual integration with Spring

To use Ogham without Spring Boot, you have to first add the dependency to your pom.xml:

```xml
  ...
	<dependencies>
	  ...
		<dependency>
			<groupId>fr.sii.ogham</groupId>
			<artifactId>ogham-spring</artifactId>
			<version>${ogham-module.version}</version>
		</dependency>
		...
	</dependencies>
	...
```

Then to use it in your code, simply inject the service:

```java
	public class UserService {
		@Autowired
		MessagingService messagingService;
		
		public UserDTO register(UserDTO user) {
			...
			messagingService.send(new Email("account registered", "email content", user.getEmailAddress()));
			...
		}
	}
```


#### Integration including Thymeleaf in Java

If you want to use Thymeleaf provided by Spring MVC, you have to add the following configuration:

```java
	@Configuration
	public static class DefaultConfiguration {
		@Autowired
		Environment environment;
		
		@Bean
		public MessagingService messagingService(SpringTemplateEngine engine) {
			MessagingBuilder builder = new MessagingBuilder().useAllDefaults(new PropertiesBridge().convert(environment));
			builder.getEmailBuilder().getTemplateBuilder().getThymeleafParser().withTemplateEngine(engine);
			builder.getSmsBuilder().getTemplateBuilder().getThymeleafParser().withTemplateEngine(engine);
			return builder.build();
		}
	}
```


#### Independent integration (standalone Thymeleaf) in Java

If you want to use Thymeleaf provided by Ogham (independent from Spring), you have to add the following configuration:

```java
	@Configuration
	public static class DefaultConfiguration {
		@Autowired
		Environment environment;
		
		@Bean
		public MessagingService messagingService() {
			return new MessagingBuilder().useAllDefaults(new PropertiesBridge().convert(environment)).build();
		}
	}
```

#### Independent integration in XML

To create the service using the builder, you can use `factory-method` feature provided by Spring. The builder requires Spring environment to be able to access configuration properties.

```xml
	<bean id="messagingService" class="fr.sii.ogham.core.builder.SpringXMLMessagingBuilder" factory-method="build">
		<constructor-arg ref="environment" />
	</bean>
```

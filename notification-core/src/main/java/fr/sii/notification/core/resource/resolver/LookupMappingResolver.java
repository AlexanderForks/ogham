package fr.sii.notification.core.resource.resolver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.sii.notification.core.exception.resource.ResourceResolutionException;
import fr.sii.notification.core.resource.Resource;
import fr.sii.notification.core.util.LookupUtils;

/**
 * <p>
 * Decorator resolver that is able to manage lookup prefix. It associates each
 * prefix to a dedicated resolver.
 * </p>
 * <p>
 * The lookup is a prefix that contains at least one ':' character. The lookup
 * prefix is case sensitive. For example, if the path is
 * <code>"classpath:/foo/bar.txt"</code> then the lookup prefix is
 * <code>"classpath:"</code>. If the path is <code>"foo:bar:/foobar.txt"</code>
 * then the lookup prefix is <code>"foo:bar:"</code>.
 * </p>
 * <p>
 * The lookup can also be empty in order to define a kind of default resolver if
 * no lookup is provided. The resource path could then be "/email/hello.html".
 * The resolver associated to empty string lookup will be used in this case.
 * </p>
 * <p>
 * This resolver is a conditional resolver. The resource path is supported only
 * if there exists a resolver for the provided lookup. Moreover, if the resolver
 * associated to the lookup is also a conditional resolver, the resource path
 * resolution is possible only if the dedicated resolver also indicates that the
 * resource path can be handled.
 * </p>
 * <p>
 * When the resource resolution starts, this implementation just delegates to
 * the resolver dedicated to the provided lookup.
 * </p>
 * 
 * @author Aurélien Baudet
 * @see ClassPathResolver
 * @see FileResolver
 * @see StringResourceResolver
 */
public class LookupMappingResolver implements ConditionalResolver {
	private static final Logger LOG = LoggerFactory.getLogger(LookupMappingResolver.class);

	/**
	 * The map that associates a lookup with a resolver
	 */
	private Map<String, ResourceResolver> mapping;

	/**
	 * Initialize the lookup resolution with a map that associates a lookup with
	 * a resolver. The lookup (key of the map) must not contain the ':'
	 * character.
	 * 
	 * @param mapping
	 *            association between a lookup and a resolver
	 */
	public LookupMappingResolver(Map<String, ResourceResolver> mapping) {
		super();
		this.mapping = mapping;
	}

	/**
	 * Initialize the lookup resolution with a lookup associated to a resolver.
	 * 
	 * @param lookup
	 *            the lookup string without the ':' character (example:
	 *            "classpath")
	 * @param resolver
	 *            the resolver to call for the lookup string
	 */
	public LookupMappingResolver(String lookup, ResourceResolver resolver) {
		this(new HashMap<String, ResourceResolver>());
		addMapping(lookup, resolver);
	}

	@Override
	public Resource getResource(String path) throws ResourceResolutionException {
		ResourceResolver resolver = getResolver(path);
		LOG.debug("Loading resource {} using resolver {}...", path, resolver);
		return resolver.getResource(LookupUtils.getRealPath(mapping, path));
	}

	@Override
	public boolean supports(String path) {
		LOG.debug("Finding resolver for resource {}...", path);
		String lookupType = LookupUtils.getLookupType(mapping, path);
		if (lookupType != null) {
			ResourceResolver resolver = mapping.get(lookupType);
			boolean supports = resolver instanceof ConditionalResolver ? ((ConditionalResolver) resolver).supports(LookupUtils.getRealPath(lookupType, path)) : true;
			if (supports) {
				LOG.debug("{} can be used for resolving lookup '{}' and can handle resource {}", resolver, lookupType, path);
			} else {
				LOG.debug("{} can be used for resolving lookup '{}' but can't handle resource {}", resolver, lookupType, path);
			}
			return supports;
		} else {
			LOG.debug("No resolver can handle lookup for path '{}'", path);
			return false;
		}
	}

	/**
	 * Add a resolver for the associated lookup. If a resolver already exists
	 * with the same lookup, the new provided resolver will replace it.
	 * 
	 * @param lookup
	 *            the lookup string without the ':' character (example:
	 *            "classpath")
	 * @param resolver
	 *            the resolver to call for the lookup string
	 * @return this instance for fluent use
	 */
	public LookupMappingResolver addMapping(String lookup, ResourceResolver resolver) {
		mapping.put(lookup, resolver);
		return this;
	}

	/**
	 * Give access to the resolver associated to the provided resource path.
	 * 
	 * @param path
	 *            the name or the path to the resource that may contain a lookup
	 *            prefix.
	 * @return the resolver to use for the resource or null if no resolver can
	 *         handle the lookup provided in the path
	 */
	public ResourceResolver getResolver(String path) {
		return LookupUtils.getResolver(mapping, path);
	}

	/**
	 * Give access to the resolver mapping.
	 * 
	 * @return the mapping indexed by the lookup string
	 */
	public Map<String, ResourceResolver> getMapping() {
		return mapping;
	}
}

package uk.ac.cardiff.raptor.ui.secure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLDiscovery;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.SAMLWebSSOHoKProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPArtifactBinding;
import org.springframework.security.saml.processor.HTTPPAOS11Binding;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.ArtifactResolutionProfile;
import org.springframework.security.saml.websso.ArtifactResolutionProfileImpl;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileECPImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Configuration here is taken from ,
 * https://github.com/vdenotaris/spring-boot-security-saml-sample/blob/master/src/main/java/com/vdenotaris/spring/boot/security/saml/web/config/WebSecurityConfig.java
 * 
 * @author philsmart
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@ConfigurationProperties(prefix = "saml.metadata")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Value(value = "${saml.enabled}")
	private boolean samlEnabled;

	@Value(value = "${saml.entity-id}")
	private String entityId;

	@Value(value = "${saml.key-store}")
	private String keystore;

	@Value(value = "${saml.keystore.store-password}")
	private String storePassword;

	@Value(value = "${saml.keystore.alias.name}")
	private String aliasName;

	@Value(value = "${saml.keystore.alias.password}")
	private String aliasPassword;

	private List<MetadataProviderInfo> providers;

	private Timer backgroundTaskTimer;

	private MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager;

	@Inject
	private SAMLUserDetailsServiceImpl samlUserDetails;

	@PostConstruct
	public void init() {
		if (samlEnabled) {
			this.backgroundTaskTimer = new Timer(true);
			this.multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
		}
	}

	@PreDestroy
	public void destroy() {
		if (samlEnabled) {
			this.backgroundTaskTimer.purge();
			this.backgroundTaskTimer.cancel();
			this.multiThreadedHttpConnectionManager.shutdown();
		}
	}

	/**
	 * Session registry, to allow server level access to logged in users
	 * 
	 * @return
	 */
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	/**
	 * Maps session information into the spring context
	 * 
	 * @return
	 */
	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
		return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}

	/**
	 * Defines the web based security configuration.
	 * 
	 * @param http
	 *            It allows configuring web based security for specific http
	 *            requests.
	 * @throws Exception
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		/*
		 * if you want no authentication context to trigger spring down the SAML auth
		 * route - we do not.
		 */
		// http.httpBasic().authenticationEntryPoint(samlEntryPoint());

		http.csrf().disable();

		http.authorizeRequests().mvcMatchers("/ui/admin.xhtml").hasAnyRole("ADMIN");

		http.addFilterAfter(new UserToMdcFilter(), AnonymousAuthenticationFilter.class);

		http.authorizeRequests().antMatchers("/resources/**").permitAll().antMatchers("/javax.faces.resource/**")
				.permitAll().antMatchers("/saml/**").permitAll().antMatchers("/**").authenticated().and()
				.authenticationProvider(new SimpleAuthenticationProvider()).formLogin().loginPage("/index.xhtml")
				.loginProcessingUrl("/login").permitAll();

		http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry()).maxSessionsPreventsLogin(false);

		http.logout().logoutSuccessUrl("/");
	}

	/**
	 * Sets a custom authentication provider. The SAML Provider is set into the
	 * {@link #samlWebSSOProcessingFilter()} when it calls the
	 * {@link #authenticationManager()} method. This obviously then becomes the
	 * global auth provider, unless another is directly specified on the
	 * {@link HttpSecurity} class.
	 * 
	 * @param auth
	 *            SecurityBuilder used to create an AuthenticationManager.
	 * @throws Exception
	 */
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		log.info("Are we creating a SAML Authentication Provider [{}]", samlEnabled ? "yes" : "no");
		if (samlEnabled) {
			auth.authenticationProvider(samlAuthenticationProvider());

		}

	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLAuthenticationProvider samlAuthenticationProvider() {
		final SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
		samlAuthenticationProvider.setUserDetails(samlUserDetails);
		samlAuthenticationProvider.setForcePrincipalAsString(false);

		return samlAuthenticationProvider;
	}

	public HttpClient httpClient() {
		return new HttpClient(this.multiThreadedHttpConnectionManager);
	}

	// XML parser pool needed for OpenSAML parsing
	@Bean(initMethod = "initialize")
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public StaticBasicParserPool parserPool() {
		return new StaticBasicParserPool();
	}

	@Bean(name = "parserPoolHolder")
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public ParserPoolHolder parserPoolHolder() {
		return new ParserPoolHolder();
	}

	/**
	 * Create a {@link HTTPMetadataProvider} from the input parameters.
	 * 
	 * @param metadataUrl
	 *            the URL of the metadata
	 * @param sigAliasName
	 *            the alias of the signature used to validate the downloaded
	 *            metadata. This is taken from the keystore used to configure this
	 *            saml SP.
	 * @param trustChecks
	 *            if true, performs signature validation.
	 * @return and {@link ExtendedMetadataDelegate}
	 * @throws MetadataProviderException
	 */
	// TODO this may not work, as each provider is going to share the timer and
	// the httpClient?
	public ExtendedMetadataDelegate createMetadataProvider(final String metadataUrl, final String sigAliasName,
			final boolean trustChecks) throws MetadataProviderException {

		final HTTPMetadataProvider httpMetadataProvider = new HTTPMetadataProvider(this.backgroundTaskTimer,
				httpClient(), metadataUrl);
		httpMetadataProvider.setParserPool(parserPool());
		final ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(httpMetadataProvider,
				extendedMetadata());
		extendedMetadataDelegate.setMetadataTrustCheck(trustChecks);
		extendedMetadataDelegate.setMetadataRequireSignature(trustChecks);
		extendedMetadataDelegate.setMetadataTrustedKeys(new HashSet<String>(Arrays.asList(sigAliasName)));

		backgroundTaskTimer.purge();
		return extendedMetadataDelegate;
	}

	@Bean
	@Qualifier("metadata")
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public CachingMetadataManager metadata() throws MetadataProviderException {
		log.info("Configuring MetadataProviders! " + providers);

		final List<MetadataProvider> metaProviders = new ArrayList<MetadataProvider>();
		for (final MetadataProviderInfo provInfo : providers) {

			metaProviders.add(createMetadataProvider(provInfo.getMetadataURL(), provInfo.getKeystoreSignatureAlias(),
					provInfo.isDoTrustCheck()));

		}
		final CachingMetadataManager man = new CachingMetadataManager(metaProviders);
		return man;
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLDefaultLogger samlLogger() {
		return new SAMLDefaultLogger();
	}

	// Provider of default SAML Context
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLContextProviderImpl contextProvider() {
		return new SAMLContextProviderImpl();
	}

	// Initialization of OpenSAML library
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public static SAMLBootstrap sAMLBootstrap() {
		return new SAMLBootstrap();
	}

	// SAML 2.0 WebSSO Assertion Consumer
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public WebSSOProfileConsumer webSSOprofileConsumer() {
		return new WebSSOProfileConsumerImpl();
	}

	// SAML 2.0 Holder-of-Key WebSSO Assertion Consumer
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
		return new WebSSOProfileConsumerHoKImpl();
	}

	// SAML 2.0 Web SSO profile
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public WebSSOProfile webSSOprofile() {
		return new WebSSOProfileImpl();
	}

	// SAML 2.0 Holder-of-Key Web SSO profile
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
		return new WebSSOProfileConsumerHoKImpl();
	}

	// SAML 2.0 ECP profile
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public WebSSOProfileECPImpl ecpprofile() {
		return new WebSSOProfileECPImpl();
	}

	/**
	 * Filter automatically generates default SP metadata. It does this for every
	 * request?
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public MetadataGenerator metadataGenerator() {
		final MetadataGenerator metadataGenerator = new MetadataGenerator();
		metadataGenerator.setEntityId(entityId);
		metadataGenerator.setExtendedMetadata(extendedMetadata());
		metadataGenerator.setIncludeDiscoveryExtension(true);

		metadataGenerator.setKeyManager(keyManager());
		return metadataGenerator;
	}

	// Setup advanced info about metadata
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public ExtendedMetadata extendedMetadata() {
		final ExtendedMetadata extendedMetadata = new ExtendedMetadata();
		extendedMetadata.setIdpDiscoveryEnabled(true);
		extendedMetadata.setSignMetadata(false);
		extendedMetadata.setEcpEnabled(false);

		return extendedMetadata;
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public KeyManager keyManager() {
		final DefaultResourceLoader loader = new DefaultResourceLoader();
		final Resource storeFile = loader.getResource(keystore);
		final Map<String, String> passwords = new HashMap<String, String>();
		passwords.put(aliasName, aliasPassword);
		final String defaultKey = aliasName;
		return new JKSKeyManager(storeFile, storePassword, passwords, defaultKey);
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public MetadataGeneratorFilter metadataGeneratorFilter() {
		return new MetadataGeneratorFilter(metadataGenerator());
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter() throws Exception {
		final SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter = new SAMLWebSSOHoKProcessingFilter();
		samlWebSSOHoKProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
		samlWebSSOHoKProcessingFilter.setAuthenticationManager(authenticationManager());
		samlWebSSOHoKProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
		return samlWebSSOHoKProcessingFilter;
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLProcessorImpl processor() {
		final Collection<SAMLBinding> bindings = new ArrayList<SAMLBinding>();
		bindings.add(httpRedirectDeflateBinding());
		bindings.add(httpPostBinding());
		bindings.add(artifactBinding(parserPool(), velocityEngine()));
		bindings.add(httpSOAP11Binding());
		bindings.add(httpPAOS11Binding());
		return new SAMLProcessorImpl(bindings);
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SingleLogoutProfile logoutprofile() {
		return new SingleLogoutProfileImpl();
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public HTTPArtifactBinding artifactBinding(final ParserPool parserPool, final VelocityEngine velocityEngine) {
		return new HTTPArtifactBinding(parserPool, velocityEngine, artifactResolutionProfile());
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLDiscovery samlIDPDiscovery() {
		final SAMLDiscovery idpDiscovery = new SAMLDiscovery();
		idpDiscovery.setIdpSelectionPath("/saml/idpSelection");
		return idpDiscovery;
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
		return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLLogoutFilter samlLogoutFilter() {
		return new SAMLLogoutFilter(successLogoutHandler(), new LogoutHandler[] { logoutHandler() },
				new LogoutHandler[] { logoutHandler() });
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SecurityContextLogoutHandler logoutHandler() {
		final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.setInvalidateHttpSession(true);
		logoutHandler.setClearAuthentication(true);
		return logoutHandler;
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
		final SimpleUrlLogoutSuccessHandler successLogoutHandler = new SimpleUrlLogoutSuccessHandler();
		successLogoutHandler.setDefaultTargetUrl("/");
		return successLogoutHandler;
	}

	// Entry point to initialize authentication, default values taken from
	// properties file
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLEntryPoint samlEntryPoint() {
		final SAMLEntryPoint samlEntryPoint = new SAMLEntryPoint();
		samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());
		return samlEntryPoint;
	}

	/**
	 * saml/SSO endpoint, which is what is called from the return from an IdP Authn
	 * request.It delegates to the configured authentication manager to authenticate
	 * the SAML response.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
		final SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
		samlWebSSOProcessingFilter.setAuthenticationManager(authenticationManager());
		samlWebSSOProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
		samlWebSSOProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
		/*
		 * Classes to allow registration of session
		 */
		final List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
		delegateStrategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));
		final CompositeSessionAuthenticationStrategy compSession = new CompositeSessionAuthenticationStrategy(
				delegateStrategies);
		samlWebSSOProcessingFilter.setSessionAuthenticationStrategy(compSession);
		return samlWebSSOProcessingFilter;
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() {
		final SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successRedirectHandler.setDefaultTargetUrl("/ui/search.xhtml");
		return successRedirectHandler;
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
		final SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
		failureHandler.setUseForward(true);
		failureHandler.setDefaultFailureUrl("/error.xhtml");
		return failureHandler;
	}

	// Bindings
	private ArtifactResolutionProfile artifactResolutionProfile() {
		final ArtifactResolutionProfileImpl artifactResolutionProfile = new ArtifactResolutionProfileImpl(httpClient());
		artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
		return artifactResolutionProfile;
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public HTTPSOAP11Binding soapBinding() {
		return new HTTPSOAP11Binding(parserPool());
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public HTTPPostBinding httpPostBinding() {
		return new HTTPPostBinding(parserPool(), velocityEngine());
	}

	// Initialization of the velocity engine
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public VelocityEngine velocityEngine() {
		return VelocityFactory.getEngine();
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public HTTPRedirectDeflateBinding httpRedirectDeflateBinding() {
		return new HTTPRedirectDeflateBinding(parserPool());
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public HTTPSOAP11Binding httpSOAP11Binding() {
		return new HTTPSOAP11Binding(parserPool());
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public HTTPPAOS11Binding httpPAOS11Binding() {
		return new HTTPPAOS11Binding(parserPool());
	}

	/**
	 * Returns the authentication manager currently used by Spring. It represents a
	 * bean definition with the aim allow wiring from other classes performing the
	 * Inversion of Control (IoC).
	 * 
	 * @throws Exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	// The filter is waiting for connections on URL suffixed with filterSuffix
	// and presents SP metadata there
	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public MetadataDisplayFilter metadataDisplayFilter() {
		return new MetadataDisplayFilter();
	}

	@Bean
	@ConditionalOnProperty(name = "saml.enabled", havingValue = "true")
	public WebSSOProfileOptions defaultWebSSOProfileOptions() {
		final WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
		webSSOProfileOptions.setIncludeScoping(false);
		return webSSOProfileOptions;
	}

	/**
	 * @return the providers
	 */
	public List<MetadataProviderInfo> getProviders() {
		return providers;
	}

	/**
	 * @param providers
	 *            the providers to set
	 */
	public void setProviders(final List<MetadataProviderInfo> providers) {
		this.providers = providers;
	}

	/**
	 * @return the samlEnabled
	 */
	public boolean isSamlEnabled() {
		return samlEnabled;
	}

}

package com.ricardo.financeiro.config;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ricardo.financeiro.config.format.BigDecimalFormatter;
import com.ricardo.financeiro.controller.LivrosController;
import com.ricardo.financeiro.session.TabelaItensVenda;
import com.ricardo.financeiro.thymeleaf.SalvavidasDialect;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration
@ComponentScan(basePackageClasses = { LivrosController.class, TabelaItensVenda.class })
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableCaching
@EnableAsync
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware {

	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
		bundle.setBasename("classpath:/messages");
		bundle.setDefaultEncoding("UTF-8"); // https://www.utf8-chartable.de/
		return bundle;
	}
	
	@Bean
	public BeanPostProcessor thymeleafDialectPostProcessor() {
	    return new BeanPostProcessor() {
	    	
	        @Override
	        public Object postProcessAfterInitialization(Object bean, String beanName) {

	            if (bean instanceof SpringTemplateEngine templateEngine) {
	                templateEngine.addDialect(new LayoutDialect());
	                templateEngine.addDialect(new SalvavidasDialect());
	            }
	            return bean;
	        }
	    };
	}
	
//	@Bean
//	public CacheManager cacheManager() {
//		CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
//				.maximumSize(3)
//				.expireAfterAccess(1, TimeUnit.MINUTES);
//		
//		GuavaCacheManager cacheManager = new GuavaCacheManager();
//		cacheManager.setCacheBuilder(cacheBuilder);
//		
//		return cacheManager;
//	}
	
	
	@Override
	public void addFormatters(FormatterRegistry registry) {

	    BigDecimalFormatter bigDecimalFormatter = new BigDecimalFormatter("#,##0.00");
	    registry.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
	    
	    NumberStyleFormatter integerFormatter = new NumberStyleFormatter("#,##0");
	    registry.addFormatterForFieldType(Integer.class, integerFormatter);

	    DateTimeFormatterRegistrar dateTimeFormatter = new DateTimeFormatterRegistrar();
	    dateTimeFormatter.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

	    dateTimeFormatter.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm"));
	    dateTimeFormatter.registerFormatters(registry);
	}

	
//	@Bean
//	public LocaleResolver localeResolver() {
//		return new FixedLocaleResolver(Locale.of("pt", "BR"));
//	}
	
	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		
		return localValidatorFactoryBean;
	}
	
	@Override
	public Validator getValidator() {
		return validator();
	}

}

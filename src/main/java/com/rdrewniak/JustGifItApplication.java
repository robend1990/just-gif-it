package com.rdrewniak;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class, JmxAutoConfiguration.class,
		WebSocketAutoConfiguration.class})
public class JustGifItApplication {

    @Value("${multipart.location}/gif/")
    private String gifLocation;

    public static void main(String[] args) {
        SpringApplication.run(JustGifItApplication.class, args);
    }

    @PostConstruct
    private void init() {
        File gifFolder = new File(gifLocation);
        if (!gifFolder.exists()) {
            gifFolder.mkdir();
        }
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/gif/**")
                        .addResourceLocations("file:" + gifLocation);
                super.addResourceHandlers(registry);
            }
        };
    }
    
    
    /**
     * Deregister filter which allows to use other http method that post and get 
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean deregisterHiddenHttpMethodFilter(HiddenHttpMethodFilter filter) {
    	FilterRegistrationBean bean = new FilterRegistrationBean(filter);
    	bean.setEnabled(false);
    	return bean;
    }
    
    /**
     * We are not using PUT method. lets disable put filter
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean deregisterHttpPutFormContentFilter(HttpPutFormContentFilter filter) {
    	FilterRegistrationBean bean = new FilterRegistrationBean(filter);
    	bean.setEnabled(false);
    	return bean;
    }
    
    /**
     * We are not using any request and session scoped beans. Lets disable filter
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean deregisterRequestContextFilter(RequestContextFilter filter) {
    	FilterRegistrationBean bean = new FilterRegistrationBean(filter);
    	bean.setEnabled(false);
    	return bean;
    }
    
}

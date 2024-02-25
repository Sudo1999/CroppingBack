package com.image.crop.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring6.ISpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

// Voir https://stackoverflow.com/questions/57172677/error-resolving-template-users-list-template-might-not-exist-or-might-not-be
// et https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#creating-and-configuring-the-template-engine
// et https://www.baeldung.com/spring-mvc-view-resolver-tutorial
// et https://www.tutorialspoint.com/viewresolver-in-spring-mvc

//@Configuration
//@ComponentScan(basePackages = "com.image.crop")
public class AppConfig {

    //private ISpringTemplateEngine templateEngine;
    // Interface ISpringTemplateEngine
    // => All Superinterfaces: org.thymeleaf.ITemplateEngine
    // => All Known Implementing Classes: SpringTemplateEngine

    private ISpringTemplateEngine templateEngine;

    @Bean
    public ViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        //resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
        resolver.setTemplateEngine(templateEngine);
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setViewNames(new String[] {"*.html"});
        return resolver;
    }

    private ITemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/templates/");
        resolver.setPrefix("/api/");
        resolver.setCacheable(false);
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    /*
    // Autre exemple de ViewResolver :
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(1);
        return resolver;
    }
    */
}

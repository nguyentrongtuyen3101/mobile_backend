package mobile.com.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

@Configuration // Thêm annotation này
@EnableWebMvc
@ComponentScan(basePackages = "mobile.com.api") // Thêm để scan controller
@PropertySource("classpath:application.properties") // Đọc file application.properties
public class web_config extends AbstractAnnotationConfigDispatcherServletInitializer implements WebMvcConfigurer {

	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{api_db_spring_config.class}; // Liên kết với file config chính
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
    	 return new Class<?>[]{web_config.class}; // Trả về chính nó
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/api/*"}; // Ánh xạ DispatcherServlet tới "/"
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return new Filter[]{filter}; // Thêm CharacterEncodingFilter
    }

    @Override
    protected String getServletName() {
        return "dispatcher"; // Tên servlet
    }

    @Override
    protected boolean isAsyncSupported() {
        return true; // Hỗ trợ async
    }

    // CharacterEncodingFilter (đã có trong getServletFilters, nhưng giữ @Bean để khớp XML)
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    // Message Converter cho API
    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converter.setSupportedMediaTypes(Arrays.asList(
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_HTML
        ));
        return converter;
    }

    // MultipartResolver cho file upload
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10485760); // 10MB
        resolver.setDefaultEncoding("UTF-8");
        return resolver;
    }
    
 // Thêm bean JavaMailSender
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("tinhluc2@gmail.com"); // Thay bằng email của bạn
        mailSender.setPassword("axqnsafslczyhcuy"); // Thay bằng App Password

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("/uploads/") // Sử dụng đường dẫn tương đối trong ứng dụng
                .setCachePeriod(0);
    }
}
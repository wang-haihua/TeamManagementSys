package cn.dagongren8.teamplus.config;

import cn.dagongren8.teamplus.component.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

/**
 * MVC配置类
 */
@Configuration
public class MyMvcConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Autowired
            ApplicationContext applicationContext;

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("index"); // 设置系统主入口
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                // 登录认证
                registry.addInterceptor(applicationContext.getBean(LoginHandlerInterceptor.class))
                        .addPathPatterns("/**")
                        .excludePathPatterns("/login/**", "/register/**", "/invite/**", "/webjars/**", "/static/**");
            }
        };
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        //单个文件最大
        factory.setMaxFileSize(DataSize.parse("50MB")); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse("100MB"));
        return factory.createMultipartConfig();
    }
}

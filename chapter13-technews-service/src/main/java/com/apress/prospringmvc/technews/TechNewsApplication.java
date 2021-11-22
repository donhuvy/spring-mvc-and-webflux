package com.apress.prospringmvc.technews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
@EnableEurekaClient
public class TechNewsApplication {

    public static void main(String... args) {
        // Look for configuration in technews-service.properties or technews-service.yml
        System.setProperty("spring.config.name", "technews-service");
        SpringApplication springApplication = new SpringApplication(TechNewsApplication.class);
        springApplication.run(args);
    }

    @Bean
    public RouterFunction<ServerResponse> router(TechNewsHandler handler) {
        return RouterFunctions
                .route(GET("/"), handler.main) // curl  http://localhost:4000
                .andRoute(GET("/index.htm"), handler.main) // curl -H "Accept:text/event-stream" http://localhost:4000/tech/news
                .andRoute(GET("/tech/news"), handler.data);
    }
}

@Component
class TechNewsHandler {
    public static final List<String> TECH_NEWS = List.of(
            "Apress merged with Springer.",
            "VMWare buys Pivotal.",
            "Twitter was hacked!",
            "Spring 6  is coming in December 2022.",
            "Amazon launches reactive API for DynamoDB.",
            "Java 17 will be released in September 2021.",
            "The JavaScript world is still 'The Wild Wild West'.",
            "Java modules, still a topic that developers frown upon."
    );
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    final HandlerFunction<ServerResponse> main = serverRequest -> ok()
            .contentType(MediaType.TEXT_HTML)
            .bodyValue("Tech News service up and running!");
    final HandlerFunction<ServerResponse> data = serverRequest -> ok().contentType(MediaType.TEXT_EVENT_STREAM)
            .body(Flux.interval(Duration.ofSeconds(5)).map(delay -> randomNews()), String.class);

    public static String randomNews() {
        return TECH_NEWS.get(RANDOM.nextInt(TECH_NEWS.size()));
    }
}



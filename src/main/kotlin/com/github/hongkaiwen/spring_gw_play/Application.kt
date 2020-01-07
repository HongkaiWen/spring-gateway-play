package com.github.hongkaiwen.spring_gw_play

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono






@SpringBootApplication
@RestController
open class Application {

    @Bean
    open fun myRoutes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route {
                it
                    .path("/get")
                    .filters {
                        it.addRequestHeader("Hello", "World")
                            .addResponseHeader("Hi", "baby")
                    }
                    .uri("http://localhost:8001")
            }.route {
                it.path("/delay/*").filters { it.hystrix { it.setFallbackUri("forward:/fallback") } }.uri("http://localhost:8001")
            }
            .build()
    }

    @RequestMapping("/fallback")
    fun fallback(): Mono<String> {
        return Mono.just("后端响应太慢了")
    }

    @Bean
    open fun customFilter(): GlobalFilter {
        return HelloFilter()
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
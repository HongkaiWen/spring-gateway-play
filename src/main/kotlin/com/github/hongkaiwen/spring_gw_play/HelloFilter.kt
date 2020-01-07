package com.github.hongkaiwen.spring_gw_play

import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class HelloFilter : GlobalFilter, Ordered {

  override fun filter(exchange: ServerWebExchange?, chain: GatewayFilterChain?): Mono<Void> {
    return doFilter(exchange, chain)
  }

  override fun getOrder(): Int {
    return 1
  }

  fun doFilter(exchange: ServerWebExchange?, chain: GatewayFilterChain?) = mono(Unconfined) {
    var param1 = exchange?.request?.queryParams?.get("hello")?.get(0) ?: ""
    println(getValue(param1))
    chain?.filter(exchange)?.awaitFirst()
  }

  suspend fun getValue(hello: String): String{
    delay(2000)
    return hello.toUpperCase()
  }

}

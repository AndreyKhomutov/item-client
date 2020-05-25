package com.learnreactivespring.controller;

import com.learnreactivespring.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/client/retrieve")
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient.get().uri("/v1/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Item in client project retrieve");
    }

    @GetMapping("/client/exchange")
    public Flux<Item> getAllItemsUsingExchange() {
        return webClient.get().uri("/v1/items")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("Item in client project exchange");

    }

    @GetMapping("/client/retrive/singleItem")
    public Mono<Item> getOneItemsUsingRetrieve() {

        String id = "ABC";

        return webClient.get().uri("/v1/items/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Item in client project retrieve single item");
    }

    @GetMapping("/client/exchange/singleItem")
    public Mono<Item> getOneItemsUsingExchange() {

        String id = "ABC";

        return webClient.get().uri("/v1/items/{id}", id)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("Item in client project retrieve single item");
    }

    @PostMapping("/client/createItem")
    public Mono<Item> createItem(@RequestBody Item item) {

        Mono<Item> itemMono = Mono.just(item);

        return webClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Item in client project insert single item");
    }

    @PutMapping("/client/updateItem/{id}")
    public Mono<Item> updateItem(@PathVariable String id,
                                 @RequestBody Item item) {

        Mono<Item> itemMono = Mono.just(item);

        return webClient.put().uri("/v1/items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Item in client project update single item");
    }

}

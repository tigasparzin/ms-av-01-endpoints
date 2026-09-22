package com.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "reproducaoClient", url = "${url}")
public interface ReproducaoClient {

    @PostMapping("/reproducao")
    public Reproducao registrar(@RequestBody Reproducao reproducao);
}

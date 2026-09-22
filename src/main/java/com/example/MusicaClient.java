package com.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "musicaClient", url = "${url}")
public interface MusicaClient {

    @GetMapping("/musicas/{id}")
    public Musica buscar(@PathVariable Integer id);
}

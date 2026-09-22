package com.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "playlistClient", url = "${url}")
public interface PlaylistClient {

    @GetMapping("/playlists/{playlistid}")
    public Playlist buscar(@PathVariable Integer playlistid);

    @PostMapping("/playlists/{playlistid}/musicas/{musicaId}")
    public PlaylistMusica adicionarMusica(@PathVariable Integer playlistid,
            @PathVariable Integer musicaId);
}

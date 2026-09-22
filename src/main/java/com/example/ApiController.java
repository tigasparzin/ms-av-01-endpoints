package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import feign.FeignException;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final MusicaClient musicaClient;
    private final PlaylistClient playlistClient;
    private final ReproducaoClient reproducaoClient;

    public ApiController(MusicaClient musicaClient, PlaylistClient playlistClient,
            ReproducaoClient reproducaoClient) {
        this.musicaClient = musicaClient;
        this.playlistClient = playlistClient;
        this.reproducaoClient = reproducaoClient;
    }

    @PostMapping("/adicionar/{playlistId}/musicas/{musicaId}")
    public ResponseEntity<String> adicionar(@PathVariable Integer playlistId,
            @PathVariable Integer musicaId) {
        try {
            Musica musica = this.musicaClient.buscar(musicaId);
            Playlist playlist = this.playlistClient.buscar(playlistId);
            this.playlistClient.adicionarMusica(playlistId, musicaId);

            String mensagem = "Música " + musica.titulo + " adicionada com sucesso à playlist " + playlist.nome;
            return new ResponseEntity<String>(mensagem, HttpStatus.OK);
        } catch (FeignException.NotFound exception) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/executar/{playlistId}")
    public ResponseEntity<Reproducao> executar(@PathVariable Integer playlistId) {
        try {
            this.playlistClient.buscar(playlistId);

            Reproducao reproducao = new Reproducao();
            reproducao.playlistid = playlistId;
            Reproducao registrada = this.reproducaoClient.registrar(reproducao);

            return new ResponseEntity<Reproducao>(registrada, HttpStatus.OK);
        } catch (FeignException.NotFound exception) {
            return new ResponseEntity<Reproducao>(HttpStatus.NOT_FOUND);
        }
    }
}

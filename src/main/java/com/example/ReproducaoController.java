package com.example;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reproducao")
public class ReproducaoController {

    private final ReproducaoRepository repo;

    public ReproducaoController(ReproducaoRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Reproducao> registrar(@Valid @RequestBody Reproducao reproducao) {
        reproducao.id = null;
        reproducao.datahora = LocalDateTime.now();
        this.repo.save(reproducao);
        return new ResponseEntity<Reproducao>(reproducao, HttpStatus.OK);
    }

    @GetMapping("/{playlistid}")
    public ResponseEntity<Iterable<Reproducao>> listar(@PathVariable Integer playlistid) {
        Iterable<Reproducao> reproducoes = this.repo.findByPlaylistid(playlistid);
        return new ResponseEntity<Iterable<Reproducao>>(reproducoes, HttpStatus.OK);
    }

    @GetMapping("/total/{playlistid}")
    public ResponseEntity<Long> total(@PathVariable Integer playlistid) {
        long total = this.repo.countByPlaylistid(playlistid);
        return new ResponseEntity<Long>(total, HttpStatus.OK);
    }
}

package com.example;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/musicas")
public class MusicaController {

    private final MusicaRepository repo;
    private final PlaylistMusicaRepository playlistMusicaRepo;

    public MusicaController(MusicaRepository repo, PlaylistMusicaRepository playlistMusicaRepo) {
        this.repo = repo;
        this.playlistMusicaRepo = playlistMusicaRepo;
    }

    @GetMapping
    public ResponseEntity<Iterable<Musica>> listar() {
        Iterable<Musica> musicas = this.repo.findAll();
        return new ResponseEntity<Iterable<Musica>>(musicas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Musica> buscar(@PathVariable Integer id) {
        Optional<Musica> resultado = this.repo.findById(id);

        if (resultado.isPresent()) {
            return new ResponseEntity<Musica>(resultado.get(), HttpStatus.OK);
        }

        return new ResponseEntity<Musica>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Musica> cadastrar(@Valid @RequestBody Musica musica) {
        this.repo.save(musica);
        return new ResponseEntity<Musica>(musica, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Musica> atualizar(@PathVariable Integer id,
            @Valid @RequestBody Musica musica) {
        boolean existe = this.repo.existsById(id);

        if (existe) {
            musica.id = id;
            this.repo.save(musica);
            return new ResponseEntity<Musica>(musica, HttpStatus.OK);
        }

        return new ResponseEntity<Musica>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        boolean existe = this.repo.existsById(id);

        if (existe) {
            Iterable<PlaylistMusica> associacoes = this.playlistMusicaRepo.findByMusicaid(id);
            this.playlistMusicaRepo.deleteAll(associacoes);
            this.repo.deleteById(id);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
}

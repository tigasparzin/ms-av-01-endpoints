package com.example;

import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistRepository repo;
    private final PlaylistMusicaRepository playlistMusicaRepo;
    private final MusicaRepository musicaRepo;

    public PlaylistController(PlaylistRepository repo, PlaylistMusicaRepository playlistMusicaRepo,
            MusicaRepository musicaRepo) {
        this.repo = repo;
        this.playlistMusicaRepo = playlistMusicaRepo;
        this.musicaRepo = musicaRepo;
    }

    @PostMapping
    public ResponseEntity<Playlist> cadastrar(@Valid @RequestBody Playlist playlist) {
        playlist.id = null;
        this.repo.save(playlist);
        return new ResponseEntity<Playlist>(playlist, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<Playlist>> listar() {
        Iterable<Playlist> playlists = this.repo.findAll();
        return new ResponseEntity<Iterable<Playlist>>(playlists, HttpStatus.OK);
    }

    @GetMapping("/{playlistid}")
    public ResponseEntity<Playlist> buscar(@PathVariable Integer playlistid) {
        Optional<Playlist> resultado = this.repo.findById(playlistid);

        if (resultado.isPresent()) {
            return new ResponseEntity<Playlist>(resultado.get(), HttpStatus.OK);
        }

        return new ResponseEntity<Playlist>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{playlistid}")
    public ResponseEntity<Playlist> atualizar(@PathVariable Integer playlistid,
            @Valid @RequestBody Playlist playlist) {
        boolean existe = this.repo.existsById(playlistid);

        if (existe) {
            playlist.id = playlistid;
            this.repo.save(playlist);
            return new ResponseEntity<Playlist>(playlist, HttpStatus.OK);
        }

        return new ResponseEntity<Playlist>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{playlistid}")
    @Transactional
    public ResponseEntity<Void> remover(@PathVariable Integer playlistid) {
        boolean existe = this.repo.existsById(playlistid);

        if (existe) {
            Iterable<PlaylistMusica> associacoes = this.playlistMusicaRepo.findByPlaylistid(playlistid);
            this.playlistMusicaRepo.deleteAll(associacoes);
            this.repo.deleteById(playlistid);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{playlistid}/musicas/{musicaId}")
    public ResponseEntity<PlaylistMusica> adicionarMusica(@PathVariable Integer playlistid,
            @PathVariable Integer musicaId) {
        boolean playlistExiste = this.repo.existsById(playlistid);
        boolean musicaExiste = this.musicaRepo.existsById(musicaId);

        if (!playlistExiste || !musicaExiste) {
            return new ResponseEntity<PlaylistMusica>(HttpStatus.NOT_FOUND);
        }

        PlaylistMusica associacao = new PlaylistMusica();
        associacao.playlistid = playlistid;
        associacao.musicaid = musicaId;
        this.playlistMusicaRepo.save(associacao);

        return new ResponseEntity<PlaylistMusica>(associacao, HttpStatus.OK);
    }

    @DeleteMapping("/{playlistid}/musicas/{musicaId}")
    public ResponseEntity<Void> removerMusica(@PathVariable Integer playlistid,
            @PathVariable Integer musicaId) {
        Iterable<PlaylistMusica> associacoes = this.playlistMusicaRepo
                .findByPlaylistidAndMusicaid(playlistid, musicaId);

        if (associacoes.iterator().hasNext()) {
            this.playlistMusicaRepo.deleteAll(associacoes);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{playlistid}/musicas")
    public ResponseEntity<List<Integer>> listarMusicas(@PathVariable Integer playlistid) {
        boolean playlistExiste = this.repo.existsById(playlistid);

        if (!playlistExiste) {
            return new ResponseEntity<List<Integer>>(HttpStatus.NOT_FOUND);
        }

        Iterable<PlaylistMusica> associacoes = this.playlistMusicaRepo.findByPlaylistid(playlistid);
        List<Integer> musicaIds = new ArrayList<Integer>();

        for (PlaylistMusica associacao : associacoes) {
            musicaIds.add(associacao.musicaid);
        }

        return new ResponseEntity<List<Integer>>(musicaIds, HttpStatus.OK);
    }
}

package com.example;

import org.springframework.data.repository.CrudRepository;

public interface PlaylistMusicaRepository extends CrudRepository<PlaylistMusica, Integer> {

    public Iterable<PlaylistMusica> findByPlaylistid(Integer playlistid);

    public Iterable<PlaylistMusica> findByMusicaid(Integer musicaid);

    public Iterable<PlaylistMusica> findByPlaylistidAndMusicaid(Integer playlistid, Integer musicaid);
}

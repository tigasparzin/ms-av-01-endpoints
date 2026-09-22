package com.example;

import org.springframework.data.repository.CrudRepository;

public interface ReproducaoRepository extends CrudRepository<Reproducao, Integer> {

    public Iterable<Reproducao> findByPlaylistid(Integer playlistid);

    public long countByPlaylistid(Integer playlistid);
}

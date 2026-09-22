package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "musicas")
public class Musica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @NotBlank(message = "O título é obrigatório")
    public String titulo;

    @NotBlank(message = "O artista é obrigatório")
    public String artista;

    @Size(max = 150, message = "O álbum deve ter no máximo 150 caracteres")
    public String album;

    @NotNull(message = "A duração é obrigatória")
    @Positive(message = "A duração deve ser maior que zero")
    public Integer duracao;

    @Size(max = 50, message = "O gênero deve ter no máximo 50 caracteres")
    public String genero;
}

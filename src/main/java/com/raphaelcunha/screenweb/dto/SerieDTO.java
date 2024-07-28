package com.raphaelcunha.screenweb.dto;

import com.raphaelcunha.screenweb.model.Category;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Category genero,
                       String atores,
                       String poster,
                       String sinopse) {
}

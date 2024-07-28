package com.raphaelcunha.screenweb.service;

import com.raphaelcunha.screenweb.controller.EpisodeDTO;
import com.raphaelcunha.screenweb.dto.SerieDTO;
import com.raphaelcunha.screenweb.model.Category;
import com.raphaelcunha.screenweb.model.Serie;
import com.raphaelcunha.screenweb.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> getAllSeries() {
        return convertData(repository.findAll());
    }

    public List<SerieDTO> getTop5() {
        return convertData(repository.findTop5ByOrderByRatingDesc());

    }

    public List<SerieDTO> getTop5SeriesRecentlyReleased() {
        return convertData(repository.findTop5ByOrderByEpisodesReleaseDateDesc());
        //Change to repository.get5SeriesByReleaseDate()
    }

    private List<SerieDTO> convertData(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitle(), s.getSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getUrlPoster(), s.getSynopsis()))
                .collect(Collectors.toList());
    }

    public SerieDTO getById(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitle(), s.getSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getUrlPoster(), s.getSynopsis());
        }
        return null;
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return (List<EpisodeDTO>) s.getEpisodes().stream()
                    .map(e -> new EpisodeDTO(e.getTitle(), e.getSeason(), e.getEpisodeNumber()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodeDTO> getEpisodesBySeason(Long id, Long season) {
        return repository.getEpisodesBySeason(id, season)
                .stream()
                .map(e -> new EpisodeDTO(e.getTitle(), e.getSeason(), e.getEpisodeNumber()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> getSeriesByCategory(String genre) {
        Category category = Category.fromPortuguese(genre);
        return convertData(repository.findByGenre(category));
    }
}

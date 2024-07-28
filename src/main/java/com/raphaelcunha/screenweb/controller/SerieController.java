package com.raphaelcunha.screenweb.controller;

import com.raphaelcunha.screenweb.dto.SerieDTO;
import com.raphaelcunha.screenweb.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> getSeries(){
        return service.getAllSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> getTop5(){
        return service.getTop5();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> getTop5SeriesRecentlyReleased(){
        return service.getTop5SeriesRecentlyReleased();
    }

    @GetMapping("/{id}")
    public SerieDTO getSerie(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodeDTO> getAllSeasons(@PathVariable Long id){
        return service.getAllSeasons(id);
    }

    @GetMapping("/{id}/temporadas/{season}")
    public List<EpisodeDTO> getEpisodesBySeasons(@PathVariable Long id, @PathVariable Long season){
        return service.getEpisodesBySeason(id, season);
    }

    @GetMapping("/categoria/{genre}")
    public List<SerieDTO> getSeriesByCategory(@PathVariable String genre){
        return service.getSeriesByCategory(genre);
    }

}

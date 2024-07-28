package com.raphaelcunha.screenweb.repository;

import com.raphaelcunha.screenweb.model.Category;
import com.raphaelcunha.screenweb.model.Episode;
import com.raphaelcunha.screenweb.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String serieName);

    List<Serie> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, Double rating);

    List<Serie> findTop5ByOrderByRatingDesc();

    List<Serie> findByGenre(Category category);

    //Foi substituída pela que está abaixo
    List<Serie> findBySeasonsLessThanEqualAndRatingGreaterThanEqual(int countSeasons, Double rating);

    @Query("SELECT s FROM Serie s WHERE s.seasons <= :countSeasons AND s.rating >= :rating")
    List<Serie> findBySeasonsAndRating(int countSeasons, Double rating);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE e.title ILIKE %:excerptTitle%")
    List<Episode> getEpisodeByExcerpt(String excerptTitle);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie ORDER BY e.rating DESC LIMIT 5")
    List<Episode> getTop5Episodes(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie AND YEAR(e.releaseDate) >= :year")
    List<Episode> getEpisodesReleasedFromADate(Serie serie, int year);

    List<Serie> findTop5ByOrderByEpisodesReleaseDateDesc();

    @Query("SELECT s FROM Serie s JOIN s.episodes e GROUP BY s ORDER BY MAX(e.releaseDate) DESC LIMIT 5")
    List<Serie> get5SeriesByReleaseDate();

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s.id = :id AND e.season = :season")
    List<Episode> getEpisodesBySeason(Long id, Long season);
}

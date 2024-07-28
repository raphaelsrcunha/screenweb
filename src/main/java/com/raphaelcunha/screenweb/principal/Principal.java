package com.raphaelcunha.screenweb.principal;

import com.raphaelcunha.screenweb.model.*;
import com.raphaelcunha.screenweb.repository.SerieRepository;
import com.raphaelcunha.screenweb.service.ConsumerAPI;
import com.raphaelcunha.screenweb.service.DataConversion;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner read = new Scanner(System.in);
    private ConsumerAPI consumerAPI = new ConsumerAPI();
    private DataConversion converter = new DataConversion();

    private final String URL = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=739d4cd";

    private List<SerieData> dataSeries = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> searchedSerie;

    private SerieRepository repository;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void showMenu(){

        var option = -1;

        while(option != 0){

            System.out.println("\n=================================================");
            System.out.println("                    ScreenWeb                    ");
            System.out.println("=================================================\n");
            System.out.println("1 - Buscar série");
            System.out.println("2 - Buscar episódios");
            System.out.println("3 - Listar séries buscadas");
            System.out.println("4 - Buscar série por título");
            System.out.println("5 - Buscar séries por ator");
            System.out.println("6 - Top 5 series");
            System.out.println("7 - Buscar série por categoria");
            System.out.println("8 - Buscar séries por número de temporadas");
            System.out.println("9 - Buscar episódios por trecho do nome");
            System.out.println("10 - Buscar Top 5 episódios de uma série");
            System.out.println("11 - Buscar episódios de uma série a partir de um ano de lançamento");
            System.out.println("0 - Sair");

            option = read.nextInt();
            read.nextLine();

            switch (option){
                case 1:
                    getSerieWeb();
                    break;

                case 2:
                    getSeasonsBySerie();
                    break;

                case 3:
                    getSeriesDB();
                    break;

                case 4:
                    getSeriesByTitle();
                    break;

                case 5:
                    getSeriesByActor();
                    break;

                case 6:
                    getTop5Series();
                    break;

                case 7:
                    getSeriesByGenre();
                    break;

                case 8:
                    getSeriesBySeasonsNumber();
                    break;

                case 9:
                    getEpisodeByTitle();
                    break;

                case 10:
                    getTop5Episodes();
                    break;

                case 11:
                    getEpisodesReleasedFromADate();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }

        List<SeasonData> seasonsData = new ArrayList<>();

    }



    private void getSerieWeb(){

        SerieData data = getSeriesData();
        Serie serie = new Serie(data);
        //dataSeries.add(data);
        repository.save(serie);
        System.out.println(data);

    }

    private SerieData getSeriesData(){
        System.out.println("\nDigite o nome da série: ");
        var serieName = read.nextLine();
        var json = consumerAPI.getData(URL + serieName.replace(" ", "+") + API_KEY);
        SerieData serieData = converter.getData(json, SerieData.class);
        return serieData;
    }

    private void getSeasonsBySerie() {
        getSeriesDB();
        System.out.println("Choose a serie to get more information:");
        var serieName = read.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(serieName.toLowerCase()))
                .findFirst();

        // Poderíamos substituir a busca acima pela linha abaixo.
        // Optional<Serie> serie = repository.findByTitleContainingIgnoreCase(serieName);

        if(serie.isPresent()) {

            var serieFound = serie.get();
            List<SeasonData> seasonsData = new ArrayList<>();

            for(int i = 1; i <= serieFound.getSeasons(); i++){
                var json = consumerAPI.getData("https://omdbapi.com/?t=" +
                        serieFound.getTitle().replace(" ", "+") + "&Season=" + i + "&apikey=739d4cd");
                SeasonData seasonData = converter.getData(json, SeasonData.class);
                seasonsData.add(seasonData);
            }
            seasonsData.forEach(seasonData -> System.out.println(seasonData));

            List<Episode> episodes = seasonsData.stream()
                    .flatMap(d -> d.episodes().stream()
                            .map(e -> new Episode(d.number(), e)))
                    .collect(Collectors.toList());

            serieFound.setEpisodes(episodes);
            repository.save(serieFound);

        } else {
            System.out.println("This serie was not found.");
        }

    }

    private void getSeriesDB() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);

    }

    private void getSeriesByTitle() {
        System.out.println("Escolha uma série pelo nome: ");
        var serieName = read.nextLine();
        searchedSerie = repository.findByTitleContainingIgnoreCase(serieName);

        if (searchedSerie.isPresent()) {
            System.out.println("Dados da série: " + searchedSerie.get());
        } else {
            System.out.println("Série não encontrada");
        }

    }

    private void getSeriesByActor() {
        System.out.println("Digite o nome do ator: ");
        var actorName = read.nextLine();
        System.out.println("A partir de qual nota?");
        var rating = read.nextDouble();
        List<Serie> searchedSeries = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);
        System.out.println("Séries em que o ator " + actorName + " trabalhou: ");
        searchedSeries.forEach(s ->
                System.out.println("\nNome da série: " + s.getTitle() + "\nRating: " + s.getRating() + "\n"));
    }

    private void getTop5Series() {
        List<Serie> seriesTop5 = repository.findTop5ByOrderByRatingDesc();
        seriesTop5.forEach(s ->
                System.out.println("\nNome da série: " + s.getTitle() + "\nRating: " + s.getRating() + "\n"));
    }

    private void getSeriesByGenre() {
        System.out.println("Deseja buscar séries de qual categoria?");
        var genreName = read.nextLine();
        Category category = Category.fromPortuguese(genreName);
        List<Serie> seriesByCategory = repository.findByGenre(category);
        System.out.println("Séries da categoria " + category);
        seriesByCategory.forEach(System.out::println);
    }

    private void getSeriesBySeasonsNumber() {
        System.out.println("Qual o número máximo de temporadas que você deseja?");
        var countSeasons = read.nextInt();
        read.nextLine();
        System.out.println("A partir de qual nota?");
        Double rating = read.nextDouble();
        read.nextLine();
        List<Serie> seriesBySeasonsNumber = repository.findBySeasonsAndRating(countSeasons, rating);
        seriesBySeasonsNumber.forEach(
                s -> System.out.println("\nNome da série: " + s.getTitle()
                        + "\nSeasons: " + s.getSeasons()
                        + "\nRating: " + s.getRating()));
    }

    private void getEpisodeByTitle() {
        System.out.println("Digite um trecho do nome do episódio: ");
        var excerptTitle = read.nextLine();

        List<Episode> episodesFound = repository.getEpisodeByExcerpt(excerptTitle);
        episodesFound.forEach(e -> System.out.println(
                "Série: " + e.getSerie().getTitle() +
                        "\nTítulo do episódio: " + e.getTitle() +
                        "\nTemporada: " + e.getSeason() +
                        "\nEpisódio: " + e.getEpisodeNumber() + "\n"
        ));
    }

    private void getTop5Episodes() {
        getSeriesByTitle();

        if (searchedSerie.isPresent()){
            Serie serie = searchedSerie.get();
            List<Episode> episodesTop5 = repository.getTop5Episodes(serie);
            System.out.println("Série: " + serie.getTitle() + "\n");
            episodesTop5.forEach(e -> System.out.println(
                    "Título do episódio: " + e.getTitle() +
                            "\nNota: " + e.getRating() +
                            "\nTemporada: " + e.getSeason() +
                            "\nEpisódio: " + e.getEpisodeNumber() + "\n"
            ));
        }
    }

    private void getEpisodesReleasedFromADate() {
        getSeriesByTitle();

        if(searchedSerie.isPresent()){
            System.out.println("Deseja episódios a partir de qual data?");
            var yearRelease = read.nextInt();
            read.nextLine();

            Serie serie = searchedSerie.get();
            List<Episode> episodesFromADate = repository.getEpisodesReleasedFromADate(serie, yearRelease);
            System.out.println("Série: " + serie.getTitle() + "\n");
            episodesFromADate.forEach(e -> System.out.println(
                    "Título do episódio: " + e.getTitle() +
                            "\nLançamento: " + e.getReleaseDate() +
                            "\nTemporada: " + e.getSeason() +
                            "\nEpisódio: " + e.getEpisodeNumber() + "\n"
            ));
        }
    }

}

//		for(int i = 1; i <= serieData.seasons(); i++){
//			json = consumerAPI.getData("https://omdbapi.com/?t=" + serieName.replace(" ", "+") + "&Season=" + i + "&apikey=739d4cd");
//			SeasonData seasonData = converter.getData(json, SeasonData.class);
//			seasonsData.add(seasonData);
//		}
////		seasonsData.forEach(System.out::println);
//
////        seasonsData.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));
//
//        List<EpisodeData> allEpisodes = seasonsData.stream()
//                .flatMap(t -> t.episodes().stream())
//                .collect(Collectors.toList());
//
//        allEpisodes.forEach(e -> System.out.println(e));
//
//        System.out.println("\nTOP FIVE\n");
//        allEpisodes.stream()
//                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
//                .limit(5)
//                .forEach(e -> System.out.println(e));
//
//        List<Episode> episodes = seasonsData.stream()
//                .flatMap(t -> t.episodes().stream()
//                        .map(d -> new Episode(t.number(), d))
//                ).collect(Collectors.toList());
//
//        episodes.forEach(System.out::println);
//
//        System.out.println("From what date would you like to see the episodes?");
//        var year = read.nextInt();
//        read.nextLine();
//
//        LocalDate dateSearch = LocalDate.of(year, 1, 1);
//
//        episodes.stream()
//                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(dateSearch))
//                .forEach(e -> System.out.println(
//                        "Season: " + e.getSeason() +
//                                " Episode: " + e.getTitle() +
//                                " Release date: " + e.getReleaseDate()
//                ));
//
//        System.out.println("Enter here an episode's name that you would like to know from which season is this episode: ");
//        var titleSearch = read.nextLine();
//
//        Optional<Episode> episodeSearch = episodes.stream()
//                .filter(e -> e.getTitle().toUpperCase().contains(titleSearch.toUpperCase()))
//                .findFirst();
//
//        if (episodeSearch.isPresent()){
//            System.out.println("Episode found!");
//            System.out.println("Title: " + episodeSearch.get().getTitle() + "\n" +
//                    "Episode number: " + episodeSearch.get().getEpisodeNumber() + "\n" +
//                    "Seasons: " + episodeSearch.get().getSeason());
//        } else{
//            System.out.println("Episode not found!");
//        }
//
//        Map<Integer, Double> ratingBySeason = episodes.stream()
//                .filter(e -> e.getRating() > 0.0)
//                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRating)));
//        System.out.println(ratingBySeason);
//
//        DoubleSummaryStatistics stat = episodes.stream()
//                .filter(e -> e.getRating() > 0.0)
//                .collect(Collectors.summarizingDouble(Episode::getRating));
//
//        System.out.println("Average: " + stat.getAverage() +
//                "\nBest rate: " + stat.getMax() +
//                "\nWorst rate: " + stat.getMin() +
//                "\nQuantity: " + stat.getCount());
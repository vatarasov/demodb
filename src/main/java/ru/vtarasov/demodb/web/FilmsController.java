package ru.vtarasov.demodb.web;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.FilmsFinder;
import ru.vtarasov.demodb.datasource.GenresFinder;
import ru.vtarasov.demodb.datasource.YearsFinder;
import ru.vtarasov.demodb.model.Film;

/**
 * @author vtarasov
 * @since 02.11.18
 */
@Controller
public class FilmsController {

    @Autowired
    private DataSourceFactory dsFactory;

    @Autowired
    private YearsFinder yearsFinder;

    @Autowired
    private GenresFinder genresFinder;

    @Autowired
    private FilmsFinder filmsFinder;

    @GetMapping("/films")
    public String list(@RequestParam(name = "str", required = false) String search, HttpServletRequest request, ModelMap model) throws Exception {
        Map<String, String[]> params = request.getParameterMap();

        int[] allYears = yearsFinder.findYears();

        Set<Integer> years = new HashSet<>();
        for (int year : allYears) {
            if (params.containsKey("year_" + year)) {
                years.add(year);
            }
        }

        String[] allGenres = genresFinder.findGenres();

        Set<String> genres = new HashSet<>();
        for (String genre : allGenres) {
            if (params.containsKey("genre_" + genre)) {
                genres.add(genre);
            }
        }

        Film[] films = filmsFinder.findFilms(search, years, genres);

        model.addAttribute("search", search);
        model.addAttribute("years", years);
        model.addAttribute("genres", genres);
        model.addAttribute("allYears", allYears);
        model.addAttribute("allGenres", allGenres);
        model.addAttribute("films", films);

        return "films";
    }
}

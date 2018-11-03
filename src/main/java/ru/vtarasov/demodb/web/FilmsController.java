package ru.vtarasov.demodb.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.model.GenresFinder;
import ru.vtarasov.demodb.model.YearFinder;

/**
 * @author vtarasov
 * @since 02.11.18
 */
@Controller
public class FilmsController {

    @Autowired
    private DataSourceFactory dsFactory;

    @GetMapping("/films")
    public String list(@RequestParam(name = "str", required = false) String search, HttpServletRequest request, ModelMap model) throws SQLException {
        Map<String, String[]> params = request.getParameterMap();

        int[] allYears = YearFinder.findYears(dsFactory.get());

        Set<Integer> years = new HashSet<>();
        for (int year : YearFinder.findYears(dsFactory.get())) {
            if (params.containsKey("year_" + year)) {
                years.add(year);
            }
        }

        String[] allGenres = GenresFinder.findGenres(dsFactory.get());

        Set<String> genres = new HashSet<>();
        for (String genre : GenresFinder.findGenres(dsFactory.get())) {
            if (params.containsKey("genre_" + genre)) {
                genres.add(genre);
            }
        }

        model.addAttribute("search", search);
        model.addAttribute("years", years);
        model.addAttribute("genres", genres);
        model.addAttribute("allYears", allYears);
        model.addAttribute("allGenres", allGenres);

        return "films";
    }
}

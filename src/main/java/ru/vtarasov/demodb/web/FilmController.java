package ru.vtarasov.demodb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.FilmGateway;
import ru.vtarasov.demodb.model.Film;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Controller
public class FilmController {

    @Autowired
    private DataSourceFactory dsf;

    @Autowired
    private FilmGateway filmGateway;

    @GetMapping("/film/{id}")
    public String index(ModelMap model, @PathVariable int id) throws Exception {
        Film film = new Film(filmGateway.load(id));
        model.addAttribute("film", film);

        return "film";
    }
}

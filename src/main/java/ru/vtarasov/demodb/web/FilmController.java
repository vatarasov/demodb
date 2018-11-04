package ru.vtarasov.demodb.web;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.model.Film;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Controller
public class FilmController {

    @Autowired
    private DataSourceFactory dsf;

    @GetMapping("/film/{id}")
    public String index(ModelMap model, @PathVariable int id) throws SQLException {
        Film film = Film.load(dsf.get(), id);
        model.addAttribute("film", film);

        return "film";
    }
}

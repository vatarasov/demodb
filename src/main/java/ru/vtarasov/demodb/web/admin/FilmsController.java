package ru.vtarasov.demodb.web.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.model.Factory;
import ru.vtarasov.demodb.model.Film;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 03.11.18
 */
@Controller("adminFilmsController")
public class FilmsController {

    @Autowired
    private DataSourceFactory dsFactory;

    @GetMapping("admin/films")
    public String index(ModelMap model) throws SQLException {
        List<Film> films = Film.list(dsFactory.get());
        model.addAttribute("films", films);
        return "admin/films/index";
    }

    @GetMapping("admin/films/add")
    public String getAdd(ModelMap model) throws SQLException {
        List<Factory> factories = Factory.list(dsFactory.get());
        List<Man> mans = Man.list(dsFactory.get());

        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/add";
    }

    @PostMapping("admin/films/add")
    public String add(@RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws SQLException {
        DataSource ds = dsFactory.get();

        Film film = new Film();
        film.setName(name);
        film.setYear(year);
        film.setGenre(genre);
        film.setDescription(description);

        if (factory != null && !"null".equals(factory)) {
            film.setFactory(Factory.load(ds, Integer.parseInt(factory)));
        }

        List<Man> stars_ = new ArrayList<>();
        for (String star : stars) {
            stars_.add(Man.load(ds, Integer.parseInt(star)));
        }
        film.setStars(stars_.toArray(new Man[0]));

        if (producer != null && !"null".equals(producer)) {
            film.setProducer(Man.load(ds, Integer.parseInt(producer)));
        }

        film.save(ds);

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws SQLException  {
        Film film = Film.load(dsFactory.get(), id);
        List<Factory> factories = Factory.list(dsFactory.get());
        List<Man> mans = Man.list(dsFactory.get());

        model.addAttribute("film", film);
        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/edit";
    }

    @PostMapping("admin/films/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws SQLException {
        DataSource ds = dsFactory.get();

        Film film = Film.load(ds, id);
        film.setName(name);
        film.setYear(year);
        film.setGenre(genre);
        film.setDescription(description);

        if (factory != null && !"null".equals(factory)) {
            film.setFactory(Factory.load(ds, Integer.parseInt(factory)));
        } else {
            film.setFactory(null);
        }

        List<Man> stars_ = new ArrayList<>();
        for (String star : stars) {
            stars_.add(Man.load(ds, Integer.parseInt(star)));
        }
        film.setStars(stars_.toArray(new Man[0]));

        if (producer != null && !"null".equals(producer)) {
            film.setProducer(Man.load(ds, Integer.parseInt(producer)));
        } else {
            film.setProducer(null);
        }

        film.update(ds);

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/delete")
    public String delete(@PathVariable int id) throws SQLException {
        DataSource ds = dsFactory.get();

        Film man = Film.load(ds, id);
        man.delete(ds);

        return "redirect:/admin/films";
    }
}

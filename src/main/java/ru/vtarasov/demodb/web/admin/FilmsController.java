package ru.vtarasov.demodb.web.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.FactoryFinder;
import ru.vtarasov.demodb.datasource.FilmFinder;
import ru.vtarasov.demodb.datasource.FilmRowGatewayImpl;
import ru.vtarasov.demodb.datasource.ManFinder;
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

    @Autowired
    private FactoryFinder factoryFinder;

    @Autowired
    private FilmFinder filmFinder;

    @Autowired
    private ManFinder manFinder;

    @GetMapping("admin/films")
    public String index(ModelMap model) throws Exception {
        List<Film> films = filmFinder.list().stream().map(Film::new).collect(Collectors.toList());
        model.addAttribute("films", films);
        return "admin/films/index";
    }

    @GetMapping("admin/films/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Factory> factories = factoryFinder.list().stream().map(Factory::new).collect(Collectors.toList());
        List<Man> mans = manFinder.list().stream().map(Man::new).collect(Collectors.toList());

        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/add";
    }

    @PostMapping("admin/films/add")
    public String add(@RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws Exception {

        Film film = new Film(new FilmRowGatewayImpl(dsFactory));
        film.setName(name);
        film.setYear(year);
        film.setGenre(genre);
        film.setDescription(description);

        if (factory != null && !"null".equals(factory)) {
            film.setFactory(new Factory(factoryFinder.load(Integer.parseInt(factory))));;
        }

        List<Man> stars_ = new ArrayList<>();
        for (String star : stars) {
            stars_.add(new Man(manFinder.load(Integer.parseInt(star))));
        }
        film.setStars(stars_.toArray(new Man[0]));

        if (producer != null && !"null".equals(producer)) {
            film.setProducer(new Man(manFinder.load(Integer.parseInt(producer))));
        }

        film.getGateway().save();

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Film film = new Film(filmFinder.load(id));
        List<Factory> factories = factoryFinder.list().stream().map(Factory::new).collect(Collectors.toList());
        List<Man> mans = manFinder.list().stream().map(Man::new).collect(Collectors.toList());

        model.addAttribute("film", film);
        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/edit";
    }

    @PostMapping("admin/films/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws Exception {

        Film film = new Film(filmFinder.load(id));
        film.setName(name);
        film.setYear(year);
        film.setGenre(genre);
        film.setDescription(description);

        if (factory != null && !"null".equals(factory)) {
            film.setFactory(new Factory(factoryFinder.load(Integer.parseInt(factory))));;
        }

        List<Man> stars_ = new ArrayList<>();
        for (String star : stars) {
            stars_.add(new Man(manFinder.load(Integer.parseInt(star))));
        }
        film.setStars(stars_.toArray(new Man[0]));

        if (producer != null && !"null".equals(producer)) {
            film.setProducer(new Man(manFinder.load(Integer.parseInt(producer))));
        }

        film.getGateway().update();

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        Film film = new Film(filmFinder.load(id));
        film.getGateway().delete();

        return "redirect:/admin/films";
    }
}

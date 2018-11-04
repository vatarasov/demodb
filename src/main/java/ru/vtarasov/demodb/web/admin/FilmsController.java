package ru.vtarasov.demodb.web.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.FactoryFinder;
import ru.vtarasov.demodb.datasource.FilmFinder;
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

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private FactoryFinder factoryFinder;

    @Autowired
    private FilmFinder filmFinder;

    @Autowired
    private ManFinder manFinder;

    @GetMapping("admin/films")
    public String index(ModelMap model) throws Exception {
        List<Film> films = filmFinder.list();
        model.addAttribute("films", films);
        return "admin/films/index";
    }

    @GetMapping("admin/films/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Factory> factories = factoryFinder.list();
        List<Man> mans = manFinder.list();

        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/add";
    }

    @PostMapping("admin/films/add")
    @Transactional
    public String add(@RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws Exception {

        Film film = new Film();
        film.setName(name);
        film.setYear(year);
        film.setGenre(genre);
        film.setDescription(description);

        if (factory != null && !"null".equals(factory)) {
            film.setFactory(factoryFinder.load(Integer.parseInt(factory)));
        }

        Set<Man> stars_ = new HashSet<>();
        for (String star : stars) {
            stars_.add(manFinder.load(Integer.parseInt(star)));
        }
        film.setStars(stars_);

        if (producer != null && !"null".equals(producer)) {
            film.setProducer(manFinder.load(Integer.parseInt(producer)));
        }

        em.persist(film);

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Film film = filmFinder.load(id);
        List<Factory> factories = factoryFinder.list();
        List<Man> mans = manFinder.list();

        model.addAttribute("film", film);
        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/edit";
    }

    @PostMapping("admin/films/{id}/edit")
    @Transactional
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws Exception {

        Film film = filmFinder.load(id);
        film.setName(name);
        film.setYear(year);
        film.setGenre(genre);
        film.setDescription(description);

        if (factory != null && !"null".equals(factory)) {
            film.setFactory(factoryFinder.load(Integer.parseInt(factory)));
        } else {
            film.setFactory(null);
        }

        Set<Man> stars_ = new HashSet<>();
        for (String star : stars) {
            stars_.add(manFinder.load(Integer.parseInt(star)));
        }
        film.setStars(stars_);

        if (producer != null && !"null".equals(producer)) {
            film.setProducer(manFinder.load(Integer.parseInt(producer)));
        } else {
            film.setProducer(null);
        }

        em.merge(film);

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/delete")
    @Transactional
    public String delete(@PathVariable int id) throws Exception {
        Film film = filmFinder.load(id);
        em.remove(film);

        return "redirect:/admin/films";
    }
}

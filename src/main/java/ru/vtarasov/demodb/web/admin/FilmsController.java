package ru.vtarasov.demodb.web.admin;

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
import ru.vtarasov.demodb.datasource.FactoryMapper;
import ru.vtarasov.demodb.datasource.FilmMapper;
import ru.vtarasov.demodb.datasource.ManMapper;
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
    private FactoryMapper factoryMapper;

    @Autowired
    private FilmMapper filmMapper;

    @Autowired
    private ManMapper manMapper;

    @GetMapping("admin/films")
    public String index(ModelMap model) throws Exception {
        List<Film> films = filmMapper.list();
        model.addAttribute("films", films);
        return "admin/films/index";
    }

    @GetMapping("admin/films/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Factory> factories = factoryMapper.list();
        List<Man> mans = manMapper.list();

        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/add";
    }

    @PostMapping("admin/films/add")
    public String add(@RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws Exception {
        DataSource ds = dsFactory.get();

        Film film = new Film();
        film.setName(name);
        film.setYear(year);
        film.setGenre(genre);
        film.setDescription(description);

        if (factory != null && !"null".equals(factory)) {
            film.setFactory(factoryMapper.load(Integer.parseInt(factory)));
        }

        List<Man> stars_ = new ArrayList<>();
        for (String star : stars) {
            stars_.add(manMapper.load(Integer.parseInt(star)));
        }
        film.setStars(stars_.toArray(new Man[0]));

        if (producer != null && !"null".equals(producer)) {
            film.setProducer(manMapper.load(Integer.parseInt(producer)));
        }

        filmMapper.save(film);

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Film film = filmMapper.load(id);
        List<Factory> factories = factoryMapper.list();
        List<Man> mans = manMapper.list();

        model.addAttribute("film", film);
        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/edit";
    }

    @PostMapping("admin/films/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws Exception {
        Film film = filmMapper.load(id);
        film.setName(name);
        film.setYear(year);
        film.setGenre(genre);
        film.setDescription(description);

        if (factory != null && !"null".equals(factory)) {
            film.setFactory(factoryMapper.load(Integer.parseInt(factory)));
        } else {
            film.setFactory(null);
        }

        List<Man> stars_ = new ArrayList<>();
        for (String star : stars) {
            stars_.add(manMapper.load(Integer.parseInt(star)));
        }
        film.setStars(stars_.toArray(new Man[0]));

        if (producer != null && !"null".equals(producer)) {
            film.setProducer(manMapper.load(Integer.parseInt(producer)));
        } else {
            film.setProducer(null);
        }

        filmMapper.update(film);

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        Film film = filmMapper.load(id);
        filmMapper.delete(film);

        return "redirect:/admin/films";
    }
}

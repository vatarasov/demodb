package ru.vtarasov.demodb.web.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.FactoryGateway;
import ru.vtarasov.demodb.datasource.FilmGateway;
import ru.vtarasov.demodb.datasource.ManGateway;
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
    private FactoryGateway factoryGateway;

    @Autowired
    private FilmGateway filmGateway;

    @Autowired
    private ManGateway manGateway;

    @GetMapping("admin/films")
    public String index(ModelMap model) throws Exception {
        List<Film> films = filmGateway.list().stream().map(Film::new).collect(Collectors.toList());
        model.addAttribute("films", films);
        return "admin/films/index";
    }

    @GetMapping("admin/films/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Factory> factories = factoryGateway.list().stream().map(Factory::new).collect(Collectors.toList());
        List<Man> mans = manGateway.list().stream().map(Man::new).collect(Collectors.toList());

        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/add";
    }

    @PostMapping("admin/films/add")
    public String add(@RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws Exception {

        Integer factoryId = null;
        if (factory != null && !"null".equals(factory)) {
            factoryId = Integer.parseInt(factory);
        }

        List<Integer> starIds = new ArrayList<>();
        for (String star : stars) {
            starIds.add(Integer.parseInt(star));
        }

        Integer producerId = null;
        if (producer != null && !"null".equals(producer)) {
            producerId = Integer.parseInt(producer);
        }

        filmGateway.save(name, year, genre, factoryId, starIds, producerId, description);

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Film film = new Film(filmGateway.load(id));
        List<Factory> factories = factoryGateway.list().stream().map(Factory::new).collect(Collectors.toList());
        List<Man> mans = manGateway.list().stream().map(Man::new).collect(Collectors.toList());

        model.addAttribute("film", film);
        model.addAttribute("factories", factories);
        model.addAttribute("mans", mans);

        return "admin/films/edit";
    }

    @PostMapping("admin/films/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam int year, @RequestParam String genre,
        @RequestParam String factory, @RequestParam String[] stars, @RequestParam String producer,
        @RequestParam String description) throws Exception {

        Integer factoryId = null;
        if (factory != null && !"null".equals(factory)) {
            factoryId = Integer.parseInt(factory);
        }

        List<Integer> starIds = new ArrayList<>();
        for (String star : stars) {
            starIds.add(Integer.parseInt(star));
        }

        Integer producerId = null;
        if (producer != null && !"null".equals(producer)) {
            producerId = Integer.parseInt(producer);
        }

        filmGateway.update(id, name, year, genre, factoryId, starIds, producerId, description);

        return "redirect:/admin/films";
    }

    @GetMapping("admin/films/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        filmGateway.delete(id);

        return "redirect:/admin/films";
    }
}

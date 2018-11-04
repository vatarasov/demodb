package ru.vtarasov.demodb.web.admin;

import java.util.List;
import java.util.stream.Collectors;
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
import ru.vtarasov.demodb.datasource.CountryFinder;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.ManFinder;
import ru.vtarasov.demodb.model.Country;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 03.11.18
 */
@Controller
public class MansController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CountryFinder countryFinder;

    @Autowired
    private ManFinder manFinder;

    @GetMapping("admin/mans")
    public String index(ModelMap model) {
        List<Man> mans = manFinder.list();
        model.addAttribute("mans", mans);
        return "admin/mans/index";
    }

    @GetMapping("admin/mans/add")
    public String getAdd(ModelMap model) {
        List<Country> countries = countryFinder.list();
        model.addAttribute("countries", countries);

        return "admin/mans/add";
    }

    @PostMapping("admin/mans/add")
    @Transactional
    public String add(@RequestParam String name, @RequestParam String country) {
        Man man = new Man();
        man.setName(name);

        if (country != null && !"null".equals(country)) {
            man.setCountry(countryFinder.load(Integer.parseInt(country)));
        }

        em.persist(man);

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id)  {
        Man man = manFinder.load(id);
        List<Country> countries = countryFinder.list();

        model.addAttribute("man", man);
        model.addAttribute("countries", countries);

        return "admin/mans/edit";
    }

    @PostMapping("admin/mans/{id}/edit")
    @Transactional
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam String country) {
        Man man = manFinder.load(id);
        man.setName(name);

        if (country != null && !"null".equals(country)) {
            man.setCountry(countryFinder.load(Integer.parseInt(country)));
        } else {
            man.setCountry(null);
        }

        em.merge(man);

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/delete")
    @Transactional
    public String delete(@PathVariable int id) throws Exception {
        Man man = manFinder.load(id);
        em.remove(man);

        return "redirect:/admin/mans";
    }
}

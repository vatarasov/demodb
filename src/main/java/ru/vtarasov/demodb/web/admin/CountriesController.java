package ru.vtarasov.demodb.web.admin;

import java.util.List;
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
import ru.vtarasov.demodb.model.Country;

/**
 * @author vtarasov
 * @since 03.11.18
 */
@Controller
public class CountriesController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CountryFinder countryFinder;

    @GetMapping("admin/countries")
    public String index(ModelMap model) throws Exception {
        List<Country> countries = countryFinder.list();

        model.addAttribute("countries", countries);
        return "admin/countries/index";
    }

    @GetMapping("admin/countries/add")
    public String addGet() {
        return "admin/countries/add";
    }

    @PostMapping("admin/countries/add")
    @Transactional
    public String addPost(@RequestParam String name) throws Exception {
        Country country = new Country();
        country.setName(name);
        em.persist(country);

        return "redirect:/admin/countries";
    }

    @GetMapping("admin/countries/{id}/edit")
    public String editGet(ModelMap model, @PathVariable int id) throws Exception  {
        Country country = countryFinder.load(id);
        model.put("country", country);

        return "admin/countries/edit";
    }

    @PostMapping("admin/countries/{id}/edit")
    @Transactional
    public String editPost(@PathVariable int id, @RequestParam String name) throws Exception {
        Country country = countryFinder.load(id);
        country.setName(name);
        em.merge(country);

        return "redirect:/admin/countries";
    }

    @GetMapping("admin/countries/{id}/delete")
    @Transactional
    public String delete(@PathVariable int id) throws Exception {
        Country country = countryFinder.load(id);
        em.remove(country);

        return "redirect:/admin/countries";
    }
}

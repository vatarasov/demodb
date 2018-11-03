package ru.vtarasov.demodb.web.admin;

import java.sql.SQLException;
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
import ru.vtarasov.demodb.model.Country;

/**
 * @author vtarasov
 * @since 03.11.18
 */
@Controller
public class CountriesController {

    @Autowired
    private DataSourceFactory dsFactory;

    @GetMapping("admin/countries")
    public String index(ModelMap model) throws SQLException {
        List<Country> countries = Country.list(dsFactory.get());
        model.addAttribute("countries", countries);
        return "admin/countries/index";
    }

    @GetMapping("admin/countries/add")
    public String addGet() {
        return "admin/countries/add";
    }

    @PostMapping("admin/countries/add")
    public String addPost(@RequestParam String name) throws SQLException {
        Country country = new Country();
        country.setName(name);
        country.save(dsFactory.get());

        return "redirect:/admin/countries";
    }

    @GetMapping("admin/countries/{id}/edit")
    public String editGet(ModelMap model, @PathVariable int id) throws SQLException  {
        Country country = Country.load(dsFactory.get(), id);
        model.put("country", country);

        return "admin/countries/edit";
    }

    @PostMapping("admin/countries/{id}/edit")
    public String editPost(@PathVariable int id, @RequestParam String name) throws SQLException {
        DataSource ds = dsFactory.get();

        Country country = Country.load(ds, id);
        country.setName(name);
        country.update(ds);

        return "redirect:/admin/countries";
    }

    @GetMapping("admin/countries/{id}/delete")
    public String delete(@PathVariable int id) throws SQLException {
        DataSource ds = dsFactory.get();

        Country country = Country.load(ds, id);
        country.delete(ds);

        return "redirect:/admin/countries";
    }
}

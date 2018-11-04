package ru.vtarasov.demodb.web.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.CountryGateway;
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

    @Autowired
    private CountryGateway countryGateway;

    @GetMapping("admin/countries")
    public String index(ModelMap model) throws Exception {
        List<Country> countries = countryGateway.list().stream().map(Country::new).collect(Collectors.toList());

        model.addAttribute("countries", countries);
        return "admin/countries/index";
    }

    @GetMapping("admin/countries/add")
    public String addGet() {
        return "admin/countries/add";
    }

    @PostMapping("admin/countries/add")
    public String addPost(@RequestParam String name) throws Exception {
        countryGateway.save(name);

        return "redirect:/admin/countries";
    }

    @GetMapping("admin/countries/{id}/edit")
    public String editGet(ModelMap model, @PathVariable int id) throws Exception  {
        Country country = new Country(countryGateway.load(id));
        model.put("country", country);

        return "admin/countries/edit";
    }

    @PostMapping("admin/countries/{id}/edit")
    public String editPost(@PathVariable int id, @RequestParam String name) throws Exception {
        countryGateway.update(id, name);

        return "redirect:/admin/countries";
    }

    @GetMapping("admin/countries/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        countryGateway.delete(id);

        return "redirect:/admin/countries";
    }
}

package ru.vtarasov.demodb.web.admin;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.CountryFinder;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.ManFinder;
import ru.vtarasov.demodb.datasource.ManRowGateway;
import ru.vtarasov.demodb.datasource.ManRowGatewayImpl;
import ru.vtarasov.demodb.model.Country;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 03.11.18
 */
@Controller
public class MansController {

    @Autowired
    private DataSourceFactory dsFactory;

    @Autowired
    private CountryFinder countryFinder;

    @Autowired
    private ManFinder manFinder;

    @GetMapping("admin/mans")
    public String index(ModelMap model) throws Exception {
        List<Man> mans = manFinder.list().stream().map(Man::new).collect(Collectors.toList());
        model.addAttribute("mans", mans);
        return "admin/mans/index";
    }

    @GetMapping("admin/mans/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Country> countries = countryFinder.list().stream().map(Country::new).collect(Collectors.toList());
        model.addAttribute("countries", countries);

        return "admin/mans/add";
    }

    @PostMapping("admin/mans/add")
    public String add(@RequestParam String name, @RequestParam String country) throws Exception {
        Man man = new Man(new ManRowGatewayImpl(dsFactory));
        man.setName(name);

        if (country != null && !"null".equals(country)) {
            man.setCountry(new Country(countryFinder.load(Integer.parseInt(country))));
        }

        man.getGateway().save();

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Man man = new Man(manFinder.load(id));
        List<Country> countries = countryFinder.list().stream().map(Country::new).collect(Collectors.toList());

        model.addAttribute("man", man);
        model.addAttribute("countries", countries);

        return "admin/mans/edit";
    }

    @PostMapping("admin/mans/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam String country) throws Exception {
        Man man = new Man(manFinder.load(id));
        man.setName(name);

        if (country != null && !"null".equals(country)) {
            man.setCountry(new Country(countryFinder.load(Integer.parseInt(country))));
        } else {
            man.setCountry(null);
        }

        man.getGateway().update();

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        Man man = new Man(manFinder.load(id));
        man.getGateway().delete();

        return "redirect:/admin/mans";
    }
}

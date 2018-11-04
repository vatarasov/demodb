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
import ru.vtarasov.demodb.datasource.FactoryFinder;
import ru.vtarasov.demodb.datasource.FactoryRowGatewayImpl;
import ru.vtarasov.demodb.model.Country;
import ru.vtarasov.demodb.model.Factory;

/**
 * @author vtarasov
 * @since 03.11.18
 */
@Controller
public class FactoriesController {

    @Autowired
    private DataSourceFactory dsFactory;

    @Autowired
    private CountryFinder countryFinder;

    @Autowired
    private FactoryFinder factoryFinder;

    @GetMapping("admin/factories")
    public String index(ModelMap model) throws Exception {
        List<Factory> factories = factoryFinder.list().stream().map(Factory::new).collect(Collectors.toList());
        model.addAttribute("factories", factories);
        return "admin/factories/index";
    }

    @GetMapping("admin/factories/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Country> countries = countryFinder.list().stream().map(Country::new).collect(Collectors.toList());
        model.addAttribute("countries", countries);

        return "admin/factories/add";
    }

    @PostMapping("admin/factories/add")
    public String add(@RequestParam String name, @RequestParam String country) throws Exception {
        Factory factory = new Factory(new FactoryRowGatewayImpl(dsFactory));
        factory.setName(name);

        if (country != null && !"null".equals(country)) {
            factory.setCountry(new Country(countryFinder.load(Integer.parseInt(country))));
        }

        factory.getGateway().save();

        return "redirect:/admin/factories";
    }

    @GetMapping("admin/factories/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Factory factory = new Factory(factoryFinder.load(id));
        List<Country> countries = countryFinder.list().stream().map(Country::new).collect(Collectors.toList());

        model.addAttribute("factory", factory);
        model.addAttribute("countries", countries);

        return "admin/factories/edit";
    }

    @PostMapping("admin/factories/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam String country) throws Exception {
        Factory factory = new Factory(factoryFinder.load(id));
        factory.setId(id);
        factory.setName(name);

        if (country != null && !"null".equals(country)) {
            factory.setCountry(new Country(countryFinder.load(Integer.parseInt(country))));
        } else {
            factory.setCountry(null);
        }

        factory.getGateway().update();

        return "redirect:/admin/factories";
    }

    @GetMapping("admin/factories/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        Factory factory = new Factory(factoryFinder.load(id));
        factory.getGateway().delete();

        return "redirect:/admin/factories";
    }
}

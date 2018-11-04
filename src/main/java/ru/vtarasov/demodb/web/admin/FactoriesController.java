package ru.vtarasov.demodb.web.admin;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.CountryMapper;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.FactoryMapper;
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
    private CountryMapper countryMapper;

    @Autowired
    private FactoryMapper factoryMapper;

    @GetMapping("admin/factories")
    public String index(ModelMap model) throws Exception {
        List<Factory> factories = factoryMapper.list();
        model.addAttribute("factories", factories);
        return "admin/factories/index";
    }

    @GetMapping("admin/factories/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Country> countries = countryMapper.list();
        model.addAttribute("countries", countries);

        return "admin/factories/add";
    }

    @PostMapping("admin/factories/add")
    public String add(@RequestParam String name, @RequestParam String country) throws Exception {
        DataSource ds = dsFactory.get();

        Factory factory = new Factory();
        factory.setName(name);

        if (country != null && !"null".equals(country)) {
            factory.setCountry(countryMapper.load(Integer.parseInt(country)));
        }

        factoryMapper.save(factory);

        return "redirect:/admin/factories";
    }

    @GetMapping("admin/factories/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Factory factory = factoryMapper.load(id);
        List<Country> countries = countryMapper.list();

        model.addAttribute("factory", factory);
        model.addAttribute("countries", countries);

        return "admin/factories/edit";
    }

    @PostMapping("admin/factories/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam String country) throws Exception {
        DataSource ds = dsFactory.get();

        Factory factory = factoryMapper.load(id);
        factory.setName(name);

        if (country == null || "null".equals(country)) {
            factory.setCountry(null);
        } else {
            factory.setCountry(countryMapper.load(Integer.parseInt(country)));
        }

        factoryMapper.update(factory);

        return "redirect:/admin/factories";
    }

    @GetMapping("admin/factories/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        DataSource ds = dsFactory.get();

        Factory factory = factoryMapper.load(id);
        factoryMapper.delete(factory);

        return "redirect:/admin/factories";
    }
}

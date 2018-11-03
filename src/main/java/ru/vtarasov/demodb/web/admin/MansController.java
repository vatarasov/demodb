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
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 03.11.18
 */
@Controller
public class MansController {

    @Autowired
    private DataSourceFactory dsFactory;

    @GetMapping("admin/mans")
    public String index(ModelMap model) throws SQLException {
        List<Man> mans = Man.list(dsFactory.get());
        model.addAttribute("mans", mans);
        return "admin/mans/index";
    }

    @GetMapping("admin/mans/add")
    public String getAdd(ModelMap model) throws SQLException {
        List<Country> countries = Country.list(dsFactory.get());
        model.addAttribute("countries", countries);

        return "admin/mans/add";
    }

    @PostMapping("admin/mans/add")
    public String add(@RequestParam String name, @RequestParam String country) throws SQLException {
        DataSource ds = dsFactory.get();

        Man man = new Man();
        man.setName(name);

        if (country != null && !"null".equals(country)) {
            man.setCountry(Country.load(ds, Integer.parseInt(country)));
        }

        man.save(ds);

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws SQLException  {
        Man man = Man.load(dsFactory.get(), id);
        List<Country> countries = Country.list(dsFactory.get());

        model.addAttribute("man", man);
        model.addAttribute("countries", countries);

        return "admin/mans/edit";
    }

    @PostMapping("admin/mans/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam String country) throws SQLException {
        DataSource ds = dsFactory.get();

        Man man = Man.load(ds, id);
        man.setName(name);

        if (country == null || "null".equals(country)) {
            man.setCountry(null);
        } else {
            man.setCountry(Country.load(ds, Integer.parseInt(country)));
        }

        man.update(ds);

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/delete")
    public String delete(@PathVariable int id) throws SQLException {
        DataSource ds = dsFactory.get();

        Man man = Man.load(ds, id);
        man.delete(ds);

        return "redirect:/admin/mans";
    }
}

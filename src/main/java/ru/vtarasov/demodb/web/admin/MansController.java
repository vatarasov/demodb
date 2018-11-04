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
import ru.vtarasov.demodb.datasource.CountryGateway;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.ManGateway;
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
    private CountryGateway countryGateway;

    @Autowired
    private ManGateway manGateway;

    @GetMapping("admin/mans")
    public String index(ModelMap model) throws Exception {
        List<Man> mans = manGateway.list().stream().map(Man::new).collect(Collectors.toList());
        model.addAttribute("mans", mans);
        return "admin/mans/index";
    }

    @GetMapping("admin/mans/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Country> countries = countryGateway.list().stream().map(Country::new).collect(Collectors.toList());
        model.addAttribute("countries", countries);

        return "admin/mans/add";
    }

    @PostMapping("admin/mans/add")
    public String add(@RequestParam String name, @RequestParam String country) throws Exception {
        Integer countryId = null;
        if (country != null && !"null".equals(country)) {
            countryId = Integer.parseInt(country);
        }

        manGateway.save(name, countryId);

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Man man = new Man(manGateway.load(id));
        List<Country> countries = countryGateway.list().stream().map(Country::new).collect(Collectors.toList());

        model.addAttribute("man", man);
        model.addAttribute("countries", countries);

        return "admin/mans/edit";
    }

    @PostMapping("admin/mans/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam String country) throws Exception {
        Integer countryId = null;
        if (country != null && !"null".equals(country)) {
            countryId = Integer.parseInt(country);
        }

        manGateway.update(id, name, countryId);

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        manGateway.delete(id);

        return "redirect:/admin/mans";
    }
}

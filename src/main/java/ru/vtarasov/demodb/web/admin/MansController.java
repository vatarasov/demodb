package ru.vtarasov.demodb.web.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtarasov.demodb.datasource.CountryMapper;
import ru.vtarasov.demodb.datasource.DataSourceFactory;
import ru.vtarasov.demodb.datasource.ManMapper;
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
    private CountryMapper countryMapper;

    @Autowired
    private ManMapper manMapper;

    @GetMapping("admin/mans")
    public String index(ModelMap model) throws Exception {
        List<Man> mans = manMapper.list();
        model.addAttribute("mans", mans);
        return "admin/mans/index";
    }

    @GetMapping("admin/mans/add")
    public String getAdd(ModelMap model) throws Exception {
        List<Country> countries = countryMapper.list();
        model.addAttribute("countries", countries);

        return "admin/mans/add";
    }

    @PostMapping("admin/mans/add")
    public String add(@RequestParam String name, @RequestParam String country) throws Exception {
        Man man = new Man();
        man.setName(name);

        if (country != null && !"null".equals(country)) {
            man.setCountry(countryMapper.load(Integer.parseInt(country)));
        }

        manMapper.save(man);

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/edit")
    public String getEdit(ModelMap model, @PathVariable int id) throws Exception  {
        Man man = manMapper.load(id);
        List<Country> countries = countryMapper.list();

        model.addAttribute("man", man);
        model.addAttribute("countries", countries);

        return "admin/mans/edit";
    }

    @PostMapping("admin/mans/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String name, @RequestParam String country) throws Exception {
        Man man = manMapper.load(id);
        man.setName(name);

        if (country == null || "null".equals(country)) {
            man.setCountry(null);
        } else {
            man.setCountry(countryMapper.load(Integer.parseInt(country)));
        }

        manMapper.update(man);

        return "redirect:/admin/mans";
    }

    @GetMapping("admin/mans/{id}/delete")
    public String delete(@PathVariable int id) throws Exception {
        Man man = manMapper.load(id);
        manMapper.delete(man);

        return "redirect:/admin/mans";
    }
}

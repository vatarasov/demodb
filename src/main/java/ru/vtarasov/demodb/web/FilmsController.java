package ru.vtarasov.demodb.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author vtarasov
 * @since 02.11.18
 */
@Controller
public class FilmsController {

    @GetMapping("/films")
    public String list(@RequestParam(name = "str", required = false) String search, HttpServletRequest request, ModelMap model) {
        Map<String, String[]> params = request.getParameterMap();

        List<String> years = new ArrayList<>();
        for (String year : years()) {
            if (params.containsKey("year_" + year)) {
                years.add(year);
            }
        }

        List<String> genres = new ArrayList<>();
        for (String genre : genres()) {
            if (params.containsKey("genre_" + genre)) {
                genres.add(genre);
            }
        }

        model.addAttribute("search", search);
        model.addAttribute("years", years());
        model.addAttribute("genres", genres());
        /*model.addAttribute("years1", years);
        model.addAttribute("genres1", genres);*/

        return "films";
    }

    private String[] years() {
        return new String[] { "2001", "2002" };
    }

    private String[] genres() {
        return new String[] { "1", "2" };
    }
}

package ru.vtarasov.demodb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vtarasov.demodb.datasource.FilmFinder;
import ru.vtarasov.demodb.model.Film;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@RestController
public class ImageController {

    @Autowired
    private FilmFinder filmFinder;

    @GetMapping("film/{id}/image")
    @ResponseBody
    public byte[] image(@PathVariable int id) {
        Film film = filmFinder.load(id);
        return film.getImage();
    }
}

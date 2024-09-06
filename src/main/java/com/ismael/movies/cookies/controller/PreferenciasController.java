package com.ismael.movies.cookies.controller;

import com.ismael.movies.controller.MovieController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PreferenciasController {
        @Autowired
        MovieController movieController;
        @RequestMapping("/preferencias")
        public String preferencias(Model model){
            model.addAttribute("css", movieController.theme);
            return "preferencias";
        }
}

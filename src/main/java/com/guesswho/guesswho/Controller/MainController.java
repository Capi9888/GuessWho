package com.guesswho.guesswho.Controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.guesswho.guesswho.Model.Game;

@Controller
public class MainController {
    @Autowired
    private Game game;
    
    @GetMapping("/")
    public ModelAndView home(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
    
    @PostMapping("/playgame")
    public @ResponseBody void playGame() {
        game.createGame();
    }

    @PostMapping("/selectCharacter")
    public  @ResponseBody void selectCharacter(@RequestParam("srcCartaFrontal") String srcCartaFrontal) {
        System.out.println("Llego1");
        String nombre = srcCartaFrontal.replace("images/", "");
        nombre = nombre.replace(".jpg", "");
        game.setWantedGamePersonByName(nombre);
    }

    @PostMapping("/guess")
    public @ResponseBody String manejarPeticion(@RequestBody Map<String, String> datosFormulario) {
        String caracteristica1 = datosFormulario.get("opciones1");
        String separador = datosFormulario.get("opciones2");
        String caracteristica2String = datosFormulario.get("opciones3");

        List<Integer> idGuesseds = game.guessPerson(caracteristica1, separador, caracteristica2String);
        Collections.shuffle(idGuesseds);
        // crear una cadena de JavaScript para llamar al método "girarCarta" para cada elemento en la lista "idGuesseds"
        StringBuilder script = new StringBuilder();
        int tiempoEspera = 0;
        for (Integer idGuess : idGuesseds) {
        script.append("setTimeout(function() { girarCarta('carta-").append(idGuess).append("'); }, ").append(tiempoEspera).append(");\n");
        tiempoEspera += 500; // incrementar el tiempo de espera en 500ms por cada iteración
    }

            return script.toString();


    }
}

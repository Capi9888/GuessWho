package com.guesswho.guesswho.Controller;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.guesswho.guesswho.DAO.PersonDB;
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
        String nombre = srcCartaFrontal.replace("images/", "");
        nombre = nombre.replace(".jpg", "");
        game.setWantedGamePersonByName(nombre);
    }

    @PostMapping("/guess")
    public @ResponseBody List<Integer> adivinarAliado(@RequestBody Map<String, String> datosFormulario) {
        try{
            String caracteristica1 = datosFormulario.get("opciones1");
            String separador = datosFormulario.get("opciones2");
            String caracteristica2String = datosFormulario.get("opciones3");
            PersonDB gp = new PersonDB();
            List<Integer> idGuesseds = game.guessPerson(caracteristica1, separador, caracteristica2String,true,null,gp.getGameId(),1);
            Collections.shuffle(idGuesseds);
            return idGuesseds;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;

        }
    }


    @PostMapping("/guessEnemy")
    public @ResponseBody List<Integer> adivinarEnemigo() {
        try{
            List<Integer> idGuesseds = game.guessPersonEnemy(12);
            Collections.shuffle(idGuesseds);
            return idGuesseds;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;

        }
    }
}


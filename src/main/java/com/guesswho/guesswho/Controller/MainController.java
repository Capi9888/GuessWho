package com.guesswho.guesswho.Controller;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.guesswho.guesswho.Model.Question;

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
    public @ResponseBody Question adivinarAliado(@RequestBody Map<String, String> datosFormulario) {
        try{
            String caracteristica1 = datosFormulario.get("opciones1");
            String separador = datosFormulario.get("opciones2");
            String caracteristica2 = datosFormulario.get("opciones3");
            PersonDB gp = new PersonDB();
            Question q = new Question();
            q = game.guessPerson(caracteristica1, separador, caracteristica2,true,null,gp.getGameId(),1);
            List<Integer> idGuesseds = q.getIdPersons();
            Collections.shuffle(idGuesseds);
            q.setIdPersons(idGuesseds);
            return q;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;

        }
    }


    @PostMapping("/guessEnemy")
    public @ResponseBody Question adivinarEnemigo() {
        try{
            Question q = new Question();
            q = game.guessPersonEnemy(12);
            List<Integer> idGuesseds = q.getIdPersons();
            Collections.shuffle(idGuesseds);
            q.setIdPersons(idGuesseds.stream().map(x->x+24).collect(Collectors.toList()));
            return q;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;

        }
    }
}


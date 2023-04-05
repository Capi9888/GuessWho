package com.guesswho.guesswho.Model;
import java.util.List;
import org.springframework.stereotype.Component;

import com.guesswho.guesswho.DAO.PersonDB;

@Component
public class Game {



    public void createGame(){
        try 
        {            
            PersonDB GamePerson = new PersonDB();
            GamePerson.createGame();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void setWantedGamePersonByName(String name){
        try 
        {
            PersonDB GamePerson = new PersonDB();
            GamePerson.setWantedGamePersonByName(name);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public List<Integer> guessPerson(String caracteristica1,String separador,String caracteristica2, boolean guessed)
    {


        try 
        {
            PersonDB GamePerson = new PersonDB();
            return GamePerson.getGuessedPersons(guessPersonAux(caracteristica1), separador, guessPersonAux(caracteristica2),guessed); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
        
    }

    public String guessPersonAux(String idAttribute)
    {

            switch(Integer.parseInt(idAttribute))
            {
                case 1:
                    return "mustache=1";
                case 2:
                    return "hat=1";
                case 3:
                    return "cheeks=1";
                case 4:
                    return "glasses=1";
                case 5:
                    return "gender=0";
                case 6:
                    return "brown_eyes=1";
                case 7:
                    return "hair='RED'";
                case 8:
                    return "hair='BLOND'";
                case 9:
                    return "hair='BLACK'";
                case 10:
                    return "hair='WHITE'";
                case 11:
                    return "hair='BALD'";
            }   
        return null;
    }

    public String guessPersonEnemy(int numCaracteristicas)
    {
        double porcentajeActual = 0.;
        double porcentajeMaximo = 0.;
        String respuestasUnicas ="";
        String respuestasY = "";
        String respuestasO = "";
        for(Integer i = 1; i<=numCaracteristicas;i++)
        {   
            int n = guessPerson(i.toString(), "n", "1",false).size();
            porcentajeActual =  Math.round((( n * 100) /  (24*1.)) * 100.0) / 100.0;
            respuestasUnicas += "Tiene " + guessPersonAux(i.toString())+" -- " +n+"/24="+ porcentajeActual +"%\n" ;

            for(Integer j = i+1; j <= numCaracteristicas;j++)
            {
                n=guessPerson(i.toString(), "y", j.toString(),false).size();
                porcentajeActual =  Math.round((( n * 100) /  (24*1.)) * 100.0) / 100.0;
                respuestasY += "Tiene "+ guessPersonAux(i.toString()) + " y " + guessPersonAux(j.toString()) +" -- " +n+"/24="+ porcentajeActual +"%\n";
                n=guessPerson(i.toString(), "o", j.toString(),false).size();
                porcentajeActual =  Math.round((( n * 100) /  (24*1.)) * 100.0) / 100.0;
                respuestasO += "Tiene "+ guessPersonAux(i.toString()) + " o " + guessPersonAux(j.toString()) +" -- " +n+"/24="+ porcentajeActual +"%\n";
            }
        }

        return "Primera forma: \n" + respuestasUnicas + "\n------------------------------\n" + "Segunda forma: \n" + respuestasY + "\n------------------------------\n" + "Tercera forma: \n" + respuestasO + "\n------------------------------\n";
    }

    
}

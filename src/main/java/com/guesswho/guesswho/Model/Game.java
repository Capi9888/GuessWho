package com.guesswho.guesswho.Model;
import java.util.List;
import org.springframework.stereotype.Component;
import java.sql.Connection;
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

    public List<Integer> guessPerson(String caracteristica1,String separador,String caracteristica2, boolean guessed, Connection conAux, int gameID,int player)
    {


        try 
        {
            PersonDB GamePerson = new PersonDB();
            return GamePerson.getGuessedPersons(guessPersonAux(caracteristica1), separador, guessPersonAux(caracteristica2),guessed,conAux, gameID,player); 
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
                    return "big_nose=1";
                case 6:
                    return "gender=0";
                case 7:
                    return "brown_eyes=1";
                case 8:
                    return "hair='RED'";
                case 9:
                    return "hair='BLOND'";
                case 10:
                    return "hair='BLACK'";
                case 11:
                    return "hair='WHITE'";
                case 12:
                    return "hair='BALD'";
            }   
        return null;
    }

    public List<Integer> guessPersonEnemy(int numCaracteristicas)
    {
        try{
            PersonDB p = new PersonDB();
            return p.guessPersonEnemy(numCaracteristicas);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    
}

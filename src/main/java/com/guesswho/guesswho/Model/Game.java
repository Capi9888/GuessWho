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

    public Question guessPerson(String caracteristica1,String separador,String caracteristica2, boolean guessed, Connection conAux, int gameID,int player)
    {


        try 
        {
            PersonDB GamePerson = new PersonDB();
            return GamePerson.getGuessedPersons(caracteristica1, separador, caracteristica2,guessed,conAux, gameID,player); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
        
    }

    

    public Question guessPersonEnemy(int numCaracteristicas)
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

package com.guesswho.guesswho.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class Question {

    //Attributes

    private List<Integer> idPersons;
    private String question;
    private String answer;

    //Getter y setter
    public List<Integer> getIdPersons() {
        return idPersons;
    }
    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }
    public void setIdPersons(List<Integer> idPersons){
        this.idPersons = idPersons;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    //Constructuroes
    public Question()
    {
        this.idPersons = new ArrayList<>();
        this.question = "";
        this.answer = "";
    }

    public Question(List<Integer> idPersons, String question, String answer)
    {
        this.idPersons = idPersons;
        this.question = question;
        this.answer = answer;
    }

    public String questionBBDD(String idAttribute)
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
                    return "hair!='BALD'";
            }   
        return null;
    }

    public String questionString(String idAttribute1, String separador, String idAttribute2)
    {

        String res = "";
            switch(Integer.parseInt(idAttribute1))
            {
                case 1:
                    res += "¿Tiene bigote";
                    break;
                case 2:
                    res+= "¿Tiene sombrero";
                    break;
                case 3:
                    res+= "¿Tiene mejillas";
                    break;
                case 4:
                    res+= "¿Tiene gafas";
                    break;
                case 5:
                    res+= "¿Tiene la nariz grande";
                    break;
                case 6:
                    res+= "¿Es hombre";
                    break;
                case 7:
                    res+= "¿Tiene ojos marrones";
                    break;
                case 8:
                    res+= "¿Tiene el pelo rojo";
                    break;
                case 9:
                    res+= "¿Tiene el pelo rubio";
                    break;
                case 10:
                    res+= "¿Tiene el pelo negro";
                    break;
                case 11:
                    res+= "¿Tiene el pelo blanco";
                    break;
                case 12:
                    res+= "¿Tiene pelo";
                    break;
            }
            
            if(separador.equals("y") || separador.equals("o"))
            {
                switch(Integer.parseInt(idAttribute2))
                {
                    case 1:
                        res+= " " +separador + " tiene bigote?";
                        break;
                    case 2:
                        res+= " " +separador + " tiene sombrero?";
                        break;
                    case 3:
                        res+= " " +separador + " tiene mejillas?";
                        break;
                    case 4:
                        res+= " " +separador + " tiene gafas?";
                        break;
                    case 5:
                        res+= " " +separador + " tiene la nariz grande?";
                        break;
                    case 6:
                        res+= " " +separador + " es hombre?";
                        break;
                    case 7:
                        res+= " " +separador + " tiene ojos marrones?";
                        break;
                    case 8:
                        res+= " " +separador + " tiene el pelo rojo?";
                        break;
                    case 9:
                        res+= " " +separador + " tiene el pelo rubio?";
                        break;
                    case 10:
                        res+= " " +separador + " tiene el pelo negro?";
                        break;
                    case 11:
                        res+= " " +separador + " tiene el pelo blanco?";
                        break;
                    case 12:
                        res+= " " +separador + " tiene pelo?";
                        break;
                }
            }
            else
            {
                res+="?";
            }
        return res;
    }
    
}

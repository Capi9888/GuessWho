package com.guesswho.guesswho.Model;
import java.util.Random;
import javax.persistence.Entity;

@Entity
public class Person {
    //region attributes
    public enum Hair {
        BALD,
        BLOND,
        RED,
        BLACK,
        WHITE,
        NULL
    }
    private int id;
    private String name;
    private boolean hat;
    private boolean glasses;
    private boolean brownEyes;
    private boolean cheeks;
    private boolean mustache;
    private boolean gender;
    private Hair hair;
    

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isHat() {
        return hat;
    }
    public void setHat(boolean hat) {
        this.hat = hat;
    }
    public boolean isGlasses() {
        return glasses;
    }
    public void setGlasses(boolean glasses) {
        this.glasses = glasses;
    }
    public boolean isBrownEyes() {
        return brownEyes;
    }
    public void setBrownEyes(boolean brownEyes) {
        this.brownEyes = brownEyes;
    }
    public boolean isCheeks() {
        return cheeks;
    }
    public void setCheeks(boolean cheeks) {
        this.cheeks = cheeks;
    }
    public boolean isMustache() {
        return mustache;
    }
    public void setMustache(boolean mustache) {
        this.mustache = mustache;
    }
    public boolean isGender() {
        return gender;
    }
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    public Hair getHair() {
        return hair;
    }
    public void setHair(Hair hair) {
        this.hair = hair;
    }
    //region constructor
    public  Person(){
        this.name = "perro";
        this.hair = randomHair();
        this.glasses = randomBoolean();
        this.brownEyes = randomBoolean();
        this.cheeks = randomBoolean();
        this.hat = randomBoolean();
        this.mustache = randomBoolean();  
        this.gender = randomBoolean();   
    } 
    public Person(int id, String name,Hair hair, boolean hasHat, boolean hasGlasses, boolean hasBrownEyes, boolean hasCheeks, boolean hasMustache, boolean gender) 
    {
        this.id = id;
        this.name = name;
        this.hair = hair;
        this.hat = hasHat;
        this.glasses = hasGlasses;
        this.brownEyes = hasBrownEyes;
        this.cheeks = hasCheeks;
        this.mustache = hasMustache;
        this.gender = gender;
    }

    //region public methods
    public String toString()
    {
        return "ID: "+this.id +"\nNombre: "+this.name + "\nPelo: " + this.hair +"\nSombrero: "+this.hat +"\nGafas: " + this.glasses +"\nOjos: "
        + this.brownEyes +"\nMejillas: "+this.cheeks+"\nBigote: "+this.mustache+"\nGenero: "+this.gender;
    }
    //region private methods

    private boolean randomBoolean()
    {
       
        Random rd = new Random();      
        return rd.nextBoolean();
    }
    private static Hair randomHair()
    {

        Hair[] values = Hair.values();
        int length = values.length;
        int randIndex = new Random().nextInt(length);
        return values[randIndex];      
    }
}

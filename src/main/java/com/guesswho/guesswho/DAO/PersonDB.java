package com.guesswho.guesswho.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.guesswho.guesswho.Configuration.AppConfig;
import com.guesswho.guesswho.Model.Person;
import com.guesswho.guesswho.Model.Person.Hair;

@Repository
public class PersonDB {

    AppConfig app = new AppConfig();
    @Autowired
    private DataSource dataSource = app.dataSource();

    public List<Person> getPersonas() {
        try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Persons")) {
        List<Person> personas = new ArrayList<Person>();
        while (rs.next()) {
           int id = rs.getInt("id_person");
           String name = rs.getString("name_person");
           String hairString = rs.getString("hair");
           Hair hairColor = Hair.valueOf(hairString.toUpperCase());
           boolean hasHat = rs.getBoolean("hat");
           boolean hasGlasses = rs.getBoolean("glasses");
           boolean hasBrownEyes = rs.getBoolean("brown_eyes");
           boolean hasCheeks = rs.getBoolean("cheeks");
           boolean hasMustache = rs.getBoolean("mustache");
           boolean gender = rs.getBoolean("gender");
           
           Person person = new Person(id, name, hairColor, hasHat, hasGlasses, hasBrownEyes, hasCheeks, hasMustache, gender);
           personas.add(person);          
        }        
        return personas;

     } catch (SQLException e) {
        e.printStackTrace();
        return null;
     }
    }

    public void createGame() {
        try (Connection con = dataSource.getConnection()) {

            //Se crea la partida en la tabla Games
            String sql = "INSERT INTO Games (id_game) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            int nextGameId = getNextGameId();
            pstmt.setInt(1, nextGameId);
            pstmt.executeUpdate();

            //Se crea la relación personajes / game
            sql = "INSERT INTO GamePerson (id_game,id_person,player,guessed,wanted) VALUES (?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            for(int i = 1; i <=24; i++){                
                pstmt.setInt(1, nextGameId);
                pstmt.setInt(2, i);
                pstmt.setInt(3, 1);
                pstmt.setInt(4,0);
                pstmt.setInt(5,0);
                pstmt.executeUpdate();

                pstmt.setInt(1, nextGameId);
                pstmt.setInt(2, i);
                pstmt.setInt(3, 2);
                pstmt.setInt(4,0);
                pstmt.setInt(5,0);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNextGameId() throws SQLException {
        int nextId = 1;
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT MAX(id_game) AS max_game_id FROM Games";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                nextId = rs.getInt("max_game_id") + 1;
            }
        }
        return nextId;
    }

    private int getGameId() throws SQLException {
        int nextId = 1;
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT MAX(id_game) AS max_game_id FROM Games";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                nextId = rs.getInt("max_game_id");
            }
        }
        return nextId;
    }

    private int getIDPersonByName(String nombre) throws SQLException {
        int id = -1;
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT id_person FROM Persons WHERE name_person = '" +nombre+ "'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                id = rs.getInt("id_person");
            }
        }
        return id;
    }

    public void setWantedGamePersonByName(String nombrePersona) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            String sql = "UPDATE GamePerson SET wanted=1 WHERE id_game="+getGameId()+" AND id_person="+getIDPersonByName(nombrePersona)+" AND player="+1;
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
        }       
    }
    


    public List<Integer> getGuessedPersons(String caracteristica1,String separador,String caracteristica2,boolean guessed) throws SQLException {
        int id = -1;
        try (Connection con = dataSource.getConnection()) {
            String sql = "";
            if(separador.equals("y")){
                sql = "select gp.id_person,gp.guessed,gp.wanted from GamePerson gp LEFT JOIN Persons p on gp.id_person = p.id_person WHERE id_game = "+getGameId()+" AND player = 1 AND ("+caracteristica1+" AND "+caracteristica2+")";
            }else if(separador.equals("o")){
                sql = "select gp.id_person,gp.guessed,gp.wanted from GamePerson gp LEFT JOIN Persons p on gp.id_person = p.id_person WHERE id_game = "+getGameId()+" AND player = 1 AND ("+caracteristica1+" OR "+caracteristica2+")";
            }else{
                sql = "select gp.id_person,gp.guessed,gp.wanted from GamePerson gp LEFT JOIN Persons p on gp.id_person = p.id_person WHERE id_game = "+getGameId()+" AND player = 1 AND ("+caracteristica1+")";
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List<Integer> res = new ArrayList<>();
            Boolean haveWanted = false;
            while (rs.next()) {
                if(rs.getInt("guessed")==0){
                    id = rs.getInt("id_person");
                    res.add(id);
                    if(!haveWanted){
                       haveWanted = rs.getInt("wanted")==1?true:false;
                    }
                }
            }
            if(guessed){
                if(haveWanted){
                    res = getGuessedPersonsReverso(res);
                    res.removeAll(getAllGuessedPersons(getGameId(), 1));     
                }
                if(res.size() != 0){
                    setGuessedGamePerson(sql,haveWanted);
                }
            }            
            return res;
        }
    }

    private void setGuessedGamePerson(String sql,Boolean haveWanted) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            String sqlIdPerson;
            sqlIdPerson = sql.replace("gp.id_person,gp.guessed,gp.wanted", "gp.id_person");
            if(haveWanted){
                sqlIdPerson = sqlIdPerson.replace("AND (","AND NOT(");
            }         
            String sqlUpdate = "UPDATE GamePerson SET guessed=1 WHERE id_game="+getGameId()+" AND id_person in("+sqlIdPerson+") AND player=1";
            PreparedStatement pstmt = con.prepareStatement(sqlUpdate);
            pstmt.executeUpdate();
        }       
    }

    private List<Integer> getGuessedPersonsReverso(List<Integer> res){
        List<Integer> resInverso = new ArrayList<>();
        for(int i = 1; i<=24;i++){
            if(!res.contains(i)){
                resInverso.add(i);
            }
        }

        return resInverso;
    }

    private List<Integer> getAllGuessedPersons(int idGame,int player) throws SQLException{
        List<Integer> res = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT id_person FROM GamePerson WHERE guessed = 1 AND player = "+player+" and id_game = "+idGame;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                res.add(rs.getInt("id_person"));
            }
            return res;
        }
    }

    public List<Integer> getGuessed(String hair, Integer hat, Integer glasses, Integer brownEyes,
    Integer cheeks, Integer mustache, Integer gender, Integer gameId, Integer playerId) {
    try (Connection con = dataSource.getConnection();
         CallableStatement stmt = con.prepareCall("{CALL sp_GetGuessed(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            if (hair!=null) {
                stmt.setString(1, hair);
            } else {
                stmt.setNull(1, Types.VARCHAR);
            }
            
            if (hat != null) {
                stmt.setInt(2, hat);
            } else {
                stmt.setNull(2, Types.BIT);
            }
            
            if (glasses != null) {
                stmt.setInt(3, glasses);
            } else {
                stmt.setNull(3, Types.BIT);
            }
            
            if (brownEyes != null) {
                stmt.setInt(4, brownEyes);
            } else {
                stmt.setNull(4, Types.BIT);
            }
            
            if (cheeks != null) {
                stmt.setInt(5, cheeks);
            } else {
                stmt.setNull(5, Types.BIT);
            }
            
            if (mustache != null) {
                stmt.setInt(6, mustache);
            } else {
                stmt.setNull(6, Types.BIT);
            }
            
            if (gender != null) {
                stmt.setInt(7, gender);
            } else {
                stmt.setNull(7, Types.BIT);
            }
            
            if (gameId != null) {
                stmt.setInt(8, gameId);
            } else {
                stmt.setNull(8, Types.BIT);
            }
            
            if (playerId != null) {
                stmt.setInt(9, playerId);
            } else {
                stmt.setNull(9, Types.BIT);
            }
        

            List<Integer> guessedPersons = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                boolean wanted = false;
                while (rs.next()) {
                int id_person = rs.getInt("id_person");
                if(!wanted){
                   wanted = rs.getInt("wanted")==1?true:false;
                }
                if( rs.getInt("guessed")==0){
                    guessedPersons.add(id_person);
                }
                }
            }
            return guessedPersons;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
    
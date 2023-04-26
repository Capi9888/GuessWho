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
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.guesswho.guesswho.Configuration.AppConfig;
import com.guesswho.guesswho.Model.Person;
import com.guesswho.guesswho.Model.Question;
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

                Person person = new Person(id, name, hairColor, hasHat, hasGlasses, hasBrownEyes, hasCheeks,
                        hasMustache, gender);
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

            // Se crea la partida en la tabla Games
            String sql = "INSERT INTO Games (id_game) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            int nextGameId = getNextGameId();
            pstmt.setInt(1, nextGameId);
            pstmt.executeUpdate();

            // Se crea la relación personajes / game
            sql = "INSERT INTO GamePerson (id_game,id_person,player,guessed,wanted) VALUES (?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            Random random = new Random();
            int wantedEnemy = random.nextInt(24) + 1;
            for (int i = 1; i <= 24; i++) {
                pstmt.setInt(1, nextGameId);
                pstmt.setInt(2, i);
                pstmt.setInt(3, 1);
                pstmt.setInt(4, 0);
                pstmt.setInt(5, i==wantedEnemy?1:0);
                pstmt.executeUpdate();

                pstmt.setInt(1, nextGameId);
                pstmt.setInt(2, i);
                pstmt.setInt(3, 2);
                pstmt.setInt(4, 0);
                pstmt.setInt(5, 0);
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

    public int getGameId() throws SQLException {
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
            String sql = "SELECT id_person FROM Persons WHERE name_person = '" + nombre + "'";
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
            String sql = "UPDATE GamePerson SET wanted=1 " +
                         "WHERE id_game=? AND id_person=? AND player=2";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, getGameId());
            pstmt.setInt(2, getIDPersonByName(nombrePersona));
            pstmt.executeUpdate();
        }
    }

    public Question getGuessedPersons(String caracteristica1, String separador, String caracteristica2,
            boolean guessed, Connection conAux, int gameID, int player) throws SQLException {
        int id = -1;
        Question q = new Question();
        boolean haveConnection = (conAux != null);
        Connection con = haveConnection ? conAux : dataSource.getConnection();
        try (Statement stmt = con.createStatement()) {
            String sql = "";
            if (separador.equals("y")) {
                sql = "select gp.id_person,gp.guessed,gp.wanted from GamePerson gp LEFT JOIN Persons p on gp.id_person = p.id_person WHERE id_game = "
                        + gameID + " AND player = " + player + " AND (" + q.questionBBDD(caracteristica1) + " AND " + q.questionBBDD(caracteristica2)
                        + ")";
            } else if (separador.equals("o")) {
                sql = "select gp.id_person,gp.guessed,gp.wanted from GamePerson gp LEFT JOIN Persons p on gp.id_person = p.id_person WHERE id_game = "
                        + gameID + " AND player = " + player + " AND (" + q.questionBBDD(caracteristica1) + " OR " + q.questionBBDD(caracteristica2)
                        + ")";
            } else {
                sql = "select gp.id_person,gp.guessed,gp.wanted from GamePerson gp LEFT JOIN Persons p on gp.id_person = p.id_person WHERE id_game = "
                        + gameID + " AND player = " + player + " AND (" + q.questionBBDD(caracteristica1) + ")";
            }

            ResultSet rs = stmt.executeQuery(sql);
            List<Integer> res = new ArrayList<>();
            Boolean haveWanted = false;
            while (rs.next()) {
                if (rs.getInt("guessed") == 0) {
                    id = rs.getInt("id_person");
                    res.add(id);
                    if (!haveWanted) {
                        haveWanted = rs.getInt("wanted") == 1 ? true : false;
                    }
                }
            }
            if (guessed) {
                if (haveWanted) {
                    res = getGuessedPersonsReverso(res);
                    res.removeAll(getAllGuessedPersons(gameID, player));
                }
                if (res.size() != 0) {
                    setGuessedGamePerson(sql, haveWanted, player);
                }
            }

            q.setIdPersons(res);
            q.setAnswer(haveWanted?"Sí":"No");
            q.setQuestion(q.questionString(caracteristica1, separador, caracteristica2));

            return q;
        } finally {
            if (!haveConnection) {
                if(con != null){
                    con.close();
                }
            }
        }
    }

    private void setGuessedGamePerson(String sql, Boolean haveWanted, int player) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            String sqlIdPerson;
            sqlIdPerson = sql.replace("gp.id_person,gp.guessed,gp.wanted", "gp.id_person");
            if (haveWanted) {
                sqlIdPerson = sqlIdPerson.replace("AND (", "AND NOT(");
            }
            String sqlUpdate = "UPDATE GamePerson SET guessed=1 WHERE id_game=" + getGameId() + " AND id_person in("
                    + sqlIdPerson + ") AND player=" + player;
            PreparedStatement pstmt = con.prepareStatement(sqlUpdate);
            pstmt.executeUpdate();
        }
    }

    private List<Integer> getGuessedPersonsReverso(List<Integer> res) {
        List<Integer> resInverso = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            if (!res.contains(i)) {
                resInverso.add(i);
            }
        }

        return resInverso;
    }

    private List<Integer> getAllGuessedPersons(int idGame, int player) throws SQLException {
        List<Integer> res = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT id_person FROM GamePerson WHERE guessed = 1 AND player = " + player + " and id_game = "
                    + idGame;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                res.add(rs.getInt("id_person"));
            }
            return res;
        }
    }

    public Question guessPersonEnemy(int numCaracteristicas) throws SQLException {
        double porcentajeActual = 0.;
        double porcentajeMaximo = 0.;

        String acum1 = "";
        String acum2 = "";
        String acum3 = "";

        int gameID = getGameId();

        int personasSinAdivinar = 24 - getAllGuessedPersons(gameID, 2).size();

        try (Connection con = dataSource.getConnection()) {
            for (Integer i = 1; i <= numCaracteristicas; i++) {
                String caracteristica1 =i.toString();
                int n = getGuessedPersons(caracteristica1, "n", "1", false, con, gameID, 2).getIdPersons().size();
                porcentajeActual = Math.round(((n * 100) / (personasSinAdivinar * 1.)) * 100.0) / 100.0;
                if (porcentajeActual == 50.) {
                    return getGuessedPersons(caracteristica1, "n", "1", true, con, gameID, 2);
                } else if (porcentajeMaximo == 0.
                        || (Math.abs(porcentajeActual - 50) < Math.abs(porcentajeMaximo - 50))) {
                    porcentajeMaximo = porcentajeActual;
                    acum1 = caracteristica1;
                    acum2 = "n";
                    acum3 = caracteristica1;
                }

                for (Integer j = i + 1; j <= numCaracteristicas; j++) {
                    String caracteristica2 = j.toString();
                    n = getGuessedPersons(caracteristica1, "o", caracteristica2, false, con, gameID, 2).getIdPersons().size();
                    porcentajeActual = Math.round(((n * 100) / (personasSinAdivinar * 1.)) * 100.0) / 100.0;
                    if (porcentajeActual == 50.) {
                        return getGuessedPersons(caracteristica1, "o", caracteristica2, true, con, gameID, 2);
                    } else if (Math.abs(porcentajeActual - 50) < Math.abs(porcentajeMaximo - 50)) {
                        porcentajeMaximo = porcentajeActual;
                        acum1 = caracteristica1;
                        acum2 = "o";
                        acum3 = caracteristica2;
                    }

                    n = getGuessedPersons(caracteristica1, "y", caracteristica2, false, con, gameID, 2).getIdPersons().size();
                    porcentajeActual = Math.round(((n * 100) / (personasSinAdivinar * 1.)) * 100.0) / 100.0;
                    if (porcentajeActual == 50.) {
                        return getGuessedPersons(caracteristica1, "y", caracteristica2, true, con, gameID, 2);
                    } else if (Math.abs(porcentajeActual - 50) < Math.abs(porcentajeMaximo - 50)) {
                        porcentajeMaximo = porcentajeActual;
                        acum1 = caracteristica1;
                        acum2 = "y";
                        acum3 = caracteristica2;
                    }
                }
            }
            return getGuessedPersons(acum1, acum2, acum3, true, con, gameID, 2);
        }

    }
}

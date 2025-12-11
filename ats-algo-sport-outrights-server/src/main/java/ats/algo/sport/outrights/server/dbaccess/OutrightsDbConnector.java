package ats.algo.sport.outrights.server.dbaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.crypto.IllegalBlockSizeException;

import ats.algo.sport.outrights.calcengine.core.ATSFixture;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;

public class OutrightsDbConnector {

    static Connection connection;

    /**
     * establishes a connection to the database specified by the url
     * 
     * @param url
     * @return null if connection successfully established, else a String giving the reason why the connection attempt
     *         failed
     */
    public String establishConnection(String url) {
        connection = null;
        return "Not yet implemented";
    }

    public String establishConnection(String url, String db, String password) throws SQLException {
        connection = getConnection(url, db, password);
        return "Not yet implemented";
    }

    public Connection getConnection(String url, String userName, String password) throws SQLException {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);

        conn = DriverManager.getConnection(url, connectionProps);
        System.out.println("Connected to database");
        return conn;
    }

    /**
     * uses the previously established connection to query the d/b to get the list of fixtures that ATS know about
     * related to this atsCompetitionID
     * 
     * @return Map of objects of type ATSFixture. Each gives the home team, away team, eventID and fixture date. Keys to
     *         the map are the eventIds
     * @throws IllegalBlockSizeException
     */
    public List<ATSFixture> getATSFixtures(String atsCompetitionID, Teams teams) throws SQLException {
        Statement stmt = connection.createStatement();
        ArrayList<ATSFixture> list = new ArrayList<ATSFixture>();
        // ResultSet resultSet = connection.getMetaData().getCatalogs();
        ResultSet resultSet = stmt.executeQuery(
                        "SELECT id,name,type,state,event_time,npath,getnodepath(npath) from nodes where node_id = '"
                                        + atsCompetitionID + "';");// and 5300324
        // event_time
        // >
        // now()
        // ResultSet resultSet = stmt.executeQuery("setenv 1;");

        int columnsNumber = resultSet.getMetaData().getColumnCount();
        String date = "";
        String home = "";
        String away = "";
        long eventId = 0;
        while (resultSet.next()) {
            // Get the database name, which is at position 1
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + resultSet.getMetaData().getColumnName(i));
            }
            System.out.println("");

            eventId = Long.parseLong(resultSet.getString(1));
            String[] match = resultSet.getString(2).split(" vs ");
            if (match.length != 2) {
                System.out.println("Found Content is with wrong size!");
                System.out.println(resultSet.getString(2));
            } else {
                for (java.util.Map.Entry<String, Team> team : teams.getTeams().entrySet()) {
                    if (team.getValue().getLsportsName().equals(match[0]))
                        home = team.getKey();
                    if (team.getValue().getLsportsName().equals(match[1]))
                        away = team.getKey();
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString(5).substring(0, 19), formatter);
                date = dateTime.toString();
                System.out.println(date + home + away + eventId);
                ATSFixture atsFixture = new ATSFixture(date, home, away, eventId);
                list.add(atsFixture);
            }
        }
        resultSet.close();

        return list;

    }

    public void closeConnection() throws SQLException {
        System.out.println("Connection closing");
        connection.close();
    }
}

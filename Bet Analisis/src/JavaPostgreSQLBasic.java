import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Basic connection to PostgreSQL database. 
 * Conexión básica a la base de datos PostgreSQL.
 *
 * @author Xules You can follow me on my website http://www.codigoxules.org/en
 * Puedes seguirme en mi web http://www.codigoxules.org).
 */
public class JavaPostgreSQLBasic {

    /**
     * We establish the connection with the database <b>customerdb</b>.
     * Establecemos la conexión con la base de datos <b>customerdb</b>.
     */
    public void connectDatabase() {
        try {
            // We register the PostgreSQL driver
            // Registramos el driver de PostgresSQL
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            Connection connection = null;
            // Database connect
            // Conectamos con la base de datos
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.1.63:5432/bet_analisis",
                    "postgres", "hacker21");

            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error: " + sqle);
        }
    }
    public void connectDatabase(String host, String port, String database,
                                String user, String password) {
        String url = "";
        try {
            // We register the PostgreSQL driver
            // Registramos el driver de PostgresSQL
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            Connection connection = null;
            url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            // Database connect
            // Conectamos con la base de datos
            connection = DriverManager.getConnection(
                    url,
                    user, password);
            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error al conectar con la base de datos de PostgreSQL (" + url + "): " + sqle);
        }
    }
    public ArrayList<Bet> selectMatch(ArrayList<Bet> matches){
        ArrayList<Bet> bets=new ArrayList<Bet>();
        for (Bet aux : matches) {
            if((aux.getProbSingle()>0.55)||(aux.getProbCombi()>0.79)){
                bets.add(aux);
            }
        }
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            Connection connection = null;
            // Database connect
            // Conectamos con la base de datos
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.1.63:5432/bet_analisis",
                    "postgres", "hacker21");
            String bet="";
            for (Bet aux1:bets) {
                if ((aux1.getProbSingle()>0.55) && (aux1.getOddSingle()>1.15)){
                    bet=aux1.getSingle();
                } else {
                    bet=aux1.getCombi();
                }
                PreparedStatement stmnt = connection.prepareStatement("INSERT INTO public.bets(match, bet) VALUES ('"+ aux1.getEquipos()+"', '" +bet+"');");
                stmnt.executeUpdate();
                System.out.println(aux1.toString());
            }
            connection.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        return bets;

    }
    public void insertarPartidos(){

        String home="";
        String away="";
        int goalHome=0;
        int goalAway=0;
        int draw=0;
        int winHome=0;
        int winAway=0;
        int jornada=0;
        Scanner sc =new Scanner(System.in);
        System.out.println("Equipo local: ");
        home=sc.nextLine();
        System.out.println("Equipo Away: ");
        away=sc.nextLine();

        System.out.println("Goles local: ");
        goalHome=sc.nextInt();
        System.out.println("Goles away: ");
        goalAway=sc.nextInt();
        System.out.println("Jornada: ");
        jornada=sc.nextInt();
        if (goalHome==goalAway)
        {draw=1;}
        else if (goalHome>goalAway){
            winHome=1;
        }else{
            winAway=1;
        }

        System.out.println(home+" "+away+" "+goalHome+" "+goalAway+" "+jornada);
        try {
            // We register the PostgreSQL driver
            // Registramos el driver de PostgresSQL
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            Connection connection = null;
            // Database connect
            // Conectamos con la base de datos
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.1.63:5432/bet_analisis",
                    "postgres", "hacker21");

            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");

            PreparedStatement stmnt = connection.prepareStatement("INSERT INTO public.matches(home_team, goal_home, goal_away, away_team, win_home, draw_match, win_away,jornada) VALUES('" + home + "', " + goalHome + ", " + goalAway + ", '" + away + "',"  + winHome + ","  + draw + ", " + winAway + ","+jornada+")");
            stmnt.executeUpdate();
            stmnt=connection.prepareStatement("UPDATE public.results\n" +
                    "\tSET  wins=wins+"+winHome+", draws=draws+"+draw+", loses=loses+"+winAway+", total=total+1, a_favor=a_favor+"+goalHome+", en_contra=en_contra+"+goalAway+"\n" +
                    "\tWHERE team='"+home+"'");
            stmnt.executeUpdate();
            stmnt=connection.prepareStatement("UPDATE public.results\n" +
                    "\tSET  wins=wins+"+winAway+", draws=draws+"+draw+", loses=loses+"+winHome+", total=total+1, a_favor=a_favor+"+goalAway+", en_contra=en_contra+"+goalHome+"\n" +
                    "\tWHERE team='"+away+"'");
            stmnt.executeUpdate();
            connection.close();

        } catch (java.sql.SQLException sqle) {
            System.out.println("Error: " + sqle);
        }
    }
    public Bet consultaBet(){
        Bet partido;
        String local="";
        String away="";
        double probLocal1=0.0;
        double probEmpate1=0.0;
        double probVisitante1=0.0;
        double probLocal2=0.0;
        double probEmpate2=0.0;
        double probVisitante2=0.0;
        double probLocalT=0.0;
        double probEmpateT=0.0;
        double probVisitanteT=0.0;
        int resBuscadoW=0;
        int resBuscadoL=0;
        int resBuscadoD=0;
        double oddLocal=0;
        double oddDraw=0;
        double oddAway=0;
        ArrayList<Double> Odds1Ganar=new ArrayList<Double>();
        ArrayList<Double> odds1Draw=new ArrayList<Double>();
        ArrayList<Double> odds1Lose=new ArrayList<Double>();
        ArrayList<Double> odds2Ganar=new ArrayList<Double>();
        ArrayList<Double> odds2Draw=new ArrayList<Double>();
        ArrayList<Double> odds2Perder=new ArrayList<Double>();
        ArrayList<Double> odds1AFavor=new ArrayList<Double>();
        ArrayList<Double> odds1Contra=new ArrayList<Double>();
        ArrayList<Double> odds2Favor=new ArrayList<Double>();
        ArrayList<Double> odds2Contra=new ArrayList<Double>();
        double probG;
        double probD;
        double probL;
        double probAF;
        double probC;
        int victorias=0;
        int derrotas=0;
        int empates=0;
        int a_favor=0;
        int en_contra=0;
        Scanner sc =new Scanner(System.in);

        System.out.println("Equipo local: ");
        local=sc.nextLine();
        System.out.println("Equipo Away: ");
        away=sc.nextLine();
        System.out.println("Odd local: ");
        oddLocal=sc.nextDouble();
        System.out.println("Odd draw: ");
        oddDraw=sc.nextDouble();
        System.out.println("Odd away: ");
        oddAway=sc.nextDouble();


        try {
            // We register the PostgreSQL driver
            // Registramos el driver de PostgresSQL
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            Connection connection = null;
            // Database connect
            // Conectamos con la base de datos
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.1.63:5432/bet_analisis",
                    "postgres", "hacker21");
            Statement stmt = connection.createStatement();
            /*
                ESTADISTICAS  PARTIDOS  EQUIPO EN CASA
            */
            ResultSet result = stmt.executeQuery("SELECT Sum(goal_home) as Goles_A_Favor,SUM(goal_away)as Goles_En_Contra,SUM(win_home) as Victorias, SUM(draw_match) as Empates, SUM(win_away) as Derrota,(SUM(win_home)+SUM(draw_match)+SUM(win_away))as total\n" +
                    "FROM public.matches where home_team='"+local+"';"); // O SELECT * FROM usuarios;
            while(result.next()) {
                 probG=(double) result.getInt("Victorias")/result.getInt("total");
                 probD=(double)result.getInt("Empates")/result.getInt("total");
                 probL=(double)result.getInt("Derrota")/result.getInt("total");
                 probAF=(double)result.getInt("Goles_A_Favor")/result.getInt("total");
                 probC=(double)result.getInt("Goles_En_Contra")/result.getInt("total");
                 Odds1Ganar.add(probG);
                 odds1Draw.add(probD);
                 odds1Lose.add(probL);
                 odds1AFavor.add(probAF);
                 odds1Contra.add(probC);
                 //System.out.println(probG+" "+probD+" "+probL+" "+probAF+" "+probC);
            }
             result.close();
            /*
                ESTADISTICAS   PARTIDOS  EQUIPO EN FUERA
            */
            result = stmt.executeQuery("SELECT Sum(goal_away) as Goles_A_Favor,SUM(goal_home)as Goles_En_Contra,SUM(win_away) as Victorias, SUM(draw_match) as Empates, SUM(win_home) as Derrota,(SUM(win_home)+SUM(draw_match)+SUM(win_away))as total\n" +
                    "FROM public.matches where away_team='"+away+"';");
            while(result.next()) {
                probG = (double) result.getInt("Victorias") / result.getInt("total");
                probD = (double) result.getInt("Empates") / result.getInt("total");
                probL = (double) result.getInt("Derrota") / result.getInt("total");
                probAF = (double) result.getInt("Goles_A_Favor") / result.getInt("total");
                probC = (double) result.getInt("Goles_En_Contra") / result.getInt("total");
                odds2Ganar.add(probG);
                odds2Draw.add(probD);
                odds2Perder.add(probL);
                odds2Favor.add(probAF);
                odds2Contra.add(probC);
            }
             /*
                ESTADISTICAS  ULTIMOS 5 PARTIDOS  EQUIPO EN CASA
            */
            result = stmt.executeQuery("Select *" +
                    " from public.matches where home_team='"+local+"'order by jornada DESC Limit 5");
            while (result.next()){
                victorias=victorias+result.getInt("win_home");
                empates=empates+result.getInt("draw_match");
                derrotas=derrotas+result.getInt("win_away");
                a_favor=a_favor+result.getInt("goal_home");
                en_contra=en_contra+result.getInt("goal_away");

            }
            Odds1Ganar.add((double)victorias/(victorias+empates+derrotas));
            odds1Draw.add((double)empates/(victorias+empates+derrotas));
            odds1Lose.add((double)derrotas/(victorias+empates+derrotas));
            odds1AFavor.add((double)a_favor/(victorias+empates+derrotas));
            odds1Contra.add((double)en_contra/(victorias+empates+derrotas));
            result.close();
            victorias=0;
            derrotas=0;
           empates=0;
             a_favor=0;
             en_contra=0;
              /*
                ESTADISTICAS  ULTIMOS 5 PARTIDOS COMO VISITANTE EQUIPO VISITANTE


            */
            result = stmt.executeQuery("Select *" +
                    " from public.matches where away_team='"+away+"'order by jornada DESC Limit 5");
            while (result.next()){
                victorias=victorias+result.getInt("win_away");
                empates=empates+result.getInt("draw_match");
                derrotas=derrotas+result.getInt("win_home");
                a_favor=a_favor+result.getInt("goal_away");
                en_contra=en_contra+result.getInt("goal_home");
            }
            odds2Ganar.add((double)victorias/(victorias+empates+derrotas));
            odds2Draw.add((double)empates/(victorias+empates+derrotas));
            odds2Perder.add((double)derrotas/(victorias+empates+derrotas));
            odds2Favor.add((double)a_favor/(victorias+empates+derrotas));
            odds2Contra.add((double)en_contra/(victorias+empates+derrotas));
            result.close();
            victorias=0;
            derrotas=0;
            empates=0;
            a_favor=0;
            en_contra=0;
             /*
                ESTADISTICAS TOTALES TEMPORADA EQUIPO DE CASA


            */
            result = stmt.executeQuery("Select wins,draws,loses,a_favor,en_contra,total" +
                    " from public.results where team='"+local+"'");
            while (result.next()){
                Odds1Ganar.add((double)result.getInt("wins")/result.getInt("total"));
                odds1Draw.add((double)result.getInt("draws")/result.getInt("total"));
                odds1Lose.add((double)result.getInt("loses")/result.getInt("total"));
                odds1AFavor.add((double)result.getInt("a_favor")/result.getInt("total"));
                odds1Contra.add((double)result.getInt("en_contra")/result.getInt("total"));
            }
            result.close();
            /*
                ESTADISTICAS TOTALES TEMPORADA EQUIPO DE FUERA


            */
            result = stmt.executeQuery("Select wins,draws,loses,a_favor,en_contra,total" +
                    " from public.results where team='"+away+"'");
            while (result.next()){
                odds2Ganar.add((double)result.getInt("wins")/result.getInt("total"));
                odds2Draw.add((double)result.getInt("draws")/result.getInt("total"));
                odds2Perder.add((double)result.getInt("loses")/result.getInt("total"));
                odds2Favor.add((double)result.getInt("a_favor")/result.getInt("total"));
                odds2Contra.add((double)result.getInt("en_contra")/result.getInt("total"));
            }
            result.close();
            result = stmt.executeQuery("Select *" +
                    " from public.matches where home_team='"+local+"'or away_team='"+local+"'order by jornada DESC Limit 5");
            while (result.next()){
                if(result.getString("home_team").equals(local)) {
                    victorias = victorias + result.getInt("win_home");
                    empates = empates + result.getInt("draw_match");
                    derrotas = derrotas + result.getInt("win_away");
                    a_favor = a_favor + result.getInt("goal_home");
                    en_contra = en_contra + result.getInt("goal_away");
                }else{
                    victorias = victorias + result.getInt("win_away");
                    empates = empates + result.getInt("draw_match");
                    derrotas = derrotas + result.getInt("win_home");
                    a_favor = a_favor + result.getInt("goal_away");
                    en_contra = en_contra + result.getInt("goal_home");
                }
            }
            Odds1Ganar.add((double)victorias/(victorias+empates+derrotas));
            odds1Draw.add((double)empates/(victorias+empates+derrotas));
            odds1Lose.add((double)derrotas/(victorias+empates+derrotas));
            odds1AFavor.add((double)a_favor/(victorias+empates+derrotas));
            odds1Contra.add((double)en_contra/(victorias+empates+derrotas));
            result.close();
            victorias=0;
            derrotas=0;
            empates=0;
            a_favor=0;
            en_contra=0;
            result = stmt.executeQuery("Select *" +
                    " from public.matches where away_team='"+away+"'order by jornada DESC Limit 5");
            while (result.next()){
                if(result.getString("away_team").equals(away)){
                victorias=victorias+result.getInt("win_away");
                empates=empates+result.getInt("draw_match");
                derrotas=derrotas+result.getInt("win_home");
                a_favor=a_favor+result.getInt("goal_away");
                en_contra=en_contra+result.getInt("goal_home");
                }else {
                    victorias=victorias+result.getInt("win_home");
                    empates=empates+result.getInt("draw_match");
                    derrotas=derrotas+result.getInt("win_away");
                    a_favor=a_favor+result.getInt("goal_home");
                    en_contra=en_contra+result.getInt("goal_away");
                }
            }
            odds2Ganar.add((double)victorias/(victorias+empates+derrotas));
            odds2Draw.add((double)empates/(victorias+empates+derrotas));
            odds2Perder.add((double)derrotas/(victorias+empates+derrotas));
            odds2Favor.add((double)a_favor/(victorias+empates+derrotas));
            odds2Contra.add((double)en_contra/(victorias+empates+derrotas));
            result.close();
            victorias=0;
            derrotas=0;
            empates=0;
            a_favor=0;
            en_contra=0;

            result = stmt.executeQuery("Select *" +
                    " from public.matches where home_team='"+local+"'or away_team='"+local+"'order by jornada DESC Limit 1");
            while (result.next()){
                if(result.getString("home_team").equals(local)) {
                    resBuscadoW=result.getInt("win_home");
                    resBuscadoD=result.getInt("draw_match");
                    resBuscadoL=result.getInt("win_away");
                }else{
                    resBuscadoW=result.getInt("win_away");
                    resBuscadoD=result.getInt("draw_match");
                    resBuscadoL=result.getInt("win_home");
                }

            }
            result.close();
            result = stmt.executeQuery("Select *" +
                    " from public.matches where home_team='"+local+"'or away_team='"+local+"'order by jornada ASC ");
            int aux=0;
            while (result.next()){
                if(result.getInt("jornada")==1 && result.getString("home_team").equals(local)){
                    if (result.getInt("win_home")==1 && resBuscadoW==1){
                        aux=1;
                    }else if(result.getInt("win_away")==1 && resBuscadoL==1){
                        aux=1;
                    }else if (resBuscadoD==1){
                        aux=1;
                    }
                }else if (result.getInt("jornada")==1 && result.getString("away_team").equals(local)) {
                    if (result.getInt("win_away")==1 && resBuscadoW==1){
                        aux=1;
                    }else if(result.getInt("win_home")==1 && resBuscadoL==1){
                        aux=1;
                    }else if(resBuscadoD==1){
                        aux=1;
                    }
                }else{
                if(result.getString("home_team").equals(local)) {
                    if(aux==1){
                    victorias = victorias + result.getInt("win_home");
                    empates = empates + result.getInt("draw_match");
                    derrotas = derrotas + result.getInt("win_away");
                    a_favor = a_favor + result.getInt("goal_home");
                    en_contra = en_contra + result.getInt("goal_away");}
                    if (result.getInt("win_home")==1 && resBuscadoW==1){
                        aux=resBuscadoW;
                    }else if(result.getInt("win_away")==1 && resBuscadoL==1){
                        aux=resBuscadoL;
                    }else if (resBuscadoD==1){
                        aux=resBuscadoD;
                    }else {aux=0;}
                }else{
                    if(aux==1){
                    victorias = victorias + result.getInt("win_away");
                    empates = empates + result.getInt("draw_match");
                    derrotas = derrotas + result.getInt("win_home");
                    a_favor = a_favor + result.getInt("goal_away");
                    en_contra = en_contra + result.getInt("goal_home");
                    }
                    if (result.getInt("win_away")==1 && resBuscadoW==1){
                        aux=1;
                    }else if(result.getInt("win_home")==1 && resBuscadoL==1){
                        aux=1;
                    }else if(resBuscadoD==1){
                        aux=1;
                    }else{
                        aux=0;
                    }
                }
                }
            }
            Odds1Ganar.add((double)victorias/(victorias+empates+derrotas));
            odds1Draw.add((double)empates/(victorias+empates+derrotas));
            odds1Lose.add((double)derrotas/(victorias+empates+derrotas));
            odds1AFavor.add((double)a_favor/(victorias+empates+derrotas));
            odds1Contra.add((double)en_contra/(victorias+empates+derrotas));
            result.close();
            System.out.println(victorias+" "+empates+" "+derrotas);

            victorias=0;
            derrotas=0;
            empates=0;
            a_favor=0;
            en_contra=0;

            result = stmt.executeQuery("Select *" +
                    " from public.matches where home_team='"+away+"'or away_team='"+away+"'order by jornada DESC Limit 1");
            while (result.next()){
                if(result.getString("home_team").equals(away)) {
                    resBuscadoW=result.getInt("win_home");
                    resBuscadoD=result.getInt("draw_match");
                    resBuscadoL=result.getInt("win_away");
                }else{
                    resBuscadoW=result.getInt("win_away");
                    resBuscadoD=result.getInt("draw_match");
                    resBuscadoL=result.getInt("win_home");
                }

            }
            result.close();
            result = stmt.executeQuery("Select *" +
                    " from public.matches where home_team='"+away+"'or away_team='"+away+"'order by jornada ASC ");
             aux=0;
            while (result.next()){
                if(result.getInt("jornada")==1 && result.getString("home_team").equals(away)){
                    if (result.getInt("win_home")==1 && resBuscadoW==1){
                        aux=1;
                    }else if(result.getInt("win_away")==1 && resBuscadoL==1){
                        aux=1;
                    }else if (resBuscadoD==1){
                        aux=1;
                    }
                }else if (result.getInt("jornada")==1 && result.getString("away_team").equals(local)) {
                    if (result.getInt("win_away")==1 && resBuscadoW==1){
                        aux=1;
                    }else if(result.getInt("win_home")==1 && resBuscadoL==1){
                        aux=1;
                    }else if(resBuscadoD==1){
                        aux=1;
                    }
                }else{
                    if(result.getString("home_team").equals(away)) {
                        if(aux==1){
                            victorias = victorias + result.getInt("win_home");
                            empates = empates + result.getInt("draw_match");
                            derrotas = derrotas + result.getInt("win_away");
                            a_favor = a_favor + result.getInt("goal_home");
                            en_contra = en_contra + result.getInt("goal_away");}
                        if (result.getInt("win_home")==1 && resBuscadoW==1){
                            aux=resBuscadoW;
                        }else if(result.getInt("win_away")==1 && resBuscadoL==1){
                            aux=resBuscadoL;
                        }else if (resBuscadoD==1){
                            aux=resBuscadoD;
                        }else {aux=0;}
                    }else{
                        if(aux==1){
                            victorias = victorias + result.getInt("win_away");
                            empates = empates + result.getInt("draw_match");
                            derrotas = derrotas + result.getInt("win_home");
                            a_favor = a_favor + result.getInt("goal_away");
                            en_contra = en_contra + result.getInt("goal_home");
                        }
                        if (result.getInt("win_away")==1 && resBuscadoW==1){
                            aux=1;
                        }else if(result.getInt("win_home")==1 && resBuscadoL==1){
                            aux=1;
                        }else if(resBuscadoD==1){
                            aux=1;
                        }else{
                            aux=0;
                        }
                    }
                }
            }
            odds2Ganar.add((double)victorias/(victorias+empates+derrotas));
            odds2Draw.add((double)empates/(victorias+empates+derrotas));
            odds2Perder.add((double)derrotas/(victorias+empates+derrotas));
            odds2Favor.add((double)a_favor/(victorias+empates+derrotas));
            odds2Contra.add((double)en_contra/(victorias+empates+derrotas));
            result.close();


            connection.close();
            Odds1Ganar.add(1/oddLocal);
            odds1Draw.add(1/oddDraw);
            odds1Lose.add(1/oddAway);
            odds2Ganar.add(1/oddAway);
            odds2Draw.add(1/oddDraw);
            odds2Perder.add(1/oddLocal);
            aux=0;
            for (double aux1:Odds1Ganar) {
                probLocal1=probLocal1+aux1;
                aux++;
            }
            probLocal1=probLocal1/aux;
            aux=0;
            for (double aux1:odds1Draw) {
                probEmpate1=probEmpate1+aux1;
                aux++;
            }
            probEmpate1=probEmpate1/aux;
            aux=0;
            for (double aux1:odds1Lose) {
                probVisitante1=probVisitante1+aux1;
                aux++;
            }
            probVisitante1=probVisitante1/aux;

            aux=0;

            for (double aux1:odds2Ganar) {
                probVisitante2=probVisitante2+aux1;
                aux++;
            }
            probVisitante2=probVisitante2/aux;

            aux=0;
            for (double aux1:odds2Draw) {
                probEmpate2=probEmpate2+aux1;
                aux++;
            }
            probEmpate2=probEmpate2/aux;
            aux=0;
            for (double aux1:odds2Perder) {

                probLocal2=probLocal2+aux1;
                aux++;
            }
            probLocal2=probLocal2/aux;

            probLocalT=(probLocal1+probLocal2)/2;
            probEmpateT=(probEmpate1+probEmpate2)/2;
            probVisitanteT=(probVisitante2+probVisitante1)/2;

            System.out.println(probLocalT+" "+probEmpateT+" "+probVisitanteT);
            String betSimpleS="";
            String betCombiS="";
            double betOddS=0.0;
            double betOddC=0.0;
            double LX =probLocalT+probEmpateT;
            double XV =probVisitanteT+probEmpateT;
            double LV =probLocalT+probVisitanteT;
            double betSimpleD=0.0;
            double betCombinada=0.0;
            if (probLocalT > probEmpateT) {
                if (probLocalT > probVisitanteT) {
                    System.out.println("El mayor es: " + probLocalT);
                    betSimpleD=probLocalT;
                    betSimpleS="1";
                    betOddS=oddLocal;
                } else {
                    System.out.println("el mayor es: " + probVisitanteT);
                    betSimpleD=probVisitanteT;
                    betSimpleS="2";
                    betOddS=oddAway;
                }
            } else if (probEmpateT > probVisitanteT) {
                System.out.println("el mayor es: " + probEmpateT);
                betSimpleD=probEmpateT;
                betSimpleS="X";
                betOddS=oddDraw;
            } else {
                System.out.println("el mayor es: " + probVisitanteT);
                betSimpleD=probVisitanteT;
                betSimpleS="2";
                betOddS=oddAway;
            }
            if (LX > XV) {
                if (LX > LV) {
                    System.out.println("El mayor es: " + LX);
                    betCombinada=LX;
                    betCombiS="1-X";
                } else {
                    System.out.println("el mayor es: " + LV);
                    betCombinada=LV;
                    betCombiS="1-2";
                }
            } else if (XV > LV) {
                System.out.println("el mayor es: " + XV);
                betCombinada=XV;
                betCombiS="X-2";
            } else {
                System.out.println("el mayor es: " + LV);
                betCombinada=LV;
                betCombiS="1-2";
            }
            System.out.println("Introduzca Cuota de "+betCombiS+":");
            betOddC=sc.nextDouble();
            partido=new Bet(local+" "+away,betSimpleS,betCombiS,betSimpleD,betCombinada,betOddS,betOddC);
            partido.toString();
            return partido;
            /*while(!Odds1Ganar.isEmpty()){
                System.out.println(Odds1Ganar.get(0));
                Odds1Ganar.remove(0);
            }while(!odds1Draw.isEmpty()){
                System.out.println(odds1Draw.get(0));
                odds1Draw.remove(0);
            }while(!odds1Lose.isEmpty()){
                System.out.println(odds1Lose.get(0));
                odds1Lose.remove(0);
            }*/
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error: " + sqle);
        }
        return null;
    }
    public ArrayList<Bet> deleteList(){
        ArrayList<Bet> aux=new ArrayList<Bet>();
        return aux; 
    }
    public static void main(String[] args) {
        ArrayList<Bet> betList=new ArrayList<Bet>();
        int salir=1;

        Scanner sc =new Scanner(System.in);
        JavaPostgreSQLBasic javaPostgreSQLBasic = new JavaPostgreSQLBasic();
        while(salir!=0){
        System.out.println("Seleccionar Opcion: \n 1.Introducir Partidos \n 2.Analisis \n 3.Elegir Bets \n 4.Borrar partidos");
        int opcion=sc.nextInt();
        salir=opcion;
        if(opcion==1){

            javaPostgreSQLBasic.insertarPartidos();


        }else if (opcion==2){

            Bet aux=javaPostgreSQLBasic.consultaBet();
            betList.add(aux);
            for (Bet bet : betList) {
                bet.toString();
            }
        }else if(opcion==3){
            betList=javaPostgreSQLBasic.selectMatch(betList);
            for (Bet bet : betList) {
                bet.toString();
            }
           
        }else if(opcion==4){
            betList=javaPostgreSQLBasic.deleteList();
        }
        }
    }
}
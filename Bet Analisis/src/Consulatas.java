import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Consulatas {

    public static void main(String[] args) {
        ArrayList<Bet> betList = new ArrayList<Bet>();
        int salir = 1;

        Scanner sc = new Scanner(System.in);
        while (salir != 0) {
            System.out.println("Seleccionar Opcion: \n 1.Introducir Partidos ");
            int opcion = sc.nextInt();
            salir = opcion;
            if (opcion == 1) {
                Consulatas.consultarLista();
            }
        }
    }

    public static ArrayList<Partido> getPartidos() {
        Partido aux1 = new Partido();
        ArrayList<Partido> aux = new ArrayList<Partido>();
        try {
            Connection connection1 = null;

            connection1 = DriverManager.getConnection("jdbc:postgresql://loststyle.ddns.net:5433/bet_analisis",
                    "postgres", "hacker21");
            PreparedStatement stmnt1 = connection1
                    .prepareStatement("SELECT id, hometeam, awayteam FROM public.active_matches where active=0;");
            ResultSet result = stmnt1.executeQuery();

            while (result.next()) {

                aux1.setHome(result.getString("hometeam"));
                aux1.setAway(result.getString("awayteam"));
                aux.add(aux1);
                System.out.println(aux1.getHome() + " " + aux1.getAway());
            }
            result.close();
            connection1.close();
            return aux;

        } catch (Exception e) {
            return null;
        }
    }

    public static void consultarLista() {
        String home = "";
        String away = "";
        double probGF = 0;
        double probDF = 0;
        double probLF = 0;
        double probGH = 0;
        double probDH = 0;
        double probLH = 0;
        double totHGF = 0.0;
        double totHDF = 0.0;
        double totHPF = 0.0;
        double totHGH = 0.0;
        double totHDH = 0.0;
        double totHPH = 0.0;
        double totAGF = 0.0;
        double totADF = 0.0;
        double totAPF = 0.0;
        double totAGH = 0.0;
        double totADH = 0.0;
        double totAPH = 0.0;
        int total = 0;
        ArrayList<Double> probHGF = new ArrayList<Double>();
        ArrayList<Double> probHDF = new ArrayList<Double>();
        ArrayList<Double> probHPF = new ArrayList<Double>();
        ArrayList<Double> probAGF = new ArrayList<Double>();
        ArrayList<Double> probADF = new ArrayList<Double>();
        ArrayList<Double> probAPF = new ArrayList<Double>();
        ArrayList<Double> probHGH = new ArrayList<Double>();
        ArrayList<Double> probHDH = new ArrayList<Double>();
        ArrayList<Double> probHPH = new ArrayList<Double>();
        ArrayList<Double> probAGH = new ArrayList<Double>();
        ArrayList<Double> probADH = new ArrayList<Double>();
        ArrayList<Double> probAPH = new ArrayList<Double>();
        ArrayList<Partido> partidos = new ArrayList<Partido>();
        int id = 0;
        double oddHF = 0.0;
        double oddDF = 0.0;
        double oddAF = 0.0;
        double oddHH = 0.0;
        double oddDH = 0.0;
        double oddAH = 0.0;
        Scanner sc = new Scanner(System.in);
        Statement stmnt;
        partidos = getPartidos();
        for (Partido aux1 : partidos) {
            System.out.println(aux1.getHome() + " VS " + aux1.getAway());
            System.out.println("Odd local Full: ");
            oddHF = sc.nextDouble();
            System.out.println("Odd draw Full: ");
            oddDF = sc.nextDouble();
            System.out.println("Odd away Full: ");
            oddAF = sc.nextDouble();
            System.out.println("Odd local Half: ");
            oddHH = sc.nextDouble();
            System.out.println("Odd draw Half: ");
            oddDH = sc.nextDouble();
            System.out.println("Odd away Half: ");
            oddAH = sc.nextDouble();
            // AÑADIR PROBABILIDADES DE BET365 A LAS LISTAS
            probHGF.add(1 / oddHF);
            probHDF.add(1 / oddDF);
            probHPF.add(1 / oddAF);
            probAGF.add(1 / oddAF);
            probADF.add(1 / oddDF);
            probAPF.add(1 / oddHF);
            probHGH.add(1 / oddHH);
            probHDH.add(1 / oddDH);
            probHPH.add(1 / oddAH);
            probAGH.add(1 / oddAH);
            probADH.add(1 / oddDH);
            probAPH.add(1 / oddHH);
            System.out.println(oddAH);
            /*
             * ESTADISTICAS EQUIPO EN CASA HISTORICO
             */try {
                Connection connection = null;

                connection = DriverManager.getConnection("jdbc:postgresql://loststyle.ddns.net:5433/bet_analisis",
                        "postgres", "hacker21");
                stmnt = connection.createStatement();
                ResultSet result = stmnt.executeQuery(
                        "SELECT scoreHomeHalf, scoreHomeFull, scoreAwayHalf, scoreAwayFull FROM public.matches_2021 where hometeam="
                                + aux1.getHome() + ";");
                while (result.next()) {
                    probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probGF + 1
                            : probGF + 0;
                    probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                            : probDF + 0;
                    probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probLF + 1
                            : probLF + 0;
                    probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probGH + 1
                            : probGH + 0;
                    probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                            : probDH + 0;
                    probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probLH + 1
                            : probLH + 0;
                    total += 1;
                }
                probHGF.add(probGF / total);
                probHDF.add(probDF / total);
                probHPF.add(probLF / total);
                probHGH.add(probGF / total);
                probHDH.add(probDH / total);
                probHPH.add(probLH / total);

                result.close();
                probGF = 0;
                probDF = 0;
                probLF = 0;
                probGH = 0;
                probDH = 0;
                probLH = 0;
                total = 0;
                /*
                 * ESTADISTICAS EQUIPO EN CASA ESTA TEMPORADA
                 */

                result = stmnt.executeQuery(
                        "SELECT \"scoreHomeHalf\", \"scoreHomeFull\", \"scoreAwayHalf\", \"scoreAwayFull\" FROM public.matches_2022 where hometeam="
                                + aux1.getHome() + ";");
                while (result.next()) {
                    probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probGF + 1
                            : probGF + 0;
                    probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                            : probDF + 0;
                    probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probLF + 1
                            : probLF + 0;
                    probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probGH + 1
                            : probGH + 0;
                    probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                            : probDH + 0;
                    probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probLH + 1
                            : probLH + 0;
                    total += 1;
                }
                probHGF.add(probGF / total);
                probHDF.add(probDF / total);
                probHPF.add(probLF / total);
                probHGH.add(probGF / total);
                probHDH.add(probDH / total);
                probHPH.add(probLH / total);
                result.close();
                probGF = 0;
                probDF = 0;
                probLF = 0;
                probGH = 0;
                probDH = 0;
                probLH = 0;
                total = 0;
                /*
                 * ESTADISTICAS EQUIPO FUERA DE CASA HISTORICO
                 */
                result = stmnt.executeQuery(
                        "SELECT \"scoreHomeHalf\", \"scoreHomeFull\", \"scoreAwayHalf\", \"scoreAwayFull\" FROM public.matches_2021 where awayteam="
                                + aux1.getAway() + ";");
                while (result.next()) {
                    probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probGF + 1
                            : probGF + 0;
                    probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                            : probDF + 0;
                    probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probLF + 1
                            : probLF + 0;
                    probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probGH + 1
                            : probGH + 0;
                    probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                            : probDH + 0;
                    probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probLH + 1
                            : probLH + 0;
                    total += 1;
                }
                probAGF.add(probGF / total);
                probADF.add(probDF / total);
                probAPF.add(probLF / total);
                probAGH.add(probGF / total);
                probADH.add(probDH / total);
                probAPH.add(probLH / total);
                result.close();
                probGF = 0;
                probDF = 0;
                probLF = 0;
                probGH = 0;
                probDH = 0;
                probLH = 0;
                total = 0;
                /*
                 * ESTADISTICAS EQUIPO FUERA DE CASA ESTA TEMPORADA
                 */
                result = stmnt.executeQuery(
                        "SELECT \"scoreHomeHalf\", \"scoreHomeFull\", \"scoreAwayHalf\", \"scoreAwayFull\" FROM public.matches_2022 where awayteam="
                                + aux1.getAway() + ";");
                while (result.next()) {
                    probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probGF + 1
                            : probGF + 0;
                    probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                            : probDF + 0;
                    probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probLF + 1
                            : probLF + 0;
                    probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probGH + 1
                            : probGH + 0;
                    probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                            : probDH + 0;
                    probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probLH + 1
                            : probLH + 0;
                    total += 1;
                }
                probAGF.add(probGF / total);
                probADF.add(probDF / total);
                probAPF.add(probLF / total);
                probAGH.add(probGF / total);
                probADH.add(probDH / total);
                probAPH.add(probLH / total);
                result.close();
                probGF = 0;
                probDF = 0;
                probLF = 0;
                probGH = 0;
                probDH = 0;
                probLH = 0;
                total = 0;
                /*
                 * ESTADISTICAS EQUIPO EN CASA ULTIMOS 5 PARTIDOS
                 */
                result = stmnt.executeQuery(
                        "Select  \"scoreHomeHalf\", \"scoreHomeFull\", \"scoreAwayHalf\", \"scoreAwayFull\" FROM public.matches_2022 where home_team='"
                                + aux1.getHome() + "'order by id DESC Limit 5");
                while (result.next()) {
                    probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probGF + 1
                            : probGF + 0;
                    probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                            : probDF + 0;
                    probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probLF + 1
                            : probLF + 0;
                    probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probGH + 1
                            : probGH + 0;
                    probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                            : probDH + 0;
                    probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probLH + 1
                            : probLH + 0;
                    total += 1;
                }
                probHGF.add(probGF / total);
                probHDF.add(probDF / total);
                probHPF.add(probLF / total);
                probHGH.add(probGF / total);
                probHDH.add(probDH / total);
                probHPH.add(probLH / total);
                result.close();
                probGF = 0;
                probDF = 0;
                probLF = 0;
                probGH = 0;
                probDH = 0;
                probLH = 0;
                total = 0;
                /*
                 * ESTADISTICAS EQUIPO FUERA DE CASA ULTIMOS 5 PARTIDOS
                 */
                result = stmnt.executeQuery(
                        "SELECT \"scoreHomeHalf\", \"scoreHomeFull\", \"scoreAwayHalf\", \"scoreAwayFull\" FROM public.matches_2022 where awayteam="
                                + aux1.getAway() + "order by id DESC Limit 5;");
                while (result.next()) {
                    probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probGF + 1
                            : probGF + 0;
                    probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                            : probDF + 0;
                    probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probLF + 1
                            : probLF + 0;
                    probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probGH + 1
                            : probGH + 0;
                    probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                            : probDH + 0;
                    probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probLH + 1
                            : probLH + 0;
                    total += 1;
                }
                probAGF.add(probGF / total);
                probADF.add(probDF / total);
                probAPF.add(probLF / total);
                probAGH.add(probGF / total);
                probADH.add(probDH / total);
                probAPH.add(probLH / total);
                result.close();
                probGF = 0;
                probDF = 0;
                probLF = 0;
                probGH = 0;
                probDH = 0;
                probLH = 0;
                total = 0;
                /*
                 * ESTADISTICAS ULTIMOS 5 PARTIDOS HOMETEAM
                 */
                result = stmnt.executeQuery(
                        "SELECT hometeam,awayteam  \"scoreHomeHalf\", \"scoreHomeFull\", \"scoreAwayHalf\", \"scoreAwayFull\" "
                                + "FROM public.matches_2022 where hometeam='" + aux1.getHome() + "' or awayteam='"
                                + aux1.getHome() + "' order by id DESC Limit 5;");
                while (result.next()) {
                    if (result.getString("hometeam").equals(aux1.getHome())) {
                        probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probGF + 1
                                : probGF + 0;
                        probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                                : probDF + 0;
                        probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probLF + 1
                                : probLF + 0;
                        probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probGH + 1
                                : probGH + 0;
                        probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                                : probDH + 0;
                        probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probLH + 1
                                : probLH + 0;

                    } else {
                        probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probGF + 1
                                : probGF + 0;
                        probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                                : probDF + 0;
                        probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probLF + 1
                                : probLF + 0;
                        probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probGH + 1
                                : probGH + 0;
                        probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                                : probDH + 0;
                        probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probLH + 1
                                : probLH + 0;
                    }
                    total += 1;
                }
                probHGF.add(probGF / total);
                probHDF.add(probDF / total);
                probHPF.add(probLF / total);
                probHGH.add(probGF / total);
                probHDH.add(probDH / total);
                probHPH.add(probLH / total);
                result.close();
                probGF = 0;
                probDF = 0;
                probLF = 0;
                probGH = 0;
                probDH = 0;
                probLH = 0;
                total = 0;
                /*
                 * ESTADISTICAS ULTIMOS 5 PARTIDOS AWAYTEAM
                 */
                result = stmnt.executeQuery(
                        "SELECT hometeam,awayteam  \"scoreHomeHalf\", \"scoreHomeFull\", \"scoreAwayHalf\", \"scoreAwayFull\" "
                                + "FROM public.matches_2022 where hometeam='" + aux1.getAway() + "' or awayteam='"
                                + aux1.getAway() + "' order by id DESC Limit 5;");
                while (result.next()) {
                    if (result.getString("hometeam").equals(aux1.getAway())) {
                        probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probGF + 1
                                : probGF + 0;
                        probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                                : probDF + 0;
                        probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probLF + 1
                                : probLF + 0;
                        probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probGH + 1
                                : probGH + 0;
                        probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                                : probDH + 0;
                        probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probLH + 1
                                : probLH + 0;

                    } else {
                        probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? probGF + 1
                                : probGF + 0;
                        probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? probDF + 1
                                : probDF + 0;
                        probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? probLF + 1
                                : probLF + 0;
                        probGH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) < 0) ? probGH + 1
                                : probGH + 0;
                        probDH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) == 0) ? probDH + 1
                                : probDH + 0;
                        probLH = ((result.getInt("scoreHomeHalf") - result.getInt("scoreAwayHalf")) > 0) ? probLH + 1
                                : probLH + 0;
                    }
                    total += 1;
                }
                probHGF.add(probGF / total);
                probHDF.add(probDF / total);
                probHPF.add(probLF / total);
                probHGH.add(probGF / total);
                probHDH.add(probDH / total);
                probHPH.add(probLH / total);
                result.close();
                probGF = 0;
                probDF = 0;
                probLF = 0;
                probGH = 0;
                probDH = 0;
                probLH = 0;
                total = 0;
                /*
                 * RACHA DE PARTIDOS HOME TEAM
                 */
                int resBuscadoWF = 0;
                int resBuscadoDF = 0;
                int resBuscadoLF = 0;
                int resBuscadoWH = 0;
                int resBuscadoDH = 0;
                int resBuscadoLH = 0;
                result = stmnt.executeQuery("Select *" + " from public.matches_2022 where hometeam='" + aux1.getHome()
                        + "'or awayteam='" + aux1.getHome() + "'order by id DESC Limit 1");
                while (result.next()) {
                    if (result.getString("hometeam").equals(aux1.getHome())) {
                        resBuscadoWF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? 1 : 0;
                        resBuscadoDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? 1 : 0;
                        resBuscadoLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? 1 : 0;
                    } else if (result.getString("awayteam").equals(aux1.getHome())) {
                        resBuscadoWF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? 1 : 0;
                        resBuscadoDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? 1 : 0;
                        resBuscadoLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? 1 : 0;
                    }
                }
                result.close();
                int i = 0;
                int aux2 = 0;
                result = stmnt.executeQuery("Select *" + " from public.matches_2022 where home_team='" + aux1.getHome()
                        + "'or away_team='" + aux1.getHome() + "'order by id ASC ");
                while (result.next()) {
                    if (i == 0 && result.getString("hometeam").equals(aux1.getHome())) {
                        if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0
                                && resBuscadoWF == 1) {
                            aux2 = 1;
                        } else if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0
                                && resBuscadoLF == 1) {
                            aux2 = 1;
                        } else if (resBuscadoDF == 1) {
                            aux2 = 1;
                        }
                        i = 1;
                    } else if (i == 0 && result.getString("awayteam").equals(aux1.getHome())) {
                        if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0
                                && resBuscadoWF == 1) {
                            aux2 = 1;
                        } else if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0
                                && resBuscadoLF == 1) {
                            aux2 = 1;
                        } else if (resBuscadoDF == 1) {
                            aux2 = 1;
                        }
                        i = 1;
                    } else {
                        if (result.getString("hometeam").equals(aux1.getHome())) {
                            if (aux2 == 1) {
                                probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0)
                                        ? probGF + 1
                                        : probGF + 0;
                                probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0)
                                        ? probDF + 1
                                        : probDF + 0;
                                probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0)
                                        ? probLF + 1
                                        : probLF + 0;
                            }
                            if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0
                                    && resBuscadoWF == 1) {
                                aux2 = resBuscadoWF;
                            } else if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0
                                    && resBuscadoLF == 1) {
                                aux2 = resBuscadoLF;
                            } else if (resBuscadoDF == 1) {
                                aux2 = resBuscadoDF;
                            } else {
                                aux2 = 0;
                            }
                        } else {
                            if (aux2 == 1) {
                                probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0)
                                        ? probGF + 1
                                        : probGF + 0;
                                probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0)
                                        ? probDF + 1
                                        : probDF + 0;
                                probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0)
                                        ? probLF + 1
                                        : probLF + 0;
                            }
                            if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0
                                    && resBuscadoWF == 1) {
                                aux2 = 1;
                            } else if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0
                                    && resBuscadoLF == 1) {
                                aux2 = 1;
                            } else if (resBuscadoDF == 1) {
                                aux2 = 1;
                            } else {
                                aux2 = 0;
                            }
                        }
                    }
                }
                result.close();
                probHGF.add(probGF / (probGF + probDF + probLF));
                probHDF.add(probDF / (probGF + probDF + probLF));
                probHPF.add(probLF / (probGF + probDF + probLF));
                probGF = 0;
                probDF = 0;
                probLF = 0;
                resBuscadoWF = 0;
                resBuscadoDF = 0;
                resBuscadoLF = 0;
                /*
                 * RACHA PARTIDOS EQUIPO VISITANTE
                 */
                result = stmnt.executeQuery("Select *" + " from public.matches_2022 where hometeam='" + aux1.getAway()
                        + "'or awayteam='" + aux1.getAway() + "'order by id DESC Limit 1");
                while (result.next()) {
                    if (result.getString("hometeam").equals(aux1.getAway())) {
                        resBuscadoWF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? 1 : 0;
                        resBuscadoDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? 1 : 0;
                        resBuscadoLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? 1 : 0;
                    } else if (result.getString("awayteam").equals(aux1.getAway())) {
                        resBuscadoWF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0) ? 1 : 0;
                        resBuscadoDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0) ? 1 : 0;
                        resBuscadoLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0) ? 1 : 0;
                    }
                }
                result.close();
                i = 0;
                aux2 = 0;
                result = stmnt.executeQuery("Select *" + " from public.matches_2022 where home_team='" + aux1.getAway()
                        + "'or away_team='" + aux1.getAway() + "'order by id ASC ");
                while (result.next()) {
                    if (i == 0 && result.getString("hometeam").equals(aux1.getAway())) {
                        if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0
                                && resBuscadoWF == 1) {
                            aux2 = 1;
                        } else if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0
                                && resBuscadoLF == 1) {
                            aux2 = 1;
                        } else if (resBuscadoDF == 1) {
                            aux2 = 1;
                        }
                        i = 1;
                    } else if (i == 0 && result.getString("awayteam").equals(aux1.getAway())) {
                        if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0
                                && resBuscadoWF == 1) {
                            aux2 = 1;
                        } else if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0
                                && resBuscadoLF == 1) {
                            aux2 = 1;
                        } else if (resBuscadoDF == 1) {
                            aux2 = 1;
                        }
                        i = 1;
                    } else {
                        if (result.getString("hometeam").equals(aux1.getAway())) {
                            if (aux2 == 1) {
                                probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0)
                                        ? probGF + 1
                                        : probGF + 0;
                                probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0)
                                        ? probDF + 1
                                        : probDF + 0;
                                probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0)
                                        ? probLF + 1
                                        : probLF + 0;
                            }
                            if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0
                                    && resBuscadoWF == 1) {
                                aux2 = resBuscadoWF;
                            } else if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0
                                    && resBuscadoLF == 1) {
                                aux2 = resBuscadoLF;
                            } else if (resBuscadoDF == 1) {
                                aux2 = resBuscadoDF;
                            } else {
                                aux2 = 0;
                            }
                        } else {
                            if (aux2 == 1) {
                                probGF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0)
                                        ? probGF + 1
                                        : probGF + 0;
                                probDF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) == 0)
                                        ? probDF + 1
                                        : probDF + 0;
                                probLF = ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0)
                                        ? probLF + 1
                                        : probLF + 0;
                            }
                            if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) < 0
                                    && resBuscadoWF == 1) {
                                aux2 = 1;
                            } else if ((result.getInt("scoreHomeFull") - result.getInt("scoreAwayFull")) > 0
                                    && resBuscadoLF == 1) {
                                aux2 = 1;
                            } else if (resBuscadoDF == 1) {
                                aux2 = 1;
                            } else {
                                aux2 = 0;
                            }
                        }
                    }
                }
                result.close();
                i = 0;
                aux2 = 0;
                probAGF.add(probGF / (probGF + probDF + probLF));
                probADF.add(probDF / (probGF + probDF + probLF));
                probAPF.add(probLF / (probGF + probDF + probLF));
                probGF = 0;
                probDF = 0;
                probLF = 0;
                resBuscadoWF = 0;
                resBuscadoDF = 0;
                resBuscadoLF = 0;
                for (Double double1 : probHGF) {
                    totHGF = totHGF + double1;
                    aux2++;
                }
                totHGF = totHGF / aux2;
                aux2 = 0;
                for (Double double1 : probHDF) {
                    totHDF = totHDF + double1;
                    aux2++;
                }
                totHDF = totHDF / aux2;
                aux2 = 0;
                for (Double double1 : probHPF) {
                    totHPF = totHPF + double1;
                    aux2++;
                }
                totHPF = totHPF / aux2;
                aux2 = 0;
                for (Double double1 : probAGF) {
                    totAGF = totAGF + double1;
                    aux2++;
                }
                totAGF = totAGF / aux2;
                aux2 = 0;
                for (Double double1 : probADF) {
                    totADF = totADF + double1;
                    aux2++;
                }
                totADF = totADF / aux2;
                aux2 = 0;
                for (Double double1 : probAPF) {
                    totAPF = totAPF + double1;
                    aux2++;
                }
                totAPF = totAPF / aux2;
                aux2 = 0;
                for (Double double1 : probHGH) {
                    totHGH = totHGH + double1;
                    aux2++;
                }
                totHGH = totHGH / aux2;
                aux2 = 0;
                for (Double double1 : probHDH) {
                    totHDH = totHDH + double1;
                    aux2++;
                }
                totHDH = totHDH / aux2;
                aux2 = 0;
                for (Double double1 : probHPH) {
                    totHPH = totHPH + double1;
                    aux2++;
                }
                totHPH = totHPH / aux2;
                aux2 = 0;
                for (Double double1 : probAGH) {
                    totAGH = totAGH + double1;
                    aux2++;
                }
                totAGH = totAGH / aux2;
                aux2 = 0;
                for (Double double1 : probADH) {
                    totADH = totADH + double1;
                    aux2++;
                }
                totADH = totADH / aux2;
                double probLocalF = (totHGF + totAPF) / 2;
                double probEmpateF = (totHDF + totADF) / 2;
                double probVisitanteF = (totHPF + totAGF) / 2;
                double probLocalH = (totHGH + totAPH) / 2;
                double probEmpateH = (totHDH + totADH) / 2;
                double probVisitanteH = (totHPH + totAGH) / 2;
                System.out.println(probLocalF + " " + probEmpateF + " " + probVisitanteF);
                System.out.println(probLocalH + " " + probEmpateH + " " + probVisitanteH);
                String betSimpleS = "";
                String betCombiS = "";
                double betOddS = 0.0;
                double betOddC = 0.0;
                double LX = probLocalF + probEmpateF;
                double XV = probVisitanteF + probEmpateF;
                double LV = probLocalF + probVisitanteF;
                double betSimpleD = 0.0;
                double betCombinada = 0.0;
                if (probLocalF > probEmpateF) {
                    if (probLocalF > probVisitanteF) {
                        System.out.println("El mayor es: " + probLocalF);
                        betSimpleD = probLocalF;
                        betSimpleS = "1";
                        betOddS = oddHF;
                    } else {
                        System.out.println("el mayor es: " + probVisitanteF);
                        betSimpleD = probVisitanteF;
                        betSimpleS = "2";
                        betOddS = oddAF;
                    }
                } else if (probEmpateF > probVisitanteF) {
                    System.out.println("el mayor es: " + probEmpateF);
                    betSimpleD = probEmpateF;
                    betSimpleS = "X";
                    betOddS = oddDF;
                } else {
                    System.out.println("el mayor es: " + probVisitanteF);
                    betSimpleD = probVisitanteF;
                    betSimpleS = "2";
                    betOddS = oddAF;
                }
                if (LX > XV) {
                    if (LX > LV) {
                        System.out.println("El mayor es: " + LX);
                        betCombinada = LX;
                        betCombiS = "1-X";
                    } else {
                        System.out.println("el mayor es: " + LV);
                        betCombinada = LV;
                        betCombiS = "1-2";
                    }
                } else if (XV > LV) {
                    System.out.println("el mayor es: " + XV);
                    betCombinada = XV;
                    betCombiS = "X-2";
                } else {
                    System.out.println("el mayor es: " + LV);
                    betCombinada = LV;
                    betCombiS = "1-2";
                }
                System.out.println("Introduzca Cuota de " + betCombiS + ":");
                betOddC = sc.nextDouble();

                String betSimpleSH = "";
                String betCombiSH = "";
                double betOddSH = 0.0;
                double betOddCH = 0.0;
                double betSimpleDH = 0.0;
                if (probLocalH > probEmpateH) {
                    if (probLocalH > probVisitanteH) {
                        System.out.println("El mayor es: " + probLocalH);
                        betSimpleDH = probLocalH;
                        betSimpleSH = "1";
                        betOddS = oddHH;
                    } else {
                        System.out.println("el mayor es: " + probVisitanteH);
                        betSimpleDH = probVisitanteH;
                        betSimpleSH = "2";
                        betOddSH = oddAH;
                    }
                } else if (probEmpateH > probVisitanteH) {
                    System.out.println("el mayor es: " + probEmpateH);
                    betSimpleDH = probEmpateH;
                    betSimpleSH = "X";
                    betOddSH = oddDH;
                } else {
                    System.out.println("el mayor es: " + probVisitanteH);
                    betSimpleDH = probVisitanteH;
                    betSimpleSH = "2";
                    betOddSH = oddAH;
                }
                String recSingle = (betSimpleD > 0.55) ? "SI" : "NO";
                String recDouble = (betCombinada > 0.79) ? "SI" : "NO";
                String recHALF = (betSimpleDH > 0.55) ? "SI" : "NO";
                stmnt.executeQuery(
                        "INSERT INTO public.bets2022(hometeam, awayteam, \"perSingle\", \"perDouble\", \"recSingle\", \"recDouble\", \"betS\", \"betD\", \"betH\", \"perHalf\", \"recHalf\",\"oddS\", \"oddD\", \"oddH\")"
                                + "VALUES (" + aux1.getHome() + ", " + aux1.getAway() + ", " + betSimpleD + ", "
                                + betCombinada + ", '" + recSingle + "', '" + recDouble + "', '" + betSimpleS + "', '"
                                + betCombiS + "', '" + betSimpleSH + "', " + betSimpleDH + ", '" + recHALF + "',"
                                + betOddS + "," + betOddC + "," + betOddSH + ");");
                stmnt.executeQuery("UPDATE public.active_matches SET  active=1" + "WHERE id=" + id + ";");
                connection.close();
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }

}

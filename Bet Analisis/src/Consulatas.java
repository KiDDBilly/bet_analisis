import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Consulatas {
    public static void consultarLista(){
        String home="";
        String away="";
        ArrayList<Double> probHGF=new ArrayList<Double>();
        ArrayList<Double> probHDF=new ArrayList<Double>();
        ArrayList<Double> probHPF=new ArrayList<Double>();
        ArrayList<Double> probAGF=new ArrayList<Double>();
        ArrayList<Double> probADF=new ArrayList<Double>();
        ArrayList<Double> probAPF=new ArrayList<Double>();
        ArrayList<Double> probHGH=new ArrayList<Double>();
        ArrayList<Double> probHDH=new ArrayList<Double>();
        ArrayList<Double> probHPH=new ArrayList<Double>();
        ArrayList<Double> probAGH=new ArrayList<Double>();
        ArrayList<Double> probADH=new ArrayList<Double>();
        ArrayList<Double> probAPH=new ArrayList<Double>();
        int id=0;
        double oddHF;
        double oddDF;
        double oddAF;
        double oddHH;
        double oddDH;
        double oddAH;
        ResultSet res;
        Scanner sc =new Scanner(System.in);
        PreparedStatement stmnt;
        try {
            Connection connection = null;

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://loststyle.ddns.net:5432/bet_analisis",
                    "postgres", "hacker21");
            stmnt = connection.prepareStatement("SELECT id, hometeam, awayteam FROM public.active_matches where active=0;");
            ResultSet result= stmnt.executeQuery();
            while (result.next()) {
                home=result.getString("hometeam");
                away=result.getString("awayteam");
                id=result.getInt("id");
                System.out.println("Odd local Full: ");
                oddHF=sc.nextDouble();
                System.out.println("Odd draw Full: ");
                oddDF=sc.nextDouble();
                System.out.println("Odd away Full: ");
                oddAF=sc.nextDouble();
                System.out.println("Odd local Half: ");
                oddHH=sc.nextDouble();
                System.out.println("Odd draw Half: ");
                oddDH=sc.nextDouble();
                System.out.println("Odd away Half: ");
                oddAH=sc.nextDouble();
                //AÑADIR PROBABILIDADES DE BET365 A LAS LISTAS
                probHGF.add(1/oddHF);probHDF.add(1/oddDF);probHPF.add(1/oddAF);probAGF.add(1/oddAF);probADF.add(1/oddDF);probAPF.add(1/oddHF);
                probHGH.add(1/oddHH);probHDH.add(1/oddDH);probHPH.add(1/oddAH);probAGH.add(1/oddAH);probADH.add(1/oddDH);probAPH.add(1/oddHH);
                stmnt=connection.prepareStatement("SELECT \"scoreHomeHalf\", \"scoreHomeFull\", \"scoreAwayHalf\", \"scoreAwayFull\" FROM public.matches_2021 where hometeam="+home+";");
                res=stmnt.executeQuery();
                while(res.next()){
                    
                }
            }
            result.close();
            connection.close();
        } catch (Exception e) {

        }
    }
}

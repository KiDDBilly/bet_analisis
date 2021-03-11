public class Bet {
    private String equipos="";
    private String single="";
    private String combi="";
    private double probSingle=0.0;
    private double probCombi=0.0;
    private double oddSingle=0.0;
    private double oddCombi=0.0;

    public Bet(String equipos, String single, String combi, double probSingle, double probCombi, double oddSingle, double oddCombi) {
        this.equipos = equipos;
        this.single = single;
        this.combi = combi;
        this.probSingle = probSingle;
        this.probCombi = probCombi;
        this.oddSingle = oddSingle;
        this.oddCombi = oddCombi;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "equipos='" + equipos + '\'' +
                ", single='" + single + '\'' +
                ", combi='" + combi + '\'' +
                ", probSingle=" + probSingle +
                ", probCombi=" + probCombi +
                ", oddSingle=" + oddSingle +
                ", oddCombi=" + oddCombi +
                '}';
    }

    public String getEquipos() {
        return equipos;
    }

    public void setEquipos(String equipos) {
        this.equipos = equipos;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getCombi() {
        return combi;
    }

    public void setCombi(String combi) {
        this.combi = combi;
    }

    public double getProbSingle() {
        return probSingle;
    }

    public void setProbSingle(double probSingle) {
        this.probSingle = probSingle;
    }

    public double getProbCombi() {
        return probCombi;
    }

    public void setProbCombi(double probCombi) {
        this.probCombi = probCombi;
    }

    public double getOddSingle() {
        return oddSingle;
    }

    public void setOddSingle(double oddSingle) {
        this.oddSingle = oddSingle;
    }

    public double getOddCombi() {
        return oddCombi;
    }

    public void setOddCombi(double oddCombi) {
        this.oddCombi = oddCombi;
    }
}

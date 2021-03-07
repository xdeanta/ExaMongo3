

package pvehiculos2;

public class Vehiculos{
    String codveh;  	 
    String nomveh  ;		
    int prezoorixe ;	
    int anomatricula;  

    public Vehiculos() {
    }

    public Vehiculos(String codveh, String nomveh, Double prezoorixe, Double anomatricula) {
        double tprezo = prezoorixe;
        double tmatri= anomatricula;
        this.codveh = codveh;
        this.nomveh = nomveh;
        this.prezoorixe = (int)tprezo;
        this.anomatricula = (int)tmatri;
    }

    public String getCodveh() {
        return codveh;
    }

    public void setCodveh(String codveh) {
        this.codveh = codveh;
    }

    public String getNomveh() {
        return nomveh;
    }

    public void setNomveh(String nomveh) {
        this.nomveh = nomveh;
    }

    public int getPrezoorixe() {
        return prezoorixe;
    }

    public void setPrezoorixe(int prezoorixe) {
        this.prezoorixe = prezoorixe;
    }

    public int getAnomatricula() {
        return anomatricula;
    }

    public void setAnomatricula(int anomatricula) {
        this.anomatricula = anomatricula;
    }

    @Override
    public String toString() {
        return "Vehiculos{" + "codveh=" + codveh + ", nomveh=" + nomveh + ", prezoorixe=" + prezoorixe + ", anomatricula=" + anomatricula + '}';
    }
    
}

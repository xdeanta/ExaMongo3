/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pvehiculos2;

/**
 *
 * @author oracle
 */
public class Ventas {
    private int id;
    private String dni;
    private String codveh;
    private int tasa;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCodveh() {
        return codveh;
    }

    public void setCodveh(String codveh) {
        this.codveh = codveh;
    }

    public Ventas(Double id, String dni, String codveh, int tasa) {
        double tempid = id;
        this.id = (int)tempid;
        this.dni = dni;
        this.codveh = codveh;
        this.tasa=tasa;
    }

    public int getTasa() {
        return tasa;
    }

    public void setTasa(int tasa) {
        this.tasa = tasa;
    }
    
    
    
    public String toString(){
        return "{id:" + id + ", dni:" + dni + ", codveh:" + codveh + ", tasa:" + tasa +"}";
    }
    
}

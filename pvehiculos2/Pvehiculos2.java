/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pvehiculos2;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.bson.Document;

/**
 *
 * @author oracle
 */
public class Pvehiculos2 {
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static Connection conxOrcl;
    private static MongoClient mongoC;
    private static MongoDatabase mongoDB;

    /**
     * @param args the command line arguments
     */
    
    public static void getSQLConnection() throws SQLException{
        String usuario = "hr";
        String password = "hr";
        String host = "localhost"; 
        String puerto = "1521";
        String sid = "orcl";
        String url = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;
        
           
            conxOrcl = DriverManager.getConnection(url);
    }
    
    public static void connectMongoClient(){
        mongoC = new MongoClient("localhost");
    }
    
    public static void connectMongoDB(String name){
        mongoDB = mongoC.getDatabase(name);
    }
    
    public static MongoCollection<Document> getCollection(String name){
        return mongoDB.getCollection(name);
    }
    
    public static void getObjects(){
        emf = Persistence.createEntityManagerFactory("examen_files_2/finalveh.odb");
        em = emf.createEntityManager();
    }
    
    public static ArrayList<Vehiculos> getVehiculos(ArrayList<Ventas> ventas){
        MongoCollection<Document> docs = getCollection("vehiculos");
        ArrayList<Vehiculos> vehiculos = new ArrayList<>();
        Vehiculos veh;
        for(int i = 0; i < ventas.size(); i++){
            Document doc = docs.find(new Document("_id", ventas.get(i).getCodveh())).first();
            veh=new Vehiculos(doc.getString("_id"),doc.getString("nomveh"),doc.getDouble("prezoorixe"),doc.getDouble("anomatricula"));
            vehiculos.add(veh);
        }
        
        return vehiculos;
    }
    
    public static ArrayList<Clientes> getClientes(ArrayList<Ventas> ventas){
        MongoCollection<Document> docs = getCollection("clientes");
        ArrayList<Clientes> cli = new ArrayList<>();
        Clientes c;
        for(int i = 0; i < ventas.size(); i++){
            Document doc = docs.find(new Document("_id", ventas.get(i).getDni())).first();
            c=new Clientes(doc.getString("_id"),doc.getString("nomec"),doc.getDouble("ncompras"));
            cli.add(c);
        }
        
        return cli;
    }
    
    public static ArrayList<Ventas> getVentas(){
        ArrayList<Ventas> ventas = new ArrayList<>();
        Struct stx;
        Ventas v;
        BigDecimal temp;
        try{
            Statement st = conxOrcl.createStatement();
            ResultSet rs = st.executeQuery("select * from vendas");
            while(rs.next()){
                stx=(Struct)rs.getObject(3);
                Object[] campos = stx.getAttributes();
                temp = (BigDecimal)campos[1];
                v=new Ventas(rs.getDouble("id"),rs.getString("dni"),(String)campos[0],temp.intValue());
                ventas.add(v);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ventas;
    }
    
    public static void createObjectDB(ArrayList<Ventas> v, ArrayList<Clientes> cli, ArrayList<Vehiculos> veh){
        getObjects();
        Venfin finalfeh;
        String nombre;
        int pf=0;
        em.getTransaction().begin();
        for(Ventas sale : v){
            for(Clientes c : cli){
                if(sale.getDni().equals(c.getDni())){
                    nombre=c.getNomec();
                    for(Vehiculos ve: veh){
                        if(sale.getCodveh().equals(ve.getCodveh())){
                            if(c.getNcompras() > 0 ){
                                pf=ve.getPrezoorixe()-((2019-ve.getAnomatricula())*500)-500+sale.getTasa();
                            }else{
                                pf=ve.getPrezoorixe()-((2019-ve.getAnomatricula())*500)+sale.getTasa();
                            }
                            finalfeh = new Venfin((double)sale.getId(),sale.getDni(),c.getNomec(),ve.getNomveh(),(double)pf);
                            em.persist(finalfeh);
                        }
                    }
                }
            }
        }
        em.getTransaction().commit();
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<Ventas> ventas = new ArrayList<>();
        ArrayList<Clientes> cli = new ArrayList<>();
        ArrayList<Vehiculos> veh = new ArrayList<>();
        connectMongoClient();
        connectMongoDB("basevehiculos");
        try{
            getSQLConnection();
            ventas = getVentas();
            cli = getClientes(ventas);
            veh = getVehiculos(ventas);
            createObjectDB(ventas,cli,veh);
            /*System.out.println(cli);
            System.out.println(veh);*/
            
            conxOrcl.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        TypedQuery<Venfin> q1 = em.createQuery("SELECT v from Venfin v",Venfin.class);
        List<Venfin> res = q1.getResultList();
        for(Venfin p : res){
            System.out.println("Point id:" + p.getId());
            System.out.println("Point Atributos:" + p);
        }
        em.close();
        emf.close();
        /*ArrayList<Clientes> cli = new ArrayList<>();
        ArrayList<Vehiculos> veh = new ArrayList<>();
        connectMongoClient();
        connectMongoDB("basevehiculos");
        cli = getClientes();
        veh = getVehiculos();
        System.out.println(cli);
        System.out.println(veh);*/
        
    }
    
}

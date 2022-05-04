package rest.team4_server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Triptype;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Path("/triptype")
public class TripTypeResource {

    public TripTypeResource() {
        try {
            DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get all trips
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("gettriptypes")

    public String getTripTypes() {


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select t from Triptype t");
        List<Triptype> list = query.getResultList();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Triptype>>() {
        }.getType();

        entityManager.close();
        return gson.toJson(list, type);

    }

    //get selected trip
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("gettriptype/{ tripTypeId }")
    public String getTripType(@PathParam("tripTypeId") String tripTypeId) {


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Triptype ttp = entityManager.find(Triptype.class, tripTypeId);
        Gson gson = new Gson();
        entityManager.close();
        return gson.toJson(ttp);

    }

    //update triptype
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})// parameter so {}
    @Path("posttriptype")
    public String postTripType(String jsonString) { //data coming as string n then convert to Json
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();
        Triptype triptypeObject = gson.fromJson(jsonString, Triptype.class);
        entityManager.getTransaction().begin();
       Triptype mergeObject = entityManager.merge(triptypeObject);// mergeobject
        entityManager.getTransaction().commit();
        entityManager.close();
        return "{ 'message':'Update successful'}";
    }

//insert

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})// parameter so {}
    @Path("puttriptype")
    public String putTripType(String jsonString) { //data coming as string n then convert to Json
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();

        Triptype triptypeObject = gson.fromJson(jsonString, Triptype.class);
        entityManager.getTransaction().begin();

        entityManager.persist(triptypeObject);
        entityManager.getTransaction().commit();
        entityManager.close();
        return "{ 'message':'Insert successful'}";
    }

    //delete

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deletetriptype/{ tripTypeId }")
    public String deleteTripType(@PathParam("tripTypeId") String tripTypeId) {


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Triptype ttp = entityManager.find(Triptype.class, tripTypeId);//find

        String message="";

        if (ttp == null) {

            entityManager.close();
            message= "{ 'message':'Delete Failed'}";
        }
        else {
            entityManager.getTransaction().begin();
            entityManager.remove(ttp);
            entityManager.getTransaction().commit();
            message="{'message':Succesfully Deleted}";

        }

        return message;
    }
}

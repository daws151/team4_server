package rest.team4_server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Agent;
import model.Booking;
import model.Package;

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

@Path("/booking")
public class BookingResource {

    public BookingResource() {
        try {
            DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //get all bookings
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getBookings")

    public String getBookings() {


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select b from Booking b");
        List<Agent> list = query.getResultList();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Booking>>() {
        }.getType();

        entityManager.close();
        return gson.toJson(list, type);

    }

    //get selected booking
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getBooking/{ bookingId }")
    public String getBooking(@PathParam("bookingId") int bookingId) {


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Booking bk = entityManager.find(Booking.class, bookingId);
        Gson gson = new Gson();
        entityManager.close();
        return gson.toJson(bk);

    }

    //update booking

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})// parameter so {}
    @Path("postBooking")
    public String postBooking(String jsonString) { //data coming as string n then convert to Json
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();

        Booking bookingObject = gson.fromJson(jsonString, Booking.class);
        entityManager.getTransaction().begin();
        Booking mergeObject = entityManager.merge(bookingObject);

        entityManager.getTransaction().commit();
        entityManager.close();
        return "{ 'message':'Update successful'}";

    }

    //insert

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})// parameter so {}
    @Path("putBooking")
    public String putBooking(String jsonString) { //data comming as string n then convert to Json
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();

        Booking bookingObject = gson.fromJson(jsonString, Booking.class);
        entityManager.getTransaction().begin();
        entityManager.persist(bookingObject);
        entityManager.getTransaction().commit();
        entityManager.close();
        return "{ 'message':'Insert successful'}";
    }


    //delete

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteBooking/{ bookingId }")
    public String deleteBooking(@PathParam("bookingId") int bookingId) {


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Booking bk = entityManager.find(Booking.class, bookingId);//find

        String message="";

        if (bk == null) {

            entityManager.close();
            message= "{ 'message':'Delete Failed'}";
        }
        else {
            entityManager.getTransaction().begin();
            entityManager.remove(bk);
            entityManager.getTransaction().commit();
            message="{'message':Succesfully Deleted}";

        }

        return message;
    }

}
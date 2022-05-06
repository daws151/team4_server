package rest.team4_server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Customer;
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

@Path("/customer")
public class CustomerResource {

    public CustomerResource() {
        try {
            DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //get all customer
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getcustomers")

    public String getCustomers()
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("select c from Customer c");
        List<Customer> list = query.getResultList();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Customer>>() {
        }.getType();

        entityManager.close();
        return gson.toJson(list, type);

    }



    //get selected customer
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getcustomer/{CustomerId}")
    public String getCustomer(@PathParam("CustomerId") int customerId)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Customer cus = entityManager.find(Customer.class, customerId);
        Gson gson = new Gson();

        entityManager.close();
        return gson.toJson(cus);

    }

    //update customer

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})// parameter so {}
    @Path("postcustomer")
    public String postCustomer(String jsonString) { //data coming as string n then convert to Json
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();
        Customer customerObject = gson.fromJson(jsonString, Customer.class);
        entityManager.getTransaction().begin();
        Customer mergeObject = entityManager.merge(customerObject);

        entityManager.getTransaction().commit();
        entityManager.close();
        return "{ 'message':'Update successful'}";

    }


    //insert

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})// parameter so {}
    @Path("putcustomer")
    public String putCustomer(String jsonString) { //data coming as string n then convert to Json

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();

        Customer customerObject = gson.fromJson(jsonString, Customer.class);
        entityManager.getTransaction().begin();
        entityManager.persist(customerObject);
        entityManager.getTransaction().commit();
        entityManager.close();
        return "{ 'message':'Insert successful'}";
    }


    //delete

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deletecustomer/{CustomerId}")
    public String deleteCustomer(@PathParam("CustomerId") int customerId) {


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Customer cus = entityManager.find(Customer.class, customerId);//find
        String message="";

        if (cus == null) {

            entityManager.close();
            message= "{ 'message':'Delete Failed'}";
        }
        else {
            entityManager.getTransaction().begin();
            entityManager.remove(cus);
            entityManager.getTransaction().commit();
            message="{'message':Successfully Deleted}";

        }

        return message;
    }

}

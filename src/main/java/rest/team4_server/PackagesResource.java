package rest.team4_server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Package;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Path("/package")
public class PackagesResource {
    public void PackageResource() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public void gsonhandler() {
//        GsonBuilder gsonBuilder = new ;
//
//    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getpackages")
    public String getPackages() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select p from Package p");
        List<Package> list = query.getResultList();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Type type = new TypeToken<List<Package>>(){}.getType();
        return gson.toJson(list,type);
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getpackage/{PackageId}")
    public String getPackage(@PathParam("PackageId") int pkgId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Package pkg = entityManager.find(Package.class, pkgId);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(pkg);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("postpackage")
    public String postPackage(String jsonString) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Package packageObject = gson.fromJson(jsonString, Package.class);
        entityManager.getTransaction().begin();
        Package mergedObject = entityManager.merge(packageObject);
        entityManager.getTransaction().commit();
        entityManager.close();
        return "{\"message\": \"Package was updated\"}";
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("insertpackage")
    public String putPackage(String jsonString) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Package packageObject = gson.fromJson(jsonString, Package.class);
        entityManager.getTransaction().begin();
        entityManager.persist(packageObject);
        entityManager.getTransaction().commit();
        entityManager.close();
        return "{\"message\": \"Package was inserted\"}";
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deletepackage/{PackageId}")
    public String deletePackage(@PathParam("PackageId") int pkgId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Package pkg = entityManager.find(Package.class, pkgId);
        String message;
        if (pkg == null) {
            message = "{'message': 'Package not found'}";
        } else {
            entityManager.getTransaction().begin();
            entityManager.remove(pkg);
            entityManager.getTransaction().commit();
            message = "{\"message\": \"Package was deleted\"}";
        }
        return message;
    }
}
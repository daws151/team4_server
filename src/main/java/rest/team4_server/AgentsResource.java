package rest.team4_server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Agent;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Path("/agent")
public class AgentsResource {
    public void AgentResource() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    // experimenting with a void method and Persistence context annotation. might fix the problem I'm having
@PersistenceContext
EntityManager em;
    public void findAgent(int agtId) {
        Agent agent = em.find(Agent.class, agtId);
        agent.getId();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getagents")
    public String getAgents() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select a from Agent a");
        List<Agent> list = query.getResultList();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Agent>>(){}.getType();
        return gson.toJson(list, type);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getagent/{agtId}")
    public String getAgent(@PathParam("agtId") int agtId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Agent agt = entityManager.find(Agent.class, agtId);
        Gson gson = new Gson();
//        Type type = new TypeToken<List<Agent>>() {
//        }.getType();
        return gson.toJson(agt);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("postagent")
    public String postAgent(String jsonString) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Gson gson = new Gson();
        Agent agentObject = gson.fromJson(jsonString, Agent.class);
        entityManager.getTransaction().begin();
        Agent mergedObject = entityManager.merge(agentObject);
        entityManager.getTransaction().commit();
        entityManager.close();
        return "{'message': 'Agent was updated'}";
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON}) // MAKE SURE YOU ADD CURLY BRACES WITHIN THIS ANNOTATION
    @Produces(MediaType.APPLICATION_JSON)
    @Path("insertagent")
    public String putAgent(String jsonString) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Gson gson = new Gson();
        Agent agentObject = gson.fromJson(jsonString, Agent.class);
        entityManager.getTransaction().begin();
        entityManager.persist(agentObject);
        entityManager.getTransaction().commit();
        entityManager.close();
        return "{'message': 'New agent was inserted'}";
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteagent/{agtId}")
    public String deleteAgent(@PathParam("agtId") int agtId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Agent agt = entityManager.find(Agent.class, agtId);
        String message;
        if (agt == null) {
            message = "{'message': 'Agent not found'}";
        } else {
            entityManager.getTransaction().begin();
            entityManager.remove(agt);
            entityManager.getTransaction().commit();
            message = "{'message': 'Agent was deleted'}";
        }
        return message;
    }
}
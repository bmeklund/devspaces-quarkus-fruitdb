package com.demo;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import io.quarkus.panache.common.Sort;

@Path("entity/fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitResource {

    @GET
    public List<Fruit> get() {
        return Fruit.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Fruit getSingle(Long id) {
        Fruit entity = Fruit.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Fruit fruit) {
        if (fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        fruit.persist();
        return Response.ok(fruit).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Fruit update(Long id, Fruit fruit) {
        if (fruit.name == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        Fruit entity = Fruit.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.name = fruit.name;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
        Fruit entity = Fruit.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

}

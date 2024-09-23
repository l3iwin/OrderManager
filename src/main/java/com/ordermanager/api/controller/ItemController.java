package com.ordermanager.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ordermanager.api.entity.Item;
import com.ordermanager.api.service.ItemService;

import jakarta.inject.Inject;

@Path("/item")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ItemController {

    @Inject
    private ItemService itemService;

    @POST
    public Response createItem(Item item) {
        itemService.createItem(item);
        return Response.ok(item).build();
    }

    @GET
    @Path("/{id}")
    public Response getItem(@PathParam("id") Long id) {
        Item item = itemService.getItem(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(item).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItem(Long itemId){
        itemService.deleteItem(itemId);
        return Response.ok(itemId).build();
    }

    @PUT
    public Response updateItem(Item item){
        itemService.updateItem(item);
        return Response.ok(item).build();
    }
}


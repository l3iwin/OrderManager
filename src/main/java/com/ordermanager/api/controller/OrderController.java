package com.ordermanager.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ordermanager.api.entity.Order;
import com.ordermanager.api.service.OrderService;

import jakarta.inject.Inject;
import jakarta.websocket.server.PathParam;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    private OrderService orderService;

    @POST
    public Response createOrder(Order order) {
        orderService.createOrder(order);
        return Response.ok(order).build();
    }

    @GET
    @Path("/{id}")
    public Response getOrder(@PathParam("id") Long id) {
        Order order = orderService.getOrder(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(order).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") Long orderId){
        orderService.deleteOrder(orderId);
        return Response.ok(orderId).build();
    }

    @PUT
    public Response updateOrder(Order order){
        orderService.updateOrder(order);
        return Response.ok(order).build();
    }
}

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

import com.ordermanager.api.entity.Stock;
import com.ordermanager.api.service.StockService;

import jakarta.inject.Inject;

@Path("/stock")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StockController {

    @Inject
    private StockService stockService;

    @POST
    public Response createStock(Stock stock) {
        stockService.createStock(stock);
        return Response.ok(stock).build();
    }

    @GET
    @Path("/{id}")
    public Response getStock(@PathParam("id") Long id) {
        Stock stock = stockService.getStock(id);
        if (stock == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(stock).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteStock(@PathParam("id") Long stockId){
        stockService.deleteStock(stockId);
        return Response.ok(stockId).build();
    }

    @PUT
    public Response updateStock(Stock stock){
        stockService.updateStock(stock);
        return Response.ok(stock).build();
    }
}


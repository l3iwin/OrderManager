package com.ordermanager.api.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ordermanager.api.entity.Item;
import com.ordermanager.api.entity.Order;

import jakarta.ejb.Stateless;

@Stateless
public class OrderRepository{

    @PersistenceContext
    private EntityManager entityManager;


    public void create(Order order) {
        entityManager.persist(order);
    }    

    public Order findById(Long id) {
        return entityManager.find(Order.class, id);
    }

    public void update(Order order) {
        entityManager.merge(order);
    }

    public void delete(Order order) {
        entityManager.remove(order);
    }

    public List<Order> findAllOrderByIsNotCompleteAndItemOrderByCreationDateDesc(Item item){
        String queryStr = "SELECT o FROM ORDER o WHERE o.isComplete is false and o.item.id = :itemId ORDER BY o.creationDate desc";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("itemId", item.getId());
        return query.getResultList();
    }

}


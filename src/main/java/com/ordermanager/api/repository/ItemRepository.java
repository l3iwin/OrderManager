package com.ordermanager.api.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ordermanager.api.entity.Item;

import jakarta.ejb.Stateless;

@Stateless
public class ItemRepository{

    @PersistenceContext
    private EntityManager entityManager;


    public void create(Item item) {
        entityManager.persist(item);
    }    

    public Item findById(Long id) {
        return entityManager.find(Item.class, id);
    }

    public void update(Item item) {
        entityManager.merge(item);
    }

    public void delete(Item item) {
        entityManager.remove(item);
    }

}

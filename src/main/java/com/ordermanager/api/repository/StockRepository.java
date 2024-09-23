package com.ordermanager.api.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ordermanager.api.entity.Item;
import com.ordermanager.api.entity.Stock;

import jakarta.ejb.Stateless;

@Stateless
public class StockRepository{

    @PersistenceContext
    private EntityManager entityManager;


    public void create(Stock stock) {
        entityManager.persist(stock);
    }    

    public Stock findById(Long id) {
        return entityManager.find(Stock.class, id);
    }

    public void update(Stock stock) {
        entityManager.merge(stock);
    }

    public void delete(Stock stock) {
        entityManager.remove(stock);
    }
    
    public Stock findByItem(Item item){
        String queryStr = "SELECT s FROM STOCK s WHERE s.item.id = :itemId";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("itemId", item.getId());
        return (Stock) query.getSingleResult();
    }
	

}

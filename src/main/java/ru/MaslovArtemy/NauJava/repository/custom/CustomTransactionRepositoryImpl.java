package ru.MaslovArtemy.NauJava.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Date;
import java.util.List;

@Repository
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {

    private final EntityManager entityManager;

    @Autowired
    public CustomTransactionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Transaction> findTransactionsByDateAndUser(Date date, User user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);

        Root<Transaction> transaction = cq.from(Transaction.class);

        Predicate datePredicate = cb.equal(transaction.get("date"), date);
        Predicate userPredicate = cb.equal(transaction.get("user"), user);

        cq.where(cb.and(datePredicate, userPredicate));
        
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Transaction> findTransactionsByCategory(Category category) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
        Root<Transaction> transaction = cq.from(Transaction.class);

        Predicate categoryPredicate = cb.equal(transaction.get("category"), category);

        cq.where(categoryPredicate);

        return entityManager.createQuery(cq).getResultList();
    }
}

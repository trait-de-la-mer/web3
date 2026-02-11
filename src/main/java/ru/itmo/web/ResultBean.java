package ru.itmo.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ru.itmo.web.model.HitResult;

import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class ResultBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "hitResultsPU")
    private EntityManager entityManager;

    @Transactional
    public void saveResult(HitResult result) {
        try {
            // Просто сохраняем - транзакция управляется аннотацией @Transactional
            entityManager.persist(result);

            System.out.println("Результат сохранён: ID = " + result.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка сохранения данных в базу", e);
        }
    }

    /**
     * Загружает все результаты из базы данных
     */
    public List<HitResult> getAllResults() {
        try {
            TypedQuery<HitResult> query = entityManager.createQuery(
                    "SELECT h FROM HitResult h ORDER BY h.createdAt DESC",
                    HitResult.class
            );
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public void clearAllResults() {
        try {
            entityManager.createQuery("DELETE FROM HitResult").executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка очистки базы данных", e);
        }
    }
}
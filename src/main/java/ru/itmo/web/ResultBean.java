package ru.itmo.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ru.itmo.web.model.HitResult;

import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class ResultBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "hitResultsPU")
    private EntityManager entityManager;

    /**
     * Сохраняет результат в базу данных
     */
    public void saveResult(HitResult result) {
        try {
            // Начинаем транзакцию
            entityManager.getTransaction().begin();

            // Сохраняем объект
            entityManager.persist(result);

            // Фиксируем транзакцию
            entityManager.getTransaction().commit();

            System.out.println("Результат сохранён: ID = " + result.getId());
        } catch (Exception e) {
            // Откатываем транзакцию при ошибке
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
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

    /**
     * Очищает все результаты из базы данных
     */
    public void clearAllResults() {
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM HitResult").executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка очистки базы данных", e);
        }
    }
}
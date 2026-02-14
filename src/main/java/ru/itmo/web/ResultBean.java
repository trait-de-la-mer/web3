package ru.itmo.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.UserTransaction;
import jakarta.annotation.Resource;
import ru.itmo.web.model.HitResult;

import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class ResultBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "hitResultsPU")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    public void saveResult(HitResult result) {
        try {
            // Начинаем транзакцию вручную
            userTransaction.begin();

            // Сохраняем результат
            entityManager.persist(result);

            // Завершаем транзакцию
            userTransaction.commit();

            System.out.println("Результат сохранён успешно");

        } catch (Exception e) {
            // В случае ошибки откатываем транзакцию
            try {
                if (userTransaction.getStatus() == jakarta.transaction.Status.STATUS_ACTIVE ||
                        userTransaction.getStatus() == jakarta.transaction.Status.STATUS_MARKED_ROLLBACK) {
                    userTransaction.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }

            e.printStackTrace();
            throw new RuntimeException("Ошибка сохранения данных в базу: " + e.getMessage(), e);
        }
    }

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

    public void clearAllResults() {
        try {
            userTransaction.begin();
            entityManager.createQuery("DELETE FROM HitResult").executeUpdate();
            userTransaction.commit();

            System.out.println("Все результаты очищены");

        } catch (Exception e) {
            try {
                if (userTransaction.getStatus() == jakarta.transaction.Status.STATUS_ACTIVE ||
                        userTransaction.getStatus() == jakarta.transaction.Status.STATUS_MARKED_ROLLBACK) {
                    userTransaction.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }

            throw new RuntimeException("Ошибка очистки базы данных: " + e.getMessage(), e);
        }
    }
}
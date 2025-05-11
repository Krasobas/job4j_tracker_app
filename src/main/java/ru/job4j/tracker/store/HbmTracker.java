package ru.job4j.tracker.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.model.Item;

import java.util.Collections;
import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    private static final Logger LOG = LoggerFactory.getLogger(HbmTracker.class.getName());
    private static final String TABLE_NAME = "items";

    @Override
    public Item add(Item item) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Impossible to add an item to table {}.", TABLE_NAME, e);
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            int updated = session.createQuery(
                            "UPDATE Item SET name = :fName, created = :fCreated WHERE id = :fId"
                    ).setParameter("fName", item.getName())
                    .setParameter("fCreated", item.getCreated())
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return updated > 0;
        } catch (Exception e) {
            LOG.error("Impossible to rename item with id = {} in table {}.", id, TABLE_NAME, e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            int deleted = session.createQuery(
                            "DELETE Item WHERE id = :fId"
                    ).setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return deleted > 0;
        } catch (Exception e) {
            LOG.error("Impossible to delete item with id = {} from table {}.", id, TABLE_NAME, e);
        }
        return false;
    }

    @Override
    public List<Item> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Item", Item.class).list();
        } catch (Exception e) {
            LOG.error("Impossible to get all items from table {}.", TABLE_NAME, e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Item> findByName(String key) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Item WHERE name LIKE :fKey", Item.class)
                    .setParameter("fKey", String.format("%%%s%%", key))
                    .list();
        } catch (Exception e) {
            LOG.error("Impossible to find item with name = {} in table {}.", key, TABLE_NAME, e);
        }
        return Collections.emptyList();
    }

    @Override
    public Item findById(int id) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Item WHERE id = :fId", Item.class)
                    .setParameter("fId", id)
                    .uniqueResult();
        } catch (Exception e) {
            LOG.error("Impossible to find item with id = {} in table {}.", id, TABLE_NAME, e);
        }
        return null;
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}

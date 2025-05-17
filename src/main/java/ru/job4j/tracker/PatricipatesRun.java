package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.toone.Role;
import ru.job4j.tracker.toone.User;
import ru.job4j.tracker.toone.UserMessenger;

import java.util.Collections;
import java.util.List;

public class PatricipatesRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            var role = new Role();
            role.setName("ADMIN");
            create(role, sf);
            var user = new User();
            user.setName("Admin Admin");
            user.setMessengers(List.of(
                    new UserMessenger(0, "tg", "@tg"),
                    new UserMessenger(0, "wu", "@wu")
            ));
            user.setRole(role);
            create(user, sf);
            var item = new Item();
            item.setName("Learn Hibernate");
            item.setParticipates(List.of(user));
            create(item, sf);
            sf.openSession()
                    .createQuery("from Item where id = :fId", Item.class)
                    .setParameter("fId", item.getId())
                    .getSingleResult()
                    .getParticipates()
                    .forEach(System.out::println);

            delete(item.getId(), sf);
            findAll(sf).forEach(System.out::println);
            findUserById(user.getId(), sf);
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static <T> T create(T model, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.persist(model);
        session.getTransaction().commit();
        session.close();
        return model;
    }

    public static void update(Item item, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }

    public static void delete(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = new Item();
        item.setId(id);
        session.delete(item);
        session.getTransaction().commit();
        session.close();
    }

    public static List<Item> findAll(SessionFactory sf) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            List result = session.createQuery("from ru.job4j.tracker.Item").list();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    public static Item findById(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public static User findUserById(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        User result = session.get(User.class, id);
        System.out.println(result);
        session.getTransaction().commit();
        session.close();
        return result;
    }
}

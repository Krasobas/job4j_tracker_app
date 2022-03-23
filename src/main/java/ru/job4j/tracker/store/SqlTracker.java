package ru.job4j.tracker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.model.Item;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(SqlTracker.class.getName());
    private static final String TABLE_NAME = "items";
    private Connection cn;
    private boolean tableExists;

    public SqlTracker() {
        init();
        checkTable();
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
        checkTable();
    }

    private void init() {
        try (InputStream in = SqlTracker.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (IOException | ClassNotFoundException | SQLException e) {
            LOG.error("Impossible to connect the database.", e);
        }
    }

    private void checkTable() {
        try (Statement st = cn.createStatement()) {
            String sql = String.format("select exists (select 1 from information_schema.columns "
                            + "where table_name = '%s' and "
                            + "(column_name = '%s' or column_name = '%s' or column_name = '%s'));",
                    TABLE_NAME,
                    "id",
                    "name",
                    "created");
            try (ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    tableExists = rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            LOG.error("Impossible to check if table {} exists.", TABLE_NAME, e);
        }
    }

    private void createTable() {
        try (Statement st = cn.createStatement()) {
            String sql = String.format("create table if not exists %s(%s, %s, %s);",
                    TABLE_NAME,
                    "id serial primary key",
                    "name text",
                    "created timestamp");
            tableExists = st.executeUpdate(sql) == 0;
        } catch (SQLException e) {
            LOG.error("Impossible to create a table with name {}.", TABLE_NAME, e);
        }
    }

    private Item createItem(ResultSet rs) throws SQLException {
        Item rsl = new Item();
        rsl.setId(rs.getInt("id"));
        rsl.setName(rs.getString("name"));
        rsl.setCreated(rs.getTimestamp("created").toLocalDateTime());
        return rsl;
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) {
        if (!tableExists) {
            createTable();
        }
        String sql = String.format("insert into %s(name, created) values (?, ?);", TABLE_NAME);
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            ps.execute();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Impossible to add an item to table {}.", TABLE_NAME, e);
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        if (!tableExists) {
            return false;
        }
        boolean rsl = false;
        String sql = String.format("update %s set name = ?, created = ? where id = ?;", TABLE_NAME);
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            ps.setInt(3, id);
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Impossible to rename item with id = {} in table {}.", id, TABLE_NAME, e);
        }
        return rsl;
    }

    @Override
    public boolean delete(int id) {
        if (!tableExists) {
            return false;
        }
        boolean rsl = false;
        String sql = String.format("delete from %s where id = ?;", TABLE_NAME);
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Impossible to delete item with id = {} from table {}.", id, TABLE_NAME, e);
        }
        return rsl;
    }

    @Override
    public List<Item> findAll() {
        if (!tableExists) {
            return Collections.emptyList();
        }
        List<Item> items = new ArrayList<>();
        String sql = String.format("select * from %s", TABLE_NAME);
        try (Statement st = cn.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    items.add(createItem(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Impossible to get all items from table {}.", TABLE_NAME, e);
        }
        return items;
    }

    @Override
    public List<Item> findByName(String key) {
        if (!tableExists) {
            return Collections.emptyList();
        }
        List<Item> items = new ArrayList<>();
        String sql = String.format("select * from %s where name = ?;", TABLE_NAME);
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(createItem(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Impossible to find item with name = {} in table {}.", key, TABLE_NAME, e);
        }
        return items;
    }

    @Override
    public Item findById(int id) {
        if (!tableExists) {
            return null;
        }
        Item rsl = null;
        String sql = String.format("select * from %s where id = ?;", TABLE_NAME);
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rsl = createItem(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Impossible to find item with id = {} in table {}.", id, TABLE_NAME, e);
        }
        return rsl;
    }
}

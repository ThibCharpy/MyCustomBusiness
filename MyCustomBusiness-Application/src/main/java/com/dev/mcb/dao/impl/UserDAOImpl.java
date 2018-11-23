package com.dev.mcb.dao.impl;

import com.dev.mcb.core.User;
import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private Session session;

    private final static Logger logger = Logger.getLogger(UserDAOImpl.class);

    @Override
    public void createUser(User user) {
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.session.beginTransaction();
            this.session.save(user);
            this.session.getTransaction().commit();
            logger.info("createUser SUCCESS ! " + user.toString());
        } catch (Exception e) {
            if (null != this.session.getTransaction()) {
                logger.info("createUser ERROR --> ROLLBACK !");
                this.session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (null != this.session) {
                this.session.close();
            }
        }
    }

    @Override
    public User readUser(Long user_id) {
        User searchedUser = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.session.beginTransaction();
            searchedUser = this.session.load(User.class, user_id);
            logger.info("readUser SUCCESS ! " + searchedUser.toString());
        } catch (Exception e) {
            if (null != this.session.getTransaction()) {
                logger.info("readUser ERROR !");
                this.session.getTransaction().rollback();
            }
            e.printStackTrace();
        }
        return searchedUser;
    }

    @Override
    public boolean updateUser(User user) {

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.session.beginTransaction();

            User userObj = this.session.get(User.class, user.getId());
            userObj.setUsername(user.getUsername());
            userObj.setPassword(user.getPassword());

            this.session.getTransaction().commit();
            logger.info("updateUser with id "+ user.getId() + " SUCCESS ! " + user.toString());
        } catch (Exception e) {
            if (null != this.session.getTransaction()) {
                logger.info("updateUser with id "+ user.getId() + " ERROR --> ROLLBACK !");
                this.session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (null != this.session) {
                this.session.close();
            }
        }

        return true;
    }

    @Override
    public boolean deleteUser(User user) {
        Long user_id = user.getId();

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.session.beginTransaction();

            this.session.delete(user);

            this.session.getTransaction().commit();
            logger.info("deleteUser with id "+ user_id + " SUCCESS ! ");
        } catch (Exception e) {
            if (null != this.session.getTransaction()) {
                logger.info("deleteUser with id "+ user_id + " ERROR --> ROLLBACK !");
                this.session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (null != this.session) {
                this.session.close();
            }
        }

        return true;
    }

    @Override
    public List listUser() {
        List users = new ArrayList<User>();
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.session.beginTransaction();

            //users = this.session.createQuery("FROM User ").list();
            logger.info("listCustomer SUCCESS ! size =" + users.size());
        } catch (Exception e) {
            if (null != this.session.getTransaction()) {
                logger.info("listCustomer ERROR --> ROLLBACK !");
                this.session.getTransaction().rollback();
            }
            e.printStackTrace();
        }

        List<User> mappedUsers = new ArrayList<>();
        for (Object user : users) {
            mappedUsers.add( (User) user);
        }

        return mappedUsers;
    }
}

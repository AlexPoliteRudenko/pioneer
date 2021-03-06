package com.elesson.pioneer.service;


import com.elesson.pioneer.dao.BaseDao;
import com.elesson.pioneer.dao.DaoFactory;
import com.elesson.pioneer.model.User;
import com.elesson.pioneer.service.util.UserCache;

import java.util.List;

import static com.elesson.pioneer.service.util.Security.encrypt;
import static com.elesson.pioneer.service.util.ServiceValidation.checkNotFound;
import static com.elesson.pioneer.service.util.ServiceValidation.checkNotFoundWithId;

/**
 * Provides implementation of all {@code UserService} interface methods.
 * All modification operations invalidate cached collection of users.
 */
public class UserServiceImpl implements UserService {

    private BaseDao userDao;
    private static volatile UserService service;

    private UserServiceImpl() {
        userDao = DaoFactory.getDao(DaoFactory.DaoType.USER);
    }

    public static UserService getUserService() {
        if(service ==null) {
            synchronized (UserServiceImpl.class) {
                if(service ==null) {
                    service = new UserServiceImpl();
                }
            }
        }
        return service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(User user) {
        checkNotFound(user, "user must not be null");
        user.setPassword(encrypt(user.getPassword()));
        checkNotFound(userDao.save(user), "user must not be null");
        UserCache.invalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(int id) {
        checkNotFoundWithId(userDao.delete(id), id);
        UserCache.invalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User get(int id) {
        return checkNotFoundWithId(userDao.getById(id), id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getByEmail(String email) {
        checkNotFound(email, "email must not be null");
        return checkNotFound(userDao.getByValue(email), "email=" + email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(User user) {
        checkNotFound(user, "user must not be null");
        if(!user.getPassword().isEmpty()) {
            user.setPassword(encrypt(user.getPassword()));
        }
        checkNotFoundWithId(userDao.save(user), user.getId());
        UserCache.invalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }
}

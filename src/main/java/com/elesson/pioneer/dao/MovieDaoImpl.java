package com.elesson.pioneer.dao;

import com.elesson.pioneer.model.Entity;
import com.elesson.pioneer.model.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

/**
 * This class provides implementation of all {@code UserDao} interface methods.
 */
public class MovieDaoImpl implements BaseDao {

    private static final Logger logger = LogManager.getLogger(MovieDaoImpl.class);

    private JDBCDao simpleDao = new JDBCDaoImpl();

    private static volatile MovieDaoImpl movieDao;

    private MovieDaoImpl() {}

    public static MovieDaoImpl getMovieDao() {
        if(movieDao ==null) {
            synchronized (MovieDaoImpl.class) {
                if(movieDao ==null) {
                    movieDao = new MovieDaoImpl();
                }
            }
        }
        if(logger.isDebugEnabled()) logger.debug("MovieDaoImpl received");
        return movieDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> getAll() {
        String query = "SELECT * FROM movies m";
        return simpleDao.getAllById(Movie.class, query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie save(Entity entity) {
        Movie movie = (Movie)entity;
        if(movie.isNew()) {
            String query = "INSERT INTO movies (name, genre, duration, year, active) VALUES (?, ?, ?, ?, ?)";
            return simpleDao.save(movie, query, movie.getName(), movie.getGenre(), movie.getDuration(), movie.getYear(), movie.isActive());
        } else {
            String query = "UPDATE movies SET name=?, genre=?, duration=?, year=?, active=? WHERE mid=?";
            return simpleDao.update(movie, query, movie.getName(), movie.getGenre(), movie.getDuration(), movie.getYear(), movie.isActive(), movie.getId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM movies WHERE mid=?;";
        return simpleDao.delete(query, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie getById(int id) {
        String query = "SELECT * FROM movies m WHERE mid=?";
        return simpleDao.getById(Movie.class, query, id);
    }

    //not implemented
    @Override
    public List<Movie> getAllByDate(LocalDate date) {
        return null;
    }

    //not implemented
    @Override
    public Movie getByValue(String value) {
        return null;
    }
}

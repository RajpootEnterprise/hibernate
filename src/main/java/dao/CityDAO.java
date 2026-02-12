package dao;

import entity.City;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class CityDAO {
    public List<City> getAllCities() {
        List<City> cities = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            cities = session.createSelectionQuery("from City", City.class).getResultList();

            session.getTransaction().commit();
        } catch (Exception e) {
              e.printStackTrace();
        }

        return cities != null ? cities : new ArrayList<>();
    }

    public City getCityById(int id) {
        City city = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            city= session.find(City.class, id);

            session.getTransaction().commit();
        } catch (Exception e) {
              e.printStackTrace();
        }

        return city != null ? city : new City();
    }

    public List<City> getCitiesByName(String cityName) {
        List<City> cities = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            cities = session.createSelectionQuery("from City where name = :name", City.class)
                    .setParameter("name", cityName) // <--- Added this line
                    .getResultList();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities != null ? cities : new ArrayList<>();
    }

    public void saveCity(City city) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(city);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Clean up on failure
            e.printStackTrace();
        }
    }

    public void deleteCity(City city) {
        if (city == null) return;
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            City temp = session.find(City.class, city.getId());
            if (temp != null) {
                session.remove(temp);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void updateCity(City city) {
        if (city == null) return;
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            City existing = session.find(City.class, city.getId());
            if (existing != null) {
                existing.setName(city.getName());
                existing.setDescription(city.getDescription());
                existing.setPopulation(city.getPopulation());
                existing.setCountry(city.getCountry());
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
package service;

import dao.CityDAO;
import entity.City;
import java.util.List;

public class CityService {
    private final CityDAO cityDAO = new CityDAO();

    public List<City> getAllCities() {
        return cityDAO.getAllCities();
    }

    public City getCity(int id) {
        return cityDAO.getCityById(id);
    }

    public List<City> getCityByName(String name) {
        return cityDAO.getCitiesByName(name);
    }

    public void saveCity(City city) {
        // Business Logic: e.g., ensure population isn't negative
        if (city.getPopulation() < 0) city.setPopulation(0);
        cityDAO.saveCity(city);
    }

    public void deleteCity(int id) {
        City city = cityDAO.getCityById(id);
        if (city != null) {
            cityDAO.deleteCity(city);
        }
    }

    public void updateCity(City city) {
        cityDAO.updateCity(city);
    }
}
package controller;

import entity.City;
import service.CityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/cities/*")
public class CityController extends HttpServlet {
    private CityService cityService;

    @Override
    public void init() {
        cityService = new CityService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            switch (action == null ? "list" : action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertCity(request, response);
                    break;
                case "/delete":
                    deleteCity(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateCity(request, response);
                    break;
                default:
                    listCities(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listCities(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<City> listCity = cityService.getAllCities();
        request.setAttribute("listCity", listCity);
        request.getRequestDispatcher("/city-list.jsp").forward(request, response);
    }

    private void insertCity(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        int population = Integer.parseInt(request.getParameter("population"));
        String description = request.getParameter("description");

        City newCity = new City(name, description, population, country);
        cityService.saveCity(newCity);
        response.sendRedirect("list");
    }

    private void deleteCity(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        cityService.deleteCity(id);
        response.sendRedirect("list");
    }

    private void updateCity(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id")); // Get ID from hidden field
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        int population = Integer.parseInt(request.getParameter("population"));
        String description = request.getParameter("description");

        City cityToUpdate = new City(name, description, population, country);
        cityToUpdate.setId(id);

        cityService.updateCity(cityToUpdate);
        response.sendRedirect("list");
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Just forward to the form page; no data needed for a blank form
        request.getRequestDispatcher("/city-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        // Fetch the existing data so the form can be "pre-filled"
        City existingCity = cityService.getCity(id);

        request.setAttribute("city", existingCity);
        request.getRequestDispatcher("/city-form.jsp").forward(request, response);
    }
}
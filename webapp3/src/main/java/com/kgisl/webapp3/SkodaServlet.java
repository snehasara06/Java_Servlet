
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// @WebServlet(urlPatterns = "/", initParams = {
//         @WebInitParam(name = "jdbcURL", value = "jdbc:mysql://localhost:3306/cars"),
//         @WebInitParam(name = "jdbcUsername", value = "root"),
//         @WebInitParam(name = "jdbcPassword", value = "")
// })
public class SkodaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CarsDAO carsDAO;

    // public void init() throws ServletException {
    //     String jdbcURL = getInitParameter("jdbcURL");
    //     String jdbcUsername = getInitParameter("jdbcUsername");
    //     String jdbcPassword = getInitParameter("jdbcPassword");

    //     carsDAO = new CarsDAO(jdbcURL, jdbcUsername, jdbcPassword);

    // }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
    
        try {
          
            switch (action) {
                case "/new":
                    System.out.println("New ");
                    newCar(request, response);
                    break;
                case "/insert":
                    insertCar(request, response);
                    break;
                case "/delete":
                    deleteCar(request, response);
                    break;
                case "/edit":
                    editCar(request, response);
                    break;
                case "/update":
                    updateCar(request, response);
                    break;
                default:
                    listCars(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listCars(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Cars> listCars = carsDAO.listAllCars();
        request.setAttribute("listCars", listCars);
        RequestDispatcher dispatcher = request.getRequestDispatcher("carList.jsp");
        dispatcher.forward(request, response);
    }

    private void newCar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("carForm.jsp");
        dispatcher.forward(request, response);
    }

    private void editCar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cars existingCar = carsDAO.getCar(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("carForm.jsp");
        request.setAttribute("car", existingCar);
        dispatcher.forward(request, response);

    }

    private void insertCar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String fuel = request.getParameter("fuel");
        String price = request.getParameter("price");
        int seater = Integer.parseInt(request.getParameter("seater"));

        Cars newBook = new Cars(name, type, fuel, price, seater);
        carsDAO.insertCar(newBook);
        response.sendRedirect("listCars");
    }

    private void updateCar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String fuel = request.getParameter("fuel");
        String price = request.getParameter("price");
        int seater = Integer.parseInt(request.getParameter("seater"));

        Cars car = new Cars(id, name, type, fuel, price, seater);
        carsDAO.updateCar(car);
        response.sendRedirect("listCars");
    }

    private void deleteCar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        Cars book = new Cars(id);
        carsDAO.deleteCar(book);
        response.sendRedirect("listCars");

    }
}
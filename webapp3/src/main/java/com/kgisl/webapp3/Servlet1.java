import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet(urlPatterns = "/", initParams = {
        @WebInitParam(name = "jdbcURL", value = "jdbc:mysql://localhost:3306/cars"),
        @WebInitParam(name = "jdbcUsername", value = "root"),
        @WebInitParam(name = "jdbcPassword", value = "")
})



public class Servlet1 extends HttpServlet {
    private CarsDAO carsDAO;
    
    public void init() throws ServletException {
        String jdbcURL = getInitParameter("jdbcURL");
        String jdbcUsername = getInitParameter("jdbcUsername");
        String jdbcPassword = getInitParameter("jdbcPassword");

        carsDAO = new CarsDAO(jdbcURL, jdbcUsername, jdbcPassword);

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
 
        List<Cars> carList = new ArrayList<Cars>();
        try {
            carList = carsDAO.listAllCars();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String json=new Gson().toJson(carList);
        // System.out.println(json);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqData=req.getReader().lines().collect(Collectors.joining());
        Cars newCar= new Gson().fromJson(reqData,Cars.class);
        System.out.println(reqData);
        System.out.println(reqData.length());
        System.out.println(newCar.getName());
        try {
            carsDAO.insertCar(newCar);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestData = req.getReader().lines().collect(Collectors.joining());
        Cars updateCar = new Gson().fromJson(requestData, Cars.class);
        System.out.println("requestData Length->" + requestData.length());
        System.out.println("requestData->" + requestData);
        System.out.println(updateCar.getId()+" "+updateCar.getName());
 
        try {
            carsDAO.updateCar(updateCar);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestData = req.getReader().lines().collect(Collectors.joining());
        Cars delCar = new Gson().fromJson(requestData, Cars.class);
        System.out.println("requestData Length->" + requestData.length());
        System.out.println("requestData->" + requestData);
        System.out.println(delCar.getId()+" "+delCar.getName());
 
        try {
            carsDAO.deleteCar(delCar);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

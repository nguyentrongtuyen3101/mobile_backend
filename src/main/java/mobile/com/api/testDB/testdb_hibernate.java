package mobile.com.api.testDB;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class testdb_hibernate {

    @Autowired
    private DataSource myDataSource;

    @Autowired
    private SessionFactory sessionFactory;
   

    @GetMapping("/testdb_hibernate")
    public String testDatabase() {
        System.out.println("✅ Endpoint /testdb_hibernate được gọi!"); // Log kiểm tra
        StringBuilder response = new StringBuilder();

        try (Connection conn = myDataSource.getConnection()) {
            response.append("Kết nối Database thành công! URL: ").append(conn.getMetaData().getURL());
        } catch (SQLException e) {
            response.append("Lỗi kết nối Database: ").append(e.getMessage());
        }

        return response.toString();
    }
}
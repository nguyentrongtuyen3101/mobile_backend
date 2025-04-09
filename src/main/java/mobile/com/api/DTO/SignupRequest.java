package mobile.com.api.DTO;
import mobile.com.api.entity.Account.Role;
public class SignupRequest {
	private String hoTen;
    private String gmail;
    private String matKhau;
    private Role role; // Có thể để mặc định là USER nếu không truyền

    // Constructor mặc định
    public SignupRequest() {
    }

    // Constructor với các tham số
    public SignupRequest(String hoTen, String gmail, String matKhau, Role role) {
        this.hoTen = hoTen;
        this.gmail = gmail;
        this.matKhau = matKhau;
        this.role = role;
    }

    // Getters và Setters
    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

package mobile.com.api.entity;

import java.sql.Date;
import java.util.List;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "account")
@Data // Lombok vẫn tự sinh getter/setter, nhưng ta có thể thêm thủ công nếu cần
public class Account {

    public enum Role {
        ADMIN,  // Không cần displayName nếu chỉ dùng internally
        USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hoTen;
    
    @Column(unique = true)
    private String gmail;

    private String matKhau;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'Trái Đất'")
    private String diachi="trái đất";
    private Date sinhnhat;
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean sex=true;
    private String sdt;
    /**
	 * @return the duongDanAnh
	 */
	public String getDuongDanAnh() {
		return duongDanAnh;
	}

	/**
	 * @param duongDanAnh the duongDanAnh to set
	 */
	public void setDuongDanAnh(String duongDanAnh) {
		this.duongDanAnh = duongDanAnh;
	}

	@Column(name = "duong_dan_anh", length = 255)
    private String duongDanAnh="hehehhe";

	/**
	 * @return the diachi
	 */
	public String getDiachi() {
		return diachi;
	}

	/**
	 * @return the sinhnhat
	 */
	public Date getSinhnhat() {
		return sinhnhat;
	}

	/**
	 * @return the sex
	 */
	public boolean isSex() {
		return sex;
	}

	/**
	 * @param diachi the diachi to set
	 */
	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}

	/**
	 * @param sinhnhat the sinhnhat to set
	 */
	public void setSinhnhat(Date sinhnhat) {
		this.sinhnhat = sinhnhat;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(boolean sex) {
		this.sex = sex;
	}

	@Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<GioHang> gioHangs;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<YeuThich> yeuThichs;

    // ===== Các phương thức bổ sung ===== //
    
    // Kiểm tra role
    /**
     * @return
     */
    @PrePersist
    protected void onCreate() {
        this.sinhnhat = new Date(System.currentTimeMillis());
    }
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    // Getter/Setter thủ công (nếu cần override từ @Data)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the sdt
	 */
	public String getSdt() {
		return sdt;
	}

	/**
	 * @param sdt the sdt to set
	 */
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getHoTen() {
        return this.hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGmail() {
        return this.gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getMatKhau() {
        return this.matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<HoaDon> getHoaDons() {
        return this.hoaDons;
    }

    public void setHoaDons(List<HoaDon> hoaDons) {
        this.hoaDons = hoaDons;
    }

    public List<GioHang> getGioHangs() {
        return this.gioHangs;
    }

    public void setGioHangs(List<GioHang> gioHangs) {
        this.gioHangs = gioHangs;
    }

    public List<YeuThich> getYeuThichs() {
        return this.yeuThichs;
    }

    public void setYeuThichs(List<YeuThich> yeuThichs) {
        this.yeuThichs = yeuThichs;
    }
}
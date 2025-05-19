package mobile.com.api.entity;

import javax.persistence.*;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_account", nullable = false)
    private Long idAccount;

    @Column(name = "id_discount")
    private Long idDiscount; // Có thể null, liên kết với bảng Discount

    @Column(name = "ho_ten", length = 100, nullable = false)
    private String hoTen;

    @Column(name = "sdt", length = 15)
    private String sdt;

    @Column(name = "diachi_giaohang", length = 255, nullable = false)
    private String diachigiaohang;

    @Column(name = "phuongthuc_thanhtoan", nullable = false)
    private boolean phuongthucthanhtoan; // true: thanh toán online, false: thanh toán khi nhận hàng

    @Column(name = "tong_tien", nullable = false)
    private double tongtien;
    
    @Column(name = "status", nullable = false,columnDefinition = "INT DEFAULT 1")
    private int status=1;
    /**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	// Mối quan hệ Many-to-One với Account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account", referencedColumnName = "id", insertable = false, updatable = false)
    private Account account;

    // Mối quan hệ Many-to-One với Discount (có thể null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_discount", referencedColumnName = "id", insertable = false, updatable = false)
    private Discount discount;

    // Mối quan hệ One-to-Many với OrderDetail
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    // Constructor mặc định (yêu cầu của JPA)
    public Order() {
    }

    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the idAccount
	 */
	public Long getIdAccount() {
		return idAccount;
	}

	/**
	 * @return the idDiscount
	 */
	public Long getIdDiscount() {
		return idDiscount;
	}

	/**
	 * @return the hoTen
	 */
	public String getHoTen() {
		return hoTen;
	}

	/**
	 * @return the sdt
	 */
	public String getSdt() {
		return sdt;
	}

	/**
	 * @return the diachigiaohang
	 */
	public String getDiachigiaohang() {
		return diachigiaohang;
	}

	/**
	 * @return the phuongthucthanhtoan
	 */
	public boolean isPhuongthucthanhtoan() {
		return phuongthucthanhtoan;
	}

	/**
	 * @return the tongtien
	 */
	public double getTongtien() {
		return tongtien;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the discount
	 */
	public Discount getDiscount() {
		return discount;
	}

	/**
	 * @return the orderDetails
	 */
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param idAccount the idAccount to set
	 */
	public void setIdAccount(Long idAccount) {
		this.idAccount = idAccount;
	}

	/**
	 * @param idDiscount the idDiscount to set
	 */
	public void setIdDiscount(Long idDiscount) {
		this.idDiscount = idDiscount;
	}

	/**
	 * @param hoTen the hoTen to set
	 */
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	/**
	 * @param sdt the sdt to set
	 */
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	/**
	 * @param diachigiaohang the diachigiaohang to set
	 */
	public void setDiachigiaohang(String diachigiaohang) {
		this.diachigiaohang = diachigiaohang;
	}

	/**
	 * @param phuongthucthanhtoan the phuongthucthanhtoan to set
	 */
	public void setPhuongthucthanhtoan(boolean phuongthucthanhtoan) {
		this.phuongthucthanhtoan = phuongthucthanhtoan;
	}

	/**
	 * @param tongtien the tongtien to set
	 */
	public void setTongtien(double tongtien) {
		this.tongtien = tongtien;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	/**
	 * @param orderDetails the orderDetails to set
	 */
	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	// Constructor với tham số
    public Order(Long idAccount, Long idDiscount, String hoTen, String sdt, String diachigiaohang, boolean phuongthucthanhtoan, double tongtien,int status) {
        this.idAccount = idAccount;
        this.idDiscount = idDiscount;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diachigiaohang = diachigiaohang;
        this.phuongthucthanhtoan = phuongthucthanhtoan;
        this.tongtien = tongtien;
        this.status=status;
        this.status=1;
    }
}
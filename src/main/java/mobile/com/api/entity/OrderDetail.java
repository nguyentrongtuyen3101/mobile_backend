package mobile.com.api.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_detail")
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_order", nullable = false)
    private Long idOrder;

    @Column(name = "id_sanpham", nullable = false)
    private Long idSanpham;

    @Column(name = "so_luong", nullable = false)
    private int soluong;

    @Column(name = "gia_tien", nullable = false)
    private double giatien;

    @Column(name = "tong_tien_sanpham", nullable = false)
    private double tongtiensanpham;

    // Mối quan hệ Many-to-One với Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order", referencedColumnName = "id", insertable = false, updatable = false)
    private Order order;

    // Mối quan hệ Many-to-One với SanPham
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sanpham", referencedColumnName = "id", insertable = false, updatable = false)
    private SanPham sanPham;

    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the idOrder
	 */
	public Long getIdOrder() {
		return idOrder;
	}

	/**
	 * @return the idSanpham
	 */
	public Long getIdSanpham() {
		return idSanpham;
	}

	/**
	 * @return the soluong
	 */
	public int getSoluong() {
		return soluong;
	}

	/**
	 * @return the giatien
	 */
	public double getGiatien() {
		return giatien;
	}

	/**
	 * @return the tongtiensanpham
	 */
	public double getTongtiensanpham() {
		return tongtiensanpham;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @return the sanPham
	 */
	public SanPham getSanPham() {
		return sanPham;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param idOrder the idOrder to set
	 */
	public void setIdOrder(Long idOrder) {
		this.idOrder = idOrder;
	}

	/**
	 * @param idSanpham the idSanpham to set
	 */
	public void setIdSanpham(Long idSanpham) {
		this.idSanpham = idSanpham;
	}

	/**
	 * @param soluong the soluong to set
	 */
	public void setSoluong(int soluong) {
		this.soluong = soluong;
	}

	/**
	 * @param giatien the giatien to set
	 */
	public void setGiatien(double giatien) {
		this.giatien = giatien;
	}

	/**
	 * @param tongtiensanpham the tongtiensanpham to set
	 */
	public void setTongtiensanpham(double tongtiensanpham) {
		this.tongtiensanpham = tongtiensanpham;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @param sanPham the sanPham to set
	 */
	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}

	// Constructor mặc định (yêu cầu của JPA)
    public OrderDetail() {
    }

    // Constructor với tham số
    public OrderDetail(Long idOrder, Long idSanpham, int soluong, double giatien, double tongtiensanpham) {
        this.idOrder = idOrder;
        this.idSanpham = idSanpham;
        this.soluong = soluong;
        this.giatien = giatien;
        this.tongtiensanpham = tongtiensanpham;
    }
}
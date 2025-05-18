package mobile.com.api.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "discount")
@Data
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_account", nullable = false)
    private Long idAccount;

    @Column(name = "ma_khuyen_mai", length = 50, nullable = false, unique = true)
    private String maKhuyenMai;
    
    @Column(name = "gia_tien")
    private double giaTien;

    // Mối quan hệ với Account (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account", referencedColumnName = "id", insertable = false, updatable = false)
    private Account account;

    // Constructor mặc định (yêu cầu của JPA)
    public Discount() {
    }

    // Constructor với tham số
    public Discount(Long idAccount, String maKhuyenMai,double giatien) {
        this.idAccount = idAccount;
        this.maKhuyenMai = maKhuyenMai;
        this.giaTien=giatien;
    }

	/**
	 * @return the giaTien
	 */
	public double getGiaTien() {
		return giaTien;
	}

	/**
	 * @param giaTien the giaTien to set
	 */
	public void setGiaTien(double giaTien) {
		this.giaTien = giaTien;
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
	 * @return the maKhuyenMai
	 */
	public String getMaKhuyenMai() {
		return maKhuyenMai;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
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
	 * @param maKhuyenMai the maKhuyenMai to set
	 */
	public void setMaKhuyenMai(String maKhuyenMai) {
		this.maKhuyenMai = maKhuyenMai;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
}
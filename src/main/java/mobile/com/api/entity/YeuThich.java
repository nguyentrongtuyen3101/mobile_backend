package mobile.com.api.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "yeu_thich")
@Data
public class YeuThich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
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
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @param sanPham the sanPham to set
	 */
	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}

	@ManyToOne
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;
}
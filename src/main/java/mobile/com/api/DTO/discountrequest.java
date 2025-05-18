package mobile.com.api.DTO;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import mobile.com.api.entity.Account;

public class discountrequest {
	  
	    private Long id;
	    private String maKhuyenMai;
	    private Account account;
	    private double giaTien;

	    // Constructor mặc định (yêu cầu của JPA)
	    public discountrequest() {
	    }

	    // Constructor với tham số
	    public discountrequest( String maKhuyenMai,double giatien) {
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

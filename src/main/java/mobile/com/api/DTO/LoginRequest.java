package mobile.com.api.DTO;

import java.sql.Date;

import javax.persistence.Column;

public class LoginRequest {
    private String gmail;
    private String matKhau;
    private String hoten;
    private String diachi;
    private Date sinhnhat;
    private boolean sex;
    private String duongDanAnh;
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


	public String getGmail() {
        return gmail;
    }

    
    /**
	 * @return the hoten
	 */
	public String getHoten() {
		return hoten;
	}


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
	 * @param hoten the hoten to set
	 */
	public void setHoten(String hoten) {
		this.hoten = hoten;
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


	public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getMatKhau() {
        return matKhau;
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


	public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}

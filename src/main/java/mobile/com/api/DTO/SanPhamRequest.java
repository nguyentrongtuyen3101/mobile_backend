package mobile.com.api.DTO;

import org.ietf.jgss.GSSContext;


import mobile.com.api.entity.SanPham.LoaiSanPham;

public class SanPhamRequest {
    private String loai;
    private String tenSanPham;
    private int soLuong;
    private String moTa;
    private String duongDanAnh;
    private double giaTien;

    // Getters & Setters
    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }
    
    public double getGiaTien() { // Getter cho giá tiền
        return giaTien;
    }

    public void setGiaTien(double giaTien) { // Setter cho giá tiền
        this.giaTien = giaTien;
    }
}
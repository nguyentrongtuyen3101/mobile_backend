package mobile.com.api.DTO;

public class SanPhamDTO {
    private Long id;
    private String loai; // Tên loại sản phẩm (RAU_CU_QUA, THIT_CA, v.v.)
    private String tenSanPham;
    private String moTa;
    private double giaTien;
    private String duongDanAnh;
    private int soLuong;

    // Constructor
    public SanPhamDTO() {}

    public SanPhamDTO(Long id, String loai, String tenSanPham, String moTa, double giaTien, String duongDanAnh, int soLuong) {
        this.id = id;
        this.loai = loai;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.giaTien = giaTien;
        this.duongDanAnh = duongDanAnh;
        this.soLuong = soLuong;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
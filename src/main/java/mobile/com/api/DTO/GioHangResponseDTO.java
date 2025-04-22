package mobile.com.api.DTO;

public class GioHangResponseDTO {
    private Long id;
    private Long accountId;
    private Long sanPhamId;
    private String tenSanPham;
    private String duongDanAnh;
    private double giaTien;
    private int soLuong;

    public GioHangResponseDTO(Long id, Long accountId, Long sanPhamId, String tenSanPham, String duongDanAnh, double giaTien, int soLuong) {
        this.id = id;
        this.accountId = accountId;
        this.sanPhamId = sanPhamId;
        this.tenSanPham = tenSanPham;
        this.duongDanAnh = duongDanAnh;
        this.giaTien = giaTien;
        this.soLuong = soLuong;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getSanPhamId() {
        return sanPhamId;
    }

    public void setSanPhamId(Long sanPhamId) {
        this.sanPhamId = sanPhamId;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
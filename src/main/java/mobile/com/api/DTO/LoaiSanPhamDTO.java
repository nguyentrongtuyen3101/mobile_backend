package mobile.com.api.DTO;

public class LoaiSanPhamDTO {
    private Long id;
    private String tenLoai;
    private String donVi;
    private String duongDanAnh;

    // Constructors
    public LoaiSanPhamDTO() {}

    public LoaiSanPhamDTO(Long id, String tenLoai, String donVi, String duongDanAnh) {
        this.id = id;
        this.tenLoai = tenLoai;
        this.donVi = donVi;
        this.duongDanAnh = duongDanAnh;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }
}
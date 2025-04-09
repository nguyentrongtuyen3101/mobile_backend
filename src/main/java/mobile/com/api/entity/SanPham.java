package mobile.com.api.entity;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "san_pham")
public class SanPham {
    public enum LoaiSanPham {
        RAU_CU_QUA("Rau củ quả"),
        TRUNG_SUA("Trứng sữa"),
        DAU_AN("Dầu ăn"),
        THIT_CA("Thịt cá"),
        BANH_MI_DO_AN_NHE("Bánh mì & đồ ăn nhẹ"),
        DO_UONG("Đồ uống");

        private String displayName;

        LoaiSanPham(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoaiSanPham loai;

    private String tenSanPham;

    private int soLuong;

    private String moTa;
   
    @Column(name = "duong_dan_anh", length = 255)
    private String duongDanAnh;

    // Getter & Setter
   

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL)
    private List<GioHang> gioHangs;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL)
    private List<YeuThich> yeuThichs;

    // --------------- Getter & Setter ---------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoaiSanPham getLoai() {
        return loai;
    }

    public void setLoai(LoaiSanPham loai) {
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
    public List<GioHang> getGioHangs() {
        return gioHangs;
    }

    public void setGioHangs(List<GioHang> gioHangs) {
        this.gioHangs = gioHangs;
    }

    public List<YeuThich> getYeuThichs() {
        return yeuThichs;
    }
    
    @Column(name = "gia_tien") // Thêm trường giá tiền
    private double giaTien;
    public void setYeuThichs(List<YeuThich> yeuThichs) {
        this.yeuThichs = yeuThichs;
    }

    // --------------- Additional Methods (Optional) ---------------

    @Override
    public String toString() {
        return "SanPham{" +
                "id=" + id +
                ", loai=" + loai +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", soLuong=" + soLuong +
                ", moTa='" + moTa + '\'' +
                ", duongDanAnh='" + duongDanAnh + '\'' +
                '}';
    }
}
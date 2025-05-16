package mobile.com.api.entity;

import javax.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "san_pham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_loai", nullable = false)
    private LoaiSanPham idLoai;

    private String tenSanPham;

    private int soLuong;

    private String moTa;

    @Column(name = "duong_dan_anh", length = 255)
    private String duongDanAnh;

    @Column(name = "gia_tien")
    private double giaTien;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GioHang> gioHangs;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<YeuThich> yeuThichs;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoaiSanPham getIdLoai() {
        return idLoai;
    }

    public void setIdLoai(LoaiSanPham idLoai) {
        this.idLoai = idLoai;
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

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
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

    public void setYeuThichs(List<YeuThich> yeuThichs) {
        this.yeuThichs = yeuThichs;
    }

    // Additional Methods (Optional)
    @Override
    public String toString() {
        return "SanPham{" +
                "id=" + id +
                ", idLoai=" + (idLoai != null ? idLoai.getId() : null) +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", soLuong=" + soLuong +
                ", moTa='" + moTa + '\'' +
                ", duongDanAnh='" + duongDanAnh + '\'' +
                ", giaTien=" + giaTien +
                '}';
    }
}
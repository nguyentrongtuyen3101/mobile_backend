package mobile.com.api.entity;

import javax.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "loai_san_pham")
public class LoaiSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_loai", nullable = false)
    private String tenLoai;

    @Column(name = "don_vi")
    private String donVi;
    @Column(name = "duong_dan_anh", length = 255)
    private String duongDanAnh;


    @OneToMany(mappedBy = "idLoai", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SanPham> sanPhams;

    // Constructors
    public LoaiSanPham() {
    }

    public LoaiSanPham(String tenLoai, String donVi) {
        this.tenLoai = tenLoai;
        this.donVi = donVi;
    }

    // Getters & Setters
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

    public List<SanPham> getSanPhams() {
        return sanPhams;
    }

    public void setSanPhams(List<SanPham> sanPhams) {
        this.sanPhams = sanPhams;
    }
    
    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }
    @Override
    public String toString() {
        return "LoaiSanPham{" +
                "id=" + id +
                ", tenLoai='" + tenLoai + '\'' +
                ", donVi='" + donVi + '\'' +
                ",duong dan anh='"+ duongDanAnh+'\''+
                '}';
    }
}
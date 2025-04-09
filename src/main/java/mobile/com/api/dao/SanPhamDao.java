package mobile.com.api.dao;

import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.SanPham.LoaiSanPham;

import java.util.List;

public interface SanPhamDao {
    // Lưu sản phẩm
    void save(SanPham sanPham);

    // Tìm sản phẩm theo tên
    SanPham findByTenSanPham(String tenSanPham);
    List<SanPham> findByLoai(LoaiSanPham loai); 
}
package mobile.com.api.service;

import mobile.com.api.entity.Account;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.SanPham.LoaiSanPham;

import java.util.List;

public interface SanPhamService {
  
    void save(SanPham sanPham);
    SanPham findByTenSanPham(String tenSanPham);
    List<SanPham> findByLoai(LoaiSanPham loai); 
    SanPham findByid(long id);
}
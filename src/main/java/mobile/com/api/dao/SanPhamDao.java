package mobile.com.api.dao;

import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.LoaiSanPham;
import mobile.com.api.entity.SanPham;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

public interface SanPhamDao {

    void save(SanPham sanPham); // Lưu hoặc cập nhật thông tin sản phẩm
    public void saveLoaiSanPham(LoaiSanPham loaiSanPham);

    SanPham findByTenSanPham(String tenSanPham); // Tìm sản phẩm theo tên

    List<SanPham> findByLoai(Long idLoai); // Tìm danh sách sản phẩm theo id loại

    SanPham findByid(long id); // Tìm sản phẩm theo id

    GioHang addgiohang(GioHang gioHang); // Thêm hoặc cập nhật giỏ hàng

    GioHang findGioHangByAccountAndSanPham(Long accountId, Long sanPhamId); // Tìm giỏ hàng theo tài khoản và sản phẩm

    List<GioHang> getGioHangByAccount(Long accountId); // Lấy danh sách giỏ hàng của tài khoản

    void deleteGioHang(Long gioHangId); // Xóa một mục trong giỏ hàng

    LoaiSanPham findLoaiSanPhamById(Long id); // Tìm loại sản phẩm theo id
    List<LoaiSanPham> findAllLoaiSanPham(); // Lấy danh sách tất cả loại sản phẩm
}
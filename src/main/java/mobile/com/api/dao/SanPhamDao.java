package mobile.com.api.dao;

import mobile.com.api.DTO.orderrequest;
import mobile.com.api.entity.Discount;
import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.LoaiSanPham;
import mobile.com.api.entity.OrderDetail;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.YeuThich;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

import mobile.com.api.entity.Order;

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
    YeuThich addThich(YeuThich yeuThich);
    YeuThich findyeuthuichByAccountAndSanPham(Long accountId, Long sanPhamId);
    List<YeuThich> getyeuthichByAccount(Long accountId);
    void deleteyeuthich(long yeuthichid);
    Discount addDiscount(Discount discount);
    Order addorrder(mobile.com.api.entity.Order order);
    OrderDetail addorderdetail(OrderDetail oderdetail);
}
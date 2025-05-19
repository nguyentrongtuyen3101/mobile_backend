package mobile.com.api.service;

import mobile.com.api.DTO.LoaiSanPhamDTO;
import mobile.com.api.DTO.OrderDetailResponseDTO;
import mobile.com.api.DTO.OrderResponseDTO;
import mobile.com.api.DTO.orderrequest;
import mobile.com.api.dao.SanPhamDao;
import mobile.com.api.entity.Discount;
import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.LoaiSanPham;
import mobile.com.api.entity.OrderDetail;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.YeuThich;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mobile.com.api.entity.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SanPhamServiceImpl implements SanPhamService {

    @Autowired
    private SanPhamDao sanPhamDao;

    @Override
    @Transactional
    public void save(SanPham sanPham) {
        sanPhamDao.save(sanPham);
    }

    @Override
    @Transactional
    public SanPham findByTenSanPham(String tenSanPham) {
        return sanPhamDao.findByTenSanPham(tenSanPham);
    }

    @Override
    @Transactional
    public List<SanPham> findByLoai(Long idLoai) {
        return sanPhamDao.findByLoai(idLoai);
    }

    @Override
    @Transactional
    public SanPham findByid(long id) {
        return sanPhamDao.findByid(id);
    }

    @Override
    @Transactional
    public GioHang addgiohang(GioHang gioHang) {
        return sanPhamDao.addgiohang(gioHang);
    }

    @Override
    @Transactional
    public GioHang findGioHangByAccountAndSanPham(Long accountId, Long sanPhamId) {
        return sanPhamDao.findGioHangByAccountAndSanPham(accountId, sanPhamId);
    }

    @Override
    @Transactional
    public List<GioHang> getGioHangByAccount(Long accountId) {
        return sanPhamDao.getGioHangByAccount(accountId);
    }

    @Override
    @Transactional
    public void deleteGioHang(Long gioHangId) {
        sanPhamDao.deleteGioHang(gioHangId);
    }

    @Override
    @Transactional
    public LoaiSanPham findLoaiSanPhamById(Long id) {
        return sanPhamDao.findLoaiSanPhamById(id);
    }
    @Override
    @Transactional
    public List<LoaiSanPham> findAllLoaiSanPham() { // Lấy danh sách tất cả loại sản phẩm thông qua DAO
        return sanPhamDao.findAllLoaiSanPham();
    }
    @Override
    @Transactional
    public void saveLoaiSanPham(LoaiSanPham loaiSanPham) {
        sanPhamDao.saveLoaiSanPham(loaiSanPham);
    }
    @Override
    @Transactional
    public YeuThich addThich(YeuThich yeuThich)
    {
    	return sanPhamDao.addThich(yeuThich);
    }
    @Override
    @Transactional
    public YeuThich findyeuthuichByAccountAndSanPham(Long accountId, Long sanPhamId)
    {
    	return sanPhamDao.findyeuthuichByAccountAndSanPham(accountId, sanPhamId);
    }
    @Override
    @Transactional
    public List<YeuThich> getyeuthichByAccount(Long accountId) {
		return sanPhamDao.getyeuthichByAccount(accountId);
	}
    @Override
    @Transactional
    public void deleteyeuthich(long yeuthichid)
    {
    	sanPhamDao.deleteyeuthich(yeuthichid);
    }
    @Override
    @Transactional
    public Discount addDiscount(Discount discount)
    {
    	return sanPhamDao.addDiscount(discount);
    }
    @Override
    @Transactional
    public Order addorrder(Order order)
    {
    	return sanPhamDao.addorrder(order);
    }
    @Override
    @Transactional
    public OrderDetail addorderdetail(OrderDetail oderdetail)
    {
    	return sanPhamDao.addorderdetail(oderdetail);
    }
    @Override
    @Transactional
    public List<OrderResponseDTO> getdonhangByAccount(Long accountId){
    	return sanPhamDao.getdonhangByAccount(accountId);
    }
    @Override
    @Transactional
    public List<OrderDetailResponseDTO> getchitietdonhangByAccount(Long orderId)
    {
    	return sanPhamDao.getchitietdonhangByAccount(orderId);
    }
    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, int status)
    {
    	sanPhamDao.updateOrderStatus( orderId,status);
    }
    @Override
    @Transactional(readOnly = true)
    public Integer getAvailableQuantity(Long idSanPham) {
        return sanPhamDao.getAvailableQuantity(idSanPham);
    }

    @Override
    @Transactional
    public void updateProductQuantity(Long idSanPham, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Số lượng không thể nhỏ hơn 0");
        }
        sanPhamDao.updateProductQuantity(idSanPham, newQuantity);
    }
}
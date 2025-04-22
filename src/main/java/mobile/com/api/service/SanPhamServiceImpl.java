package mobile.com.api.service;

import mobile.com.api.dao.SanPhamDao;
import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.SanPham.LoaiSanPham;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class SanPhamServiceImpl implements SanPhamService {

    @Autowired
    private SanPhamDao sanPhamDao;

   
    @Override
    public void save(SanPham sanPham) {
        sanPhamDao.save(sanPham);
    }

    @Override
    public SanPham findByTenSanPham(String tenSanPham) {
        return sanPhamDao.findByTenSanPham(tenSanPham);
    }
    @Override
    public List<SanPham> findByLoai(LoaiSanPham loai) {
        return sanPhamDao.findByLoai(loai);
    }
    @Override
    public SanPham findByid(long id)
    {
    	return sanPhamDao.findByid(id);
    }
    
    @Override
    public GioHang addgiohang(GioHang gioHang) {
        GioHang existingGioHang = sanPhamDao.findGioHangByAccountAndSanPham(
            gioHang.getAccount().getId(),
            gioHang.getSanPham().getId()
        );

        if (existingGioHang != null) {
            // Kiểm tra thời gian cập nhật gần nhất
            long lastUpdated = existingGioHang.getLastUpdated() != null ? existingGioHang.getLastUpdated().getTime() : 0;
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdated < 5000) { // 5 giây
                throw new RuntimeException("Yêu cầu cập nhật giỏ hàng quá nhanh, vui lòng thử lại sau!");
            }

            existingGioHang.setSoLuong(existingGioHang.getSoLuong() + gioHang.getSoLuong());
            existingGioHang.setLastUpdated(new Date(currentTime));
            sanPhamDao.addgiohang(existingGioHang);
            return existingGioHang;
        } else {
            gioHang.setLastUpdated(new Date(0));
            sanPhamDao.addgiohang(gioHang);
            return gioHang;
        }
    }
    @Override
    public List<GioHang> getGioHangByAccount(Long accountId) {
        return sanPhamDao.getGioHangByAccount(accountId);
    }
    @Override
    public void deleteGioHang(Long gioHangId) {
        sanPhamDao.deleteGioHang(gioHangId);
    }
}
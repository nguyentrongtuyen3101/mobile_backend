package mobile.com.api.service;

import mobile.com.api.dao.SanPhamDao;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.SanPham.LoaiSanPham;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
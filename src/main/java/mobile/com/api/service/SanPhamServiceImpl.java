package mobile.com.api.service;

import mobile.com.api.DTO.LoaiSanPhamDTO;
import mobile.com.api.dao.SanPhamDao;
import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.LoaiSanPham;
import mobile.com.api.entity.SanPham;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    }/*
    @Override
    @Transactional
    public Map<String, Object> findAllLoaiSanPhamPaged(int page, int size) {
        List<LoaiSanPham> allLoaiSanPhams = sanPhamDao.findAllLoaiSanPham();

        int totalElements = allLoaiSanPhams.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int startItem = page * size;
        List<LoaiSanPhamDTO> pagedList;

        if (totalElements <= startItem) {
            pagedList = List.of();
        } else {
            int toIndex = Math.min(startItem + size, totalElements);
            pagedList = allLoaiSanPhams.subList(startItem, toIndex).stream()
                .map(loai -> new LoaiSanPhamDTO(
                    loai.getId(),
                    loai.getTenLoai(),
                    loai.getDonVi(),
                    loai.getDuongDanAnh()
                ))
                .collect(Collectors.toList());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", pagedList);
        response.put("totalPages", totalPages);
        response.put("totalElements", totalElements);
        response.put("currentPage", page);
        return response;
    }*/
}
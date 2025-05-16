package mobile.com.api.controller;

import mobile.com.api.DTO.SanPhamRequest;
import mobile.com.api.DTO.SanPhamDTO;
import mobile.com.api.DTO.GioHangResponseDTO;
import mobile.com.api.DTO.giohangDTO;
import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.LoaiSanPham;
import mobile.com.api.entity.Account;
import mobile.com.api.service.SanPhamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sanphammagager")
@CrossOrigin(origins = "*")
public class SanPhamController {

    private static final Logger logger = LoggerFactory.getLogger(SanPhamController.class);

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private ServletContext servletContext;

    @PostMapping(value = "/them", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> themSanPham(
            @RequestBody SanPhamRequest request,
            HttpServletRequest httpRequest) {
        SanPham sanPham = new SanPham();
        sanPham.setTenSanPham(request.getTenSanPham());
        sanPham.setSoLuong(request.getSoLuong());
        sanPham.setMoTa(request.getMoTa());
        sanPham.setGiaTien(request.getGiaTien());

        // Xử lý ảnh
        String duongDanAnh = request.getDuongDanAnh();
        if (duongDanAnh != null && duongDanAnh.startsWith("data:image")) {
            try {
                String base64Image = duongDanAnh.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                String fileName = "product_" + System.currentTimeMillis() + ".jpg";

                String uploadDir = servletContext.getRealPath("/uploads/");
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
                logger.info("Đường dẫn lưu ảnh: {}", uploadPath.toString());

                if (!Files.exists(uploadPath)) {
                    logger.info("Tạo thư mục: {}", uploadPath.toString());
                    Files.createDirectories(uploadPath);
                }

                if (!Files.isWritable(uploadPath)) {
                    throw new Exception("Không có quyền ghi vào thư mục: " + uploadPath.toString());
                }

                Path filePath = uploadPath.resolve(fileName);
                logger.info("Ghi file vào: {}", filePath.toString());
                Files.write(filePath, imageBytes);
                sanPham.setDuongDanAnh("/uploads/" + fileName);
            } catch (Exception e) {
                logger.error("Lỗi khi lưu ảnh", e);
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Lỗi khi lưu ảnh: " + e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            sanPham.setDuongDanAnh(duongDanAnh != null ? duongDanAnh : "default-product.jpg");
        }

        if (request.getLoai() != null) {
            try {
                Long idLoai = Long.valueOf(request.getLoai());
                LoaiSanPham loaiSanPham = sanPhamService.findLoaiSanPhamById(idLoai);
                if (loaiSanPham == null) {
                    throw new IllegalArgumentException("Loại sản phẩm không hợp lệ: " + request.getLoai());
                }
                sanPham.setIdLoai(loaiSanPham);
            } catch (IllegalArgumentException e) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        }

        SanPham existingProduct = sanPhamService.findByTenSanPham(request.getTenSanPham());
        Map<String, String> response = new HashMap<>();
        if (existingProduct != null) {
            existingProduct.setSoLuong(existingProduct.getSoLuong() + request.getSoLuong());
            sanPhamService.save(existingProduct);
            response.put("action", "updated");
        } else {
            sanPhamService.save(sanPham);
            response.put("action", "created");
        }
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/sanpham/idloai",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SanPhamDTO>> getSanPhamByLoai(@RequestParam("idloai") String loai) {
        try {
            Long idLoai = Long.valueOf(loai);
            List<SanPham> sanPhams = sanPhamService.findByLoai(idLoai);
            List<SanPhamDTO> sanPhamDTOs = sanPhams.stream().map(sp -> new SanPhamDTO(
                sp.getId(),
                sp.getIdLoai().getTenLoai(),
                sp.getTenSanPham(),
                sp.getMoTa(),
                sp.getGiaTien(),
                sp.getDuongDanAnh(),
                sp.getSoLuong(),
                sp.getIdLoai().getDonVi()
            )).collect(Collectors.toList());
            return ResponseEntity.ok(sanPhamDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/sanphamchitiet/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSanPhamById(@PathVariable("id") Long id) {
        try {
            SanPham sanPham = sanPhamService.findByid(id);
            if (sanPham == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy sản phẩm với id: " + id
                ));
            }
            SanPhamDTO sanPhamDTO = new SanPhamDTO(
                sanPham.getId(),
                sanPham.getIdLoai().getTenLoai(),
                sanPham.getTenSanPham(),
                sanPham.getMoTa(),
                sanPham.getGiaTien(),
                sanPham.getDuongDanAnh(),
                sanPham.getSoLuong(),
                sanPham.getIdLoai().getDonVi()
            );
            return ResponseEntity.ok(sanPhamDTO);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy sản phẩm với id: {}", id, e);
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi lấy sản phẩm: " + e.getMessage()
            ));
        }
    }

    @PostMapping(value = "/themgiohang", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addgiohang(@RequestBody giohangDTO request) {
        try {
            if (request.getSoLuong() <= 0) {
                return ResponseEntity.status(400).body(Map.of(
                    "message", "Số lượng phải lớn hơn 0"
                ));
            }
            Account account = new Account();
            account.setId(request.getAccountId());

            SanPham sanPham = new SanPham();
            sanPham.setId(request.getSanPhamId());

            GioHang gioHang = new GioHang();
            gioHang.setAccount(account);
            gioHang.setSanPham(sanPham);
            gioHang.setSoLuong(request.getSoLuong());

            GioHang newgioHang = sanPhamService.addgiohang(gioHang);
            if (newgioHang == null) {
                return ResponseEntity.status(500).body(Map.of(
                    "message", "Không thể thêm sản phẩm vào giỏ hàng"
                ));
            }

            giohangDTO responseDTO = new giohangDTO(
                newgioHang.getId(),
                newgioHang.getAccount().getId(),
                newgioHang.getSanPham().getId(),
                newgioHang.getSoLuong(),
                "Thêm vào giỏ hàng thành công"
            );
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", "Lỗi khi thêm vào giỏ hàng: " + e.getMessage()
            ));
        }
    }

    @GetMapping(value = "/giohang", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGioHang(@RequestParam("accountId") Long accountId) {
        try {
            List<GioHang> gioHangs = sanPhamService.getGioHangByAccount(accountId);
            List<GioHangResponseDTO> gioHangDTOs = gioHangs.stream().map(gioHang -> new GioHangResponseDTO(
                gioHang.getId(),
                gioHang.getAccount().getId(),
                gioHang.getSanPham().getId(),
                gioHang.getSanPham().getTenSanPham(),
                gioHang.getSanPham().getDuongDanAnh(),
                gioHang.getSanPham().getGiaTien(),
                gioHang.getSoLuong()
            )).collect(Collectors.toList());
            return ResponseEntity.ok(gioHangDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi lấy giỏ hàng: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping(value = "/giohang/{id}")
    public ResponseEntity<?> deleteGioHang(@PathVariable("id") Long gioHangId) {
        try {
            sanPhamService.deleteGioHang(gioHangId);
            return ResponseEntity.ok(Map.of(
                "message", "Xóa sản phẩm khỏi giỏ hàng thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi xóa sản phẩm khỏi giỏ hàng: " + e.getMessage()
            ));
        }
    }
    @GetMapping(value = "/loaisanpham", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LoaiSanPham>> getAllLoaiSanPham() { // Lấy danh sách tất cả loại sản phẩm để hiển thị trong dropdown
        try {
            List<LoaiSanPham> loaiSanPhams = sanPhamService.findAllLoaiSanPham();
            return ResponseEntity.ok(loaiSanPhams);
            
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách loại sản phẩm: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
    @PostMapping(value = "/updateloaiimage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updateLoaiSanPhamImage(
            @RequestBody Map<String, Object> request) {
        try {
            Long idLoai = Long.valueOf(request.get("loai").toString());
            String duongDanAnh = request.get("duongDanAnh").toString();

            LoaiSanPham loaiSanPham = sanPhamService.findLoaiSanPhamById(idLoai);
            if (loaiSanPham == null) {
                throw new IllegalArgumentException("Loại sản phẩm không hợp lệ: " + idLoai);
            }

            if (duongDanAnh != null && duongDanAnh.startsWith("data:image")) {
                String base64Image = duongDanAnh.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                String fileName = "category_" + idLoai + "_" + System.currentTimeMillis() + ".jpg";

                String uploadDir = servletContext.getRealPath("/uploads/");
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
                logger.info("Đường dẫn lưu ảnh: {}", uploadPath.toString());

                if (!Files.exists(uploadPath)) {
                    logger.info("Tạo thư mục: {}", uploadPath.toString());
                    Files.createDirectories(uploadPath);
                }

                if (!Files.isWritable(uploadPath)) {
                    throw new Exception("Không có quyền ghi vào thư mục: " + uploadPath.toString());
                }

                Path filePath = uploadPath.resolve(fileName);
                logger.info("Ghi file vào: {}", filePath.toString());
                Files.write(filePath, imageBytes);
                loaiSanPham.setDuongDanAnh("/uploads/" + fileName);
            } else {
                loaiSanPham.setDuongDanAnh(duongDanAnh != null ? duongDanAnh : "default-category.jpg");
            }

            sanPhamService.saveLoaiSanPham(loaiSanPham);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật ảnh loại sản phẩm thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật ảnh loại sản phẩm: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Lỗi khi cập nhật ảnh: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
 
}
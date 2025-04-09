package mobile.com.api.controller;

import mobile.com.api.DTO.SanPhamRequest;
import mobile.com.api.DTO.LoginRequest;
import mobile.com.api.DTO.SanPhamDTO;
import mobile.com.api.DTO.SignupRequest;
import mobile.com.api.entity.Account;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.SanPham.LoaiSanPham;
import mobile.com.api.service.account_service;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
@RestController
@RequestMapping("/checkmobile")
@CrossOrigin(origins = "*")
public class account_controller {

    private static final Logger logger = LoggerFactory.getLogger(account_controller.class);

    @Autowired
    private account_service accountService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private ServletContext servletContext;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Account> accountOpt = accountService.login(request.getGmail(), request.getMatKhau());
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Sai email hoặc mật khẩu");
        }
        Account account = accountOpt.get();

        // Định dạng sinhnhat thành chuỗi YYYY-MM-DD
        String sinhnhatStr = account.getSinhnhat() != null
            ? new SimpleDateFormat("yyyy-MM-dd").format(account.getSinhnhat())
            : "N/A";

        return ResponseEntity.ok(Map.of(
            "id", account.getId(),
            "gmail", account.getGmail(),
            "role", account.getRole().name(),
            "hoten", account.getHoTen(),
            "diachi", account.getDiachi(),
            "gioitinh", account.isSex(),
            "sinhnhat", sinhnhatStr
        ));
    }

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

                ServletContext servletContext = httpRequest.getServletContext();
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
                sanPham.setLoai(LoaiSanPham.valueOf(request.getLoai()));
            } catch (IllegalArgumentException e) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Loại sản phẩm không hợp lệ: " + request.getLoai());
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
 // Thêm vào phần cuối của class account_controller
    @GetMapping(value = "/sanpham/theoloai")
    public ResponseEntity<List<SanPhamDTO>> getSanPhamByLoai(@RequestParam("loai") String loai) {
        try {
            LoaiSanPham loaiSanPham = LoaiSanPham.valueOf(loai.toUpperCase());
            List<SanPham> sanPhams = sanPhamService.findByLoai(loaiSanPham);
            List<SanPhamDTO> sanPhamDTOs = sanPhams.stream().map(sp -> new SanPhamDTO(
                sp.getId(),
                sp.getLoai().name(),
                sp.getTenSanPham(),
                sp.getMoTa(),
                sp.getGiaTien(),
                sp.getDuongDanAnh(),
                sp.getSoLuong()
            )).collect(Collectors.toList());
            return ResponseEntity.ok(sanPhamDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping(value = "/quenmk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> quenmk(@RequestBody SignupRequest request) {
        try {
            // Kiểm tra xem Gmail có tồn tại không
            Account existingAccount = accountService.findByGmail(request.getGmail());
            if (existingAccount == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + request.getGmail()
                ));
            }

            // Cập nhật mật khẩu mới
            accountService.updatemk(existingAccount, request.getGmail(), request.getMatKhau());

            return ResponseEntity.ok(Map.of(
                "id", existingAccount.getId(),
                "gmail", existingAccount.getGmail(),
                "role", existingAccount.getRole().name(),
                "message", "Cập nhật mật khẩu thành công"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @PostMapping(value = "/send-otp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("gmail");
            Account existingAccount = accountService.findByGmail(email);
            if (existingAccount == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + email
                ));
            }

            String otp = accountService.sendOtp(email);
            return ResponseEntity.ok(Map.of(
                "message", "OTP đã được gửi tới " + email,
                "otp", otp // Trả về OTP để frontend lưu
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", "Đã xảy ra lỗi: " + e.getMessage()
            ));
        }
    }
}
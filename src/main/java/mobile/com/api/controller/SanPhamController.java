package mobile.com.api.controller;

import mobile.com.api.DTO.SanPhamRequest;
import mobile.com.api.DTO.YeuThichDTO;
import mobile.com.api.DTO.SanPhamDTO;
import mobile.com.api.DTO.GioHangResponseDTO;
import mobile.com.api.DTO.OrderDetailRequest;
import mobile.com.api.DTO.OrderDetailResponseDTO;
import mobile.com.api.DTO.OrderResponseDTO;
import mobile.com.api.DTO.giohangDTO;
import mobile.com.api.DTO.orderrequest;
import mobile.com.api.DTO.yeuthichreponseDTO;
import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.YeuThich;
import mobile.com.api.entity.LoaiSanPham;
import mobile.com.api.entity.Order;
import mobile.com.api.entity.OrderDetail;
import mobile.com.api.entity.Account;
import mobile.com.api.service.SanPhamService;
import mobile.com.api.service.account_service;
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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/sanphammagager")
@CrossOrigin(origins = "*")
public class SanPhamController {

    private static final Logger logger = LoggerFactory.getLogger(SanPhamController.class);

    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private account_service account_service;

    @Autowired
    private ServletContext servletContext;

    // Khai báo jwtSecret trực tiếp trong controller
    private static final String jwtSecret = "TXlTdXBlclNlY3JldEtleTEyMyFAI015U3VwZXJTZWNyZXRLZXkxMjMhQCNNeVN1cGVyU2VjcmV0S2V5MTIzIUAjTXlTdXBlclNlY3JldEtleTEyMyFAIw==";

    // Hàm lấy claims từ token
    private Claims getClaimsFromToken(String token) {
        try {
            byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKeyBytes)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("Token Issued At (iat): {}", claims.getIssuedAt());
            logger.info("Token Expiration (exp): {}", claims.getExpiration());
            return claims;
        } catch (Exception e) {
            logger.error("Lỗi giải mã token: {}", e.getMessage());
            return null;
        }
    }

    // Hàm kiểm tra token
    private ResponseEntity<?> checkToken(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        logger.info("Authorization Header: {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of(
                "message", "Token không hợp lệ hoặc không tồn tại"
            ));
        }

        String token = authHeader.substring(7);
        logger.info("Token received: {}", token);
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return ResponseEntity.status(401).body(Map.of(
                "message", "Token không hợp lệ hoặc đã hết hạn"
            ));
        }
        return null; // Token hợp lệ, trả về null để tiếp tục xử lý API
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

    @GetMapping(value = "/sanpham/idloai", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<?> addgiohang(@RequestBody giohangDTO request, HttpServletRequest httpRequest) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        // Lấy gmail từ token
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = getClaimsFromToken(token);
        String gmail = claims.getSubject();

        try {
            if (request.getSoLuong() <= 0) {
                return ResponseEntity.status(400).body(Map.of(
                    "message", "Số lượng phải lớn hơn 0"
                ));
            }

            // Tìm account bằng gmail
            Account account = account_service.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng của tài khoản chưa
            GioHang existingGioHang = sanPhamService.findGioHangByAccountAndSanPham(account.getId(), request.getSanPhamId());
            if (existingGioHang != null) {
                // Nếu đã tồn tại, cập nhật số lượng
                int newQuantity = existingGioHang.getSoLuong() + request.getSoLuong();
                existingGioHang.setSoLuong(newQuantity);
                GioHang updatedGioHang = sanPhamService.addgiohang(existingGioHang); // Lưu lại
                if (updatedGioHang == null) {
                    return ResponseEntity.status(500).body(Map.of(
                        "message", "Không thể cập nhật số lượng sản phẩm trong giỏ hàng"
                    ));
                }

                giohangDTO responseDTO = new giohangDTO(
                    updatedGioHang.getId(),
                    updatedGioHang.getAccount().getId(),
                    updatedGioHang.getSanPham().getId(),
                    updatedGioHang.getSoLuong(),
                    "Cập nhật số lượng giỏ hàng thành công"
                );
                return ResponseEntity.ok(responseDTO);
            } else {
                // Nếu chưa tồn tại, thêm mới
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
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", "Lỗi khi thêm vào giỏ hàng: " + e.getMessage()
            ));
        }
    }

    @GetMapping(value = "/giohang", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGioHang(HttpServletRequest httpRequest) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        // Lấy gmail từ token
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = getClaimsFromToken(token);
        String gmail = claims.getSubject();

        try {
            // Tìm account bằng gmail
            Account account = account_service.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            List<GioHang> gioHangs = sanPhamService.getGioHangByAccount(account.getId());
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

    @DeleteMapping(value = "/xoagiohang")
    public ResponseEntity<?> deleteGioHang(@RequestParam("id") Long gioHangId, HttpServletRequest httpRequest) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

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
    public ResponseEntity<List<LoaiSanPham>> getAllLoaiSanPham() {
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
    @PostMapping(value = "/themyeuthich", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> themyeuthich(@RequestBody YeuThichDTO request, HttpServletRequest httpRequest) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        // Lấy gmail từ token
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = getClaimsFromToken(token);
        String gmail = claims.getSubject();

        try {
            // Tìm account bằng gmail
            Account account = account_service.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            SanPham sanPham = new SanPham();
            sanPham.setId(request.getSanPhamId());

            YeuThich yeuThich = new YeuThich();
            yeuThich.setAccount(account);
            yeuThich.setSanPham(sanPham);

            YeuThich newyThich = sanPhamService.addThich(yeuThich);
            if (newyThich == null) {
                return ResponseEntity.status(500).body(Map.of(
                    "message", "Không thể thêm sản phẩm vào yeu thich"
                ));
            }

            YeuThichDTO responseDTO = new YeuThichDTO(
                newyThich.getId(),
                newyThich.getAccount().getId(),
                newyThich.getSanPham().getId(),
                "Thêm vào yeu thich thành công"
            );
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", "Lỗi khi thêm vào yeu thich: " + e.getMessage()
            ));
        }
    }
    @GetMapping(value = "/showyeuthich", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showyeuthich(HttpServletRequest httpRequest) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        // Lấy gmail từ token
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = getClaimsFromToken(token);
        String gmail = claims.getSubject();

        try {
            // Tìm account bằng gmail
            Account account = account_service.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            List<YeuThich> yeuThichs = sanPhamService.getyeuthichByAccount(account.getId());
            List<yeuthichreponseDTO> yeuthichreponseDTOs = yeuThichs.stream().map(yeuthich -> new yeuthichreponseDTO(
                yeuthich.getId(),
                yeuthich.getAccount().getId(),
                yeuthich.getSanPham().getId(),
                yeuthich.getSanPham().getTenSanPham(),
                yeuthich.getSanPham().getDuongDanAnh(),
                yeuthich.getSanPham().getGiaTien()
            )).collect(Collectors.toList());
            return ResponseEntity.ok(yeuthichreponseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi lấy danh sach yeu thich : " + e.getMessage()
            ));
        }
    }
    @DeleteMapping(value = "/xoayeuthich")
    public ResponseEntity<?> xoayeuthich(@RequestParam("id") Long yeuthichid, HttpServletRequest httpRequest) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        try {
            sanPhamService.deleteyeuthich(yeuthichid);
            return ResponseEntity.ok(Map.of(
                "message", "Xóa sản phẩm khỏi danh sach yeu thich thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi xóa sản phẩm khỏi danh sach yeu thich: " + e.getMessage()
            ));
        }
    }
 // Endpoint mới để kiểm tra sản phẩm có trong danh sách yêu thích không
    @GetMapping(value = "/check-favourite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkFavourite(HttpServletRequest httpRequest, @RequestParam("sanPhamId") Long sanPhamId) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        // Lấy gmail từ token
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = getClaimsFromToken(token);
        String gmail = claims.getSubject();

        try {
            // Tìm account bằng gmail
            Account account = account_service.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
               
                ));
            }

            // Kiểm tra xem sản phẩm có trong danh sách yêu thích không
            YeuThich yeuThich = sanPhamService.findyeuthuichByAccountAndSanPham(account.getId(), sanPhamId);
            boolean isFavourite = yeuThich != null; // Nếu yeuThich không null, sản phẩm có trong danh sách yêu thích
            return ResponseEntity.ok(Map.of(
                "isFavourite", isFavourite
            ));
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra yêu thích: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi kiểm tra yêu thích: " + e.getMessage()
            ));
        }
    }
    @PostMapping(value = "/add-order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addOrder(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        // Lấy gmail từ token
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = getClaimsFromToken(token);
        String gmail = claims.getSubject();

        try {
            // Tìm account bằng gmail
            Account account = account_service.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            // Kiểm tra dữ liệu Order
            Map<String, Object> orderData = (Map<String, Object>) request.get("order");
            if (orderData == null) {
                return ResponseEntity.status(400).body(Map.of(
                    "message", "Dữ liệu đơn hàng không được để trống"
                ));
            }

            // Ánh xạ OrderRequest
            orderrequest orderRequest = new orderrequest();
            orderRequest.setHoTen(orderData.get("hoTen") != null ? orderData.get("hoTen").toString() : "");
            orderRequest.setSdt(orderData.get("sdt") != null ? orderData.get("sdt").toString() : "");
            orderRequest.setDiachigiaohang(orderData.get("diachigiaohang") != null ? orderData.get("diachigiaohang").toString() : "");
            orderRequest.setPhuongthucthanhtoan(Boolean.parseBoolean(orderData.get("phuongthucthanhtoan") != null ? orderData.get("phuongthucthanhtoan").toString() : "false"));
            orderRequest.setTongtien(Double.parseDouble(orderData.get("tongtien") != null ? orderData.get("tongtien").toString() : "0.0"));
            if (orderData.get("idDiscount") != null) {
                orderRequest.setIdDiscount(Long.valueOf(orderData.get("idDiscount").toString()));
            }

            // Tạo và lưu Order
            Order order = new Order();
            order.setIdAccount(account.getId());
            order.setIdDiscount(orderRequest.getIdDiscount());
            order.setHoTen(orderRequest.getHoTen());
            order.setSdt(orderRequest.getSdt());
            order.setDiachigiaohang(orderRequest.getDiachigiaohang());
            order.setPhuongthucthanhtoan(orderRequest.isPhuongthucthanhtoan());
            order.setTongtien(orderRequest.getTongtien());
            if (orderData.get("status") != null) {
                order.setStatus(Integer.parseInt(orderData.get("status").toString()));
            } else {
                order.setStatus(0); // Mặc định status là 0 (Preparing Order)
            }

            Order savedOrder = sanPhamService.addorrder(order);
            if (savedOrder == null) {
                return ResponseEntity.status(500).body(Map.of(
                    "message", "Không thể tạo đơn hàng"
                ));
            }

            // Kiểm tra dữ liệu OrderDetail
            List<Map<String, Object>> orderDetailsData = (List<Map<String, Object>>) request.get("orderDetails");
            if (orderDetailsData == null || orderDetailsData.isEmpty()) {
                return ResponseEntity.status(400).body(Map.of(
                    "message", "Danh sách chi tiết đơn hàng không được để trống",
                    "orderId", savedOrder.getId()
                ));
            }

            // Thêm các OrderDetail
            List<OrderDetail> savedOrderDetails = new java.util.ArrayList<>();
            for (Map<String, Object> detailData : orderDetailsData) {
                // Kiểm tra các trường bắt buộc
                if (detailData.get("idSanpham") == null || detailData.get("soluong") == null ||
                    detailData.get("giatien") == null || detailData.get("tongtiensanpham") == null) {
                    return ResponseEntity.status(400).body(Map.of(
                        "message", "Thiếu thông tin chi tiết đơn hàng: idSanpham, soluong, giatien, tongtiensanpham là bắt buộc"
                    ));
                }

                try {
                    OrderDetailRequest detailRequest = new OrderDetailRequest();
                    detailRequest.setIdOrder(savedOrder.getId());
                    detailRequest.setIdSanpham(Long.valueOf(detailData.get("idSanpham").toString()));
                    detailRequest.setSoluong(Integer.parseInt(detailData.get("soluong").toString()));
                    detailRequest.setGiatien(Double.parseDouble(detailData.get("giatien").toString()));
                    detailRequest.setTongtiensanpham(Double.parseDouble(detailData.get("tongtiensanpham").toString()));

                    // Tạo và lưu OrderDetail
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setIdOrder(detailRequest.getIdOrder());
                    orderDetail.setIdSanpham(detailRequest.getIdSanpham());
                    orderDetail.setSoluong(detailRequest.getSoluong());
                    orderDetail.setGiatien(detailRequest.getGiatien());
                    orderDetail.setTongtiensanpham(detailRequest.getTongtiensanpham());

                    OrderDetail savedDetail = sanPhamService.addorderdetail(orderDetail);
                    if (savedDetail == null) {
                        return ResponseEntity.status(500).body(Map.of(
                            "message", "Không thể thêm chi tiết đơn hàng cho sản phẩm ID: " + detailRequest.getIdSanpham()
                        ));
                    }
                    savedOrderDetails.add(savedDetail);
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(400).body(Map.of(
                        "message", "Dữ liệu chi tiết đơn hàng không hợp lệ: " + e.getMessage()
                    ));
                }
            }

            // Trả về phản hồi thành công
            return ResponseEntity.ok(Map.of(
                "message", "Đơn hàng và chi tiết đơn hàng đã được tạo thành công",
                "orderId", savedOrder.getId(),
                "orderDetailsCount", savedOrderDetails.size()
            ));

        } catch (Exception e) {
            logger.error("Lỗi khi tạo đơn hàng: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi tạo đơn hàng: " + e.getMessage()
            ));
        }
    }

    @PostMapping(value = "/get-orders-by-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOrdersByAccount(HttpServletRequest httpRequest) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        // Lấy gmail từ token
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = getClaimsFromToken(token);
        String gmail = claims.getSubject();

        try {
            // Lấy idAccount từ token (giả sử lưu trong claim "id")
            Long idAccount = claims.get("id", Long.class);
            if (idAccount == null) {
                return ResponseEntity.status(400).body(Map.of(
                    "message", "Không tìm thấy idAccount trong token"
                ));
            }

            // Tìm account bằng gmail để xác thực
            Account account = account_service.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            // Kiểm tra xem idAccount trong token có khớp với tài khoản không (bảo mật)
            if (!account.getId().equals(idAccount)) {
                return ResponseEntity.status(403).body(Map.of(
                    "message", "Thông tin tài khoản không khớp với token"
                ));
            }

            // Lấy danh sách đơn hàng dưới dạng OrderResponseDTO
            List<OrderResponseDTO> orders = sanPhamService.getdonhangByAccount(idAccount);
            if (orders == null || orders.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "message", "Không tìm thấy đơn hàng cho tài khoản ID: " + idAccount,
                    "orders", new java.util.ArrayList<>()
                ));
            }

            return ResponseEntity.ok(Map.of(
                "message", "Lấy danh sách đơn hàng thành công",
                "orders", orders
            ));
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách đơn hàng: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage()
            ));
        }
    }
    @PostMapping(value = "/get-order-details-by-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOrderDetailsByOrder(HttpServletRequest httpRequest, @RequestParam("orderId") Long orderId) {
        // Kiểm tra token
        ResponseEntity<?> tokenCheck = checkToken(httpRequest);
        if (tokenCheck != null) {
            return tokenCheck;
        }

        // Lấy gmail từ token (dùng để xác thực nhưng không kiểm tra quyền sở hữu)
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = getClaimsFromToken(token);
        String gmail = claims.getSubject();

        try {
            // Tìm account bằng gmail (chỉ để xác thực, không dùng để kiểm tra quyền)
            Account account = account_service.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            // Lấy danh sách chi tiết đơn hàng dưới dạng OrderDetailResponseDTO
            List<OrderDetailResponseDTO> orderDetails = sanPhamService.getchitietdonhangByAccount(orderId);
            if (orderDetails == null || orderDetails.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "message", "Không tìm thấy chi tiết đơn hàng cho order ID: " + orderId,
                    "orderDetails", new java.util.ArrayList<>()
                ));
            }

            return ResponseEntity.ok(Map.of(
                "message", "Lấy chi tiết đơn hàng thành công",
                "orderDetails", orderDetails
            ));
        } catch (Exception e) {
            logger.error("Lỗi khi lấy chi tiết đơn hàng: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi lấy chi tiết đơn hàng: " + e.getMessage()
            ));
        }
    }
    @PutMapping(value = "/update-order-status")
    public ResponseEntity<Map<String, String>> updateOrderStatus(@RequestParam("orderId") Long orderId) {
        try {
            // Gọi service để cập nhật trạng thái đơn hàng
            sanPhamService.updateOrderStatus(orderId, 3); // Cố định status = 3, bạn có thể thay đổi nếu muốn

            // Trả về phản hồi thành công
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật trạng thái đơn hàng thành công cho orderId: " + orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Ghi log lỗi
            logger.error("Lỗi khi cập nhật trạng thái đơn hàng: {}", e.getMessage());

            // Trả về phản hồi lỗi
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
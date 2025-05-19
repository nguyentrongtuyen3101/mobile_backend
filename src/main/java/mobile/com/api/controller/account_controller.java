package mobile.com.api.controller;

import mobile.com.api.DTO.LoginRequest;
import mobile.com.api.DTO.SignupRequest;
import mobile.com.api.DTO.discountrequest;
import mobile.com.api.entity.Account;
import mobile.com.api.entity.Discount;
import mobile.com.api.service.SanPhamService;
import mobile.com.api.service.account_service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/checkmobile")
@CrossOrigin(origins = "*")
public class account_controller {

    private static final Logger logger = LoggerFactory.getLogger(account_controller.class);

    @Autowired
    private account_service accountService;

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private SanPhamService sanPhamService;

    // Khai báo jwtSecret trực tiếp trong controller
    private static final String jwtSecret = "TXlTdXBlclNlY3JldEtleTEyMyFAI015U3VwZXJTZWNyZXRLZXkxMjMhQCNNeVN1cGVyU2VjcmV0S2V5MTIzIUAjTXlTdXBlclNlY3JldEtleTEyMyFAIw==";

    // Lấy claims từ token trong request
    private Claims getClaimsFromToken(String token) {
        try {
            byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKeyBytes)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("Token Issued At (iat): {}", new Date(claims.getIssuedAt().getTime()));
            logger.info("Token Expiration (exp): {}", new Date(claims.getExpiration().getTime()));
            return claims;
        } catch (Exception e) {
            logger.error("Lỗi giải mã token: {}", e.getMessage());
            return null;
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Account> accountOpt = accountService.login(request.getGmail(), request.getMatKhau());
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Sai email hoặc mật khẩu");
        }
        Account account = accountOpt.get();

        String sinhnhatStr = account.getSinhnhat() != null
            ? new SimpleDateFormat("yyyy-MM-dd").format(account.getSinhnhat())
            : "N/A";

        long expirationTime = 86400000; // 24 giờ
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
        String token = Jwts.builder()
                .setSubject(account.getGmail())
                .claim("id", account.getId())
                .claim("role", account.getRole().name())
                .claim("hoten", account.getHoTen())
                .claim("diachi", account.getDiachi())
                .claim("gioitinh", account.isSex())
                .claim("sdt", account.getSdt())
                .claim("sinhnhat", sinhnhatStr)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secretKeyBytes)
                .compact();

        return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Đăng nhập thành công"
            ));
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            Account existingAccount = accountService.findByGmail(request.getGmail());
            if (existingAccount != null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Đã có tài khoản với Gmail: " + request.getGmail()
                ));
            }
            Account newAccount = accountService.signup(request);
            return ResponseEntity.ok(Map.of(
                "id", newAccount.getId(),
                "gmail", newAccount.getGmail(),
                "role", newAccount.getRole().name(),
                "message", "Đăng ký thành công"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping(value = "/showaccount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showaccount(HttpServletRequest httpRequest) {
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

        try {
            String gmail = claims.getSubject();
            Account newAccount = accountService.findByGmail(gmail);
            if (newAccount == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            String sinhnhatStr = newAccount.getSinhnhat() != null
                ? new SimpleDateFormat("yyyy-MM-dd").format(newAccount.getSinhnhat())
                : "";
            return ResponseEntity.ok(Map.of(
                "id", newAccount.getId(),
                "gmail", newAccount.getGmail(),
                "hoten", newAccount.getHoTen(),
                "sdt",newAccount.getSdt(),
                "diachi", newAccount.getDiachi(),
                "sinhnhat", sinhnhatStr,
                "gioitinh", newAccount.isSex(),
                "message", "Lấy thông tin tài khoản thành công"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping(value = "/quenmk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> quenmk(@RequestBody SignupRequest request) {
        try {
            Account existingAccount = accountService.findByGmail(request.getGmail());
            if (existingAccount == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + request.getGmail()
                ));
            }

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
                "otp", otp
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", "Đã xảy ra lỗi: " + e.getMessage()
            ));
        }
    }

    @PostMapping(value = "/updateaccount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateacc(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
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
        try {
            Account existingAccount = accountService.findByGmail(request.getGmail());
            if (existingAccount == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + request.getGmail()
                ));
            }

            accountService.updateacc(request);
            Account updatedAccount = accountService.findByGmail(request.getGmail());
            String sinhnhatStr = updatedAccount.getSinhnhat() != null
                    ? new SimpleDateFormat("yyyy-MM-dd").format(updatedAccount.getSinhnhat())
                    : "N/A";

            return ResponseEntity.ok(Map.of(
                "id", existingAccount.getId(),
                "gmail", existingAccount.getGmail(),
                "role", existingAccount.getRole().name(),
                "hoten", existingAccount.getHoTen(),
                "gioitinh", existingAccount.isSex(),
                "sinhnhat", sinhnhatStr,
                "sdt",existingAccount.getSdt(),
                "diachi", existingAccount.getDiachi(),
                "message", "Cập nhật thông tin tài khoản thành công"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploadprofilepic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("gmail") String gmail,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest httpRequest) {
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
        try {
            Account existingAccount = accountService.findByGmail(gmail);
            if (existingAccount == null) {
                logger.error("Không tìm thấy tài khoản với Gmail: {}", gmail);
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            if (file.isEmpty()) {
                logger.error("File ảnh không được để trống cho Gmail: {}", gmail);
                return ResponseEntity.status(400).body(Map.of(
                    "message", "File ảnh không được để trống"
                ));
            }

            String fileName = "profile_" + gmail.replace("@", "") + "" + System.currentTimeMillis() + ".jpg";
            String uploadDir = servletContext.getRealPath("/uploads/");
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            logger.info("Đường dẫn lưu ảnh: {}", uploadPath.toString());

            if (!Files.exists(uploadPath)) {
                logger.info("Tạo thư mục: {}", uploadPath.toString());
                Files.createDirectories(uploadPath);
            }

            if (!Files.isWritable(uploadPath)) {
                logger.error("Không có quyền ghi vào thư mục: {}", uploadPath.toString());
                return ResponseEntity.status(500).body(Map.of(
                    "message", "Không có quyền ghi vào thư mục: " + uploadPath.toString()
                ));
            }

            Path filePath = uploadPath.resolve(fileName);
            logger.info("Ghi file vào: {}", filePath.toString());
            Files.write(filePath, file.getBytes());

            String duongDanAnh = "/uploads/" + fileName;
            logger.info("Cập nhật đường dẫn ảnh vào DB: {}", duongDanAnh);
            accountService.updateProfilePicture(gmail, duongDanAnh);

            Account updatedAccount = accountService.findByGmail(gmail);
            if (updatedAccount.getDuongDanAnh() == null || !updatedAccount.getDuongDanAnh().equals(duongDanAnh)) {
                logger.error("Đường dẫn ảnh không được cập nhật vào DB cho Gmail: {}", gmail);
            } else {
                logger.info("Đường dẫn ảnh đã được cập nhật vào DB: {}", updatedAccount.getDuongDanAnh());
            }

            String sinhnhatStr = updatedAccount.getSinhnhat() != null
                    ? new SimpleDateFormat("yyyy-MM-dd").format(updatedAccount.getSinhnhat())
                    : "N/A";

            return ResponseEntity.ok(Map.of(
                "id", updatedAccount.getId(),
                "gmail", updatedAccount.getGmail(),
                "role", updatedAccount.getRole().name(),
                "hoten", updatedAccount.getHoTen(),
                "gioitinh", updatedAccount.isSex(),
                "sinhnhat", sinhnhatStr,
                "diachi", updatedAccount.getDiachi(),
                "duongDanAnh", updatedAccount.getDuongDanAnh(),
                "message", "Cập nhật ảnh đại diện thành công"
            ));
        } catch (Exception e) {
            logger.error("Lỗi khi upload ảnh đại diện cho: {}", gmail, e);
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi upload ảnh: " + e.getMessage()
            ));
        }
    }
    @PostMapping(value = "/taodiscount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDiscount(@RequestBody discountrequest request, @RequestParam("gmail") String gmail) {
        try {
            // Tìm account bằng gmail
            Account existingAccount = accountService.findByGmail(gmail);
            if (existingAccount == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            // Lấy idAccount từ existingAccount
            Long idAccount = existingAccount.getId();

            // Tạo và lưu Discount
            Discount newDiscount = new Discount();
            newDiscount.setIdAccount(idAccount);
            newDiscount.setMaKhuyenMai(request.getMaKhuyenMai());
            newDiscount.setGiaTien(request.getGiaTien());
            newDiscount = sanPhamService.addDiscount(newDiscount);

            // Gửi mã giảm giá tới email
            String message =  newDiscount.getMaKhuyenMai();
            accountService.senddiscount(gmail, message); 

            return ResponseEntity.ok(Map.of(
                "id", newDiscount.getId(),
                "idAccount", newDiscount.getIdAccount(),
                "maKhuyenMai", newDiscount.getMaKhuyenMai(),
                "gia tien",newDiscount.getGiaTien(),
                "message", "Tạo mã giảm giá thành công và đã gửi mã tới email"
            ));
        } catch (RuntimeException e) {
            logger.error("Lỗi khi tạo mã giảm giá: {}", e.getMessage());
            return ResponseEntity.status(400).body(Map.of(
                "message", "Lỗi khi tạo mã giảm giá: " + e.getMessage()
            ));
        }
    }
    @PostMapping(value = "/find-discount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findDiscount(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        // Kiểm tra token
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

        try {
            // Lấy gmail từ token
            String gmail = claims.getSubject();
            // Tìm account bằng gmail
            Account account = accountService.findByGmail(gmail);
            if (account == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy tài khoản với Gmail: " + gmail
                ));
            }

            // Lấy idAccount từ account
            Long idAccount = account.getId();

            // Lấy discountCode từ request
            String discountCode = request.get("discountCode");
            if (discountCode == null || discountCode.trim().isEmpty()) {
                return ResponseEntity.status(400).body(Map.of(
                    "message", "Mã giảm giá không được để trống"
                ));
            }

            // Tìm Discount bằng idAccount và discountCode
            Discount discount = accountService.timDiscount(idAccount, discountCode);
            if (discount == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy mã giảm giá: " + discountCode + " cho tài khoản này"
                ));
            }

            // Trả về thông tin Discount
            return ResponseEntity.ok(Map.of(
                "id", discount.getId(),
                "idAccount", discount.getIdAccount(),
                "maKhuyenMai", discount.getMaKhuyenMai(),
                "giaTien", discount.getGiaTien(),
                "message", "Tìm mã giảm giá thành công"
            ));

        } catch (Exception e) {
            logger.error("Lỗi khi tìm mã giảm giá: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "message", "Lỗi khi tìm mã giảm giá: " + e.getMessage()
            ));
        }
    }
}

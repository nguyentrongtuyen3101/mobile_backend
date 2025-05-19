package mobile.com.api.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mobile.com.api.DTO.LoginRequest;
import mobile.com.api.DTO.SignupRequest;
import mobile.com.api.dao.account_dao;
import mobile.com.api.entity.Account;
import mobile.com.api.entity.Discount;
import mobile.com.api.entity.Account.Role;
import mobile.com.api.entity.SanPham;

@Service
@Transactional
public class account_service_imp implements account_service {

    @Autowired
    private account_dao accountDao;
    @Autowired
    private  JavaMailSender mailSender;
    @Override
    public Optional<Account> login(String gmail, String rawPassword) {
        Account account = accountDao.findByGmail(gmail);
        // So sánh mật khẩu trực tiếp (không mã hóa)
        if (account != null && rawPassword.equals(account.getMatKhau())) {
            return Optional.of(account);
        }
        return Optional.empty();
    }

    @Override
    public boolean isAdmin(String gmail) {
        Account account = accountDao.findByGmail(gmail);
        return account != null && account.isAdmin();
    }

    @Override
    public Account findByGmail(String gmail) {
        return accountDao.findByGmail(gmail);
    }
    @Override
    public Account signup(SignupRequest request) {
        // Kiểm tra xem email đã tồn tại hay chưa bằng phương thức existsByGmail
        if (accountDao.existsByGmail(request.getGmail())) {
            throw new RuntimeException("Email đã tồn tại: " + request.getGmail());
        }

        // Tạo tài khoản mới
        Account newAccount = new Account();
        newAccount.setHoTen(request.getHoTen());
        newAccount.setGmail(request.getGmail());
        newAccount.setMatKhau(request.getMatKhau());
        // Nếu không truyền role, mặc định là USER
        newAccount.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        // Lưu tài khoản vào database
        accountDao.save(newAccount);

        return newAccount;
    }
   @Override 
   public void updatemk(Account account,String gmail1,String mkmoi)
   {
	   accountDao.updatemk(account, gmail1, mkmoi);
   }
   @Override
   public String sendOtp(String email) {
       String otp = String.valueOf((int) (Math.random() * 900000) + 100000); // OTP 6 chữ số

       // Gửi email trực tiếp
       SimpleMailMessage message = new SimpleMailMessage();
       message.setTo(email);
       message.setSubject("Your OTP for Password Reset");
       message.setText("Your OTP to reset your password is: " + otp + "\nThis OTP is valid for 5 minutes.");
       message.setFrom("tinhluc2@gmail.com"); // Thay bằng email của bạn

       mailSender.send(message);

       return otp; // Trả về OTP để frontend lưu
   }
   @Override
   public Account updateacc(LoginRequest account)
   {
	   return accountDao.updateacc(account);
   }
   @Override
   public void updateProfilePicture(String gmail, String duongDanAnh)
   {
	   accountDao.updateProfilePicture(gmail, duongDanAnh);
   }
   @Override
   public void senddiscount(String email,String discount)
   {
	   
       SimpleMailMessage message = new SimpleMailMessage();
       message.setTo(email);
       message.setSubject("mã discount");
       message.setText("chúc mừng bro đã nhận dc discount mua hàng giảm giá nhiều v ò: " + discount);
       message.setFrom("tinhluc2@gmail.com"); // Thay bằng email của bạn

       mailSender.send(message);
   }
   @Override
   public Discount timDiscount(long accountId,String discouncode)
   {
	   return accountDao.timDiscount(accountId, discouncode);
   }
}

package mobile.com.api.service;

import java.sql.Date;
import java.util.Optional;

import mobile.com.api.DTO.LoginRequest;
import mobile.com.api.DTO.SignupRequest;
import mobile.com.api.entity.Account;
import mobile.com.api.entity.Discount;
import mobile.com.api.entity.SanPham;

public interface account_service {
	public Optional<Account> login(String gmail, String rawPassword);
    boolean isAdmin(String gmail);
    Account findByGmail(String gmail);
    Account signup(SignupRequest request);
    void updatemk(Account account,String gmail1,String mkmoi);
    String sendOtp(String email);
    Account updateacc(LoginRequest request);
    void updateProfilePicture(String gmail, String duongDanAnh);
    public void senddiscount(String email,String discount);
    Discount timDiscount(long accountId,String discouncode);
}


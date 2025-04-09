package mobile.com.api.service;

import java.util.Optional;

import mobile.com.api.DTO.SignupRequest;
import mobile.com.api.entity.Account;
import mobile.com.api.entity.SanPham;

public interface account_service {
	public Optional<Account> login(String gmail, String rawPassword);
    boolean isAdmin(String gmail);
    Account findByGmail(String gmail);
    Account signup(SignupRequest request);
    void updatemk(Account account,String gmail1,String mkmoi);
    String sendOtp(String email);
}


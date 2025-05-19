package mobile.com.api.dao;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import mobile.com.api.DTO.LoginRequest;
import mobile.com.api.entity.Account;
import mobile.com.api.entity.Discount;
import mobile.com.api.entity.SanPham;

@Repository
public interface account_dao {
	Account findByGmail(String gmail); // Trả về Account trực tiếp
    void save(Account account);
    void updatemk(Account account,String gmail1,String mkmoi);
    boolean existsByGmail(String gmail);
    Account updateacc(LoginRequest account);
    void updateProfilePicture(String gmail, String duongDanAnh);
    Discount timDiscount(long accountId,String discouncode);
}

package mobile.com.api.dao;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mobile.com.api.DTO.LoginRequest;
import mobile.com.api.entity.Account;
import mobile.com.api.entity.SanPham;

@Repository
public class account_dao_imp implements account_dao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Account findByGmail(String gmail) {
    	try {
    		Session session = sessionFactory.getCurrentSession();
            Query<Account> query = session.createQuery("FROM Account WHERE gmail = :gmail", Account.class);
            query.setParameter("gmail", gmail);
            return query.uniqueResult();
		} catch (Exception e) {
			 return null;
		}  
    }

    @Override
    public void save(Account account) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(account);
    }
    @Override
    public boolean existsByGmail(String gmail) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Account WHERE gmail = :gmail", Long.class);
        query.setParameter("gmail", gmail);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }
    @Override
    public void updatemk(Account account, String gmail1, String mkmoi) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("UPDATE Account SET matkhau = :matkhaumoi WHERE gmail = :gmail");
        query.setParameter("gmail", gmail1);
        query.setParameter("matkhaumoi", mkmoi);
        query.executeUpdate(); 
    }
    public Account updateacc(LoginRequest account) {
        if (account == null || account.getGmail() == null) {
            throw new IllegalArgumentException("Account or gmail cannot be null");
        }
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("UPDATE Account SET hoTen = :hoTen, diachi = :diachi, sinhnhat = :sinhnhat, sex = :sex WHERE gmail = :gmail");
            query.setParameter("hoTen", account.getHoten());
            query.setParameter("diachi", account.getDiachi());
            query.setParameter("sinhnhat", account.getSinhnhat());
            query.setParameter("sex", account.isSex());
            query.setParameter("gmail", account.getGmail());
            query.executeUpdate();
            return findByGmail(account.getGmail()); 
        } catch (HibernateException e) {
            throw new RuntimeException("Error updating account: " + account.getGmail(), e);
        }
    }
    @Override
    public void updateProfilePicture(String gmail, String duongDanAnh) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("UPDATE Account SET duongDanAnh = :duongDanAnh WHERE gmail = :gmail");
            query.setParameter("duongDanAnh", duongDanAnh);
            query.setParameter("gmail", gmail);
            query.executeUpdate();
        } catch (HibernateException e) {
            throw new RuntimeException("Error updating profile picture for: " + gmail, e);
        }
    }
}


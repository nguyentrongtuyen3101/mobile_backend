package mobile.com.api.dao;

import mobile.com.api.entity.Account;
import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.SanPham.LoaiSanPham;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SanPhamDaoImpl implements SanPhamDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public void save(SanPham sanPham) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(sanPham);
    }

    @Override
    public SanPham findByTenSanPham(String tenSanPham) {
        Session session = sessionFactory.getCurrentSession();
        Query<SanPham> query = session.createQuery(
            "FROM SanPham WHERE tenSanPham = :tenSanPham", SanPham.class
        );
        query.setParameter("tenSanPham", tenSanPham);
        return query.uniqueResult();
    }
    @Override
    public List<SanPham> findByLoai(LoaiSanPham loai) {
        Session session = sessionFactory.getCurrentSession();
        Query<SanPham> query = session.createQuery(
            "FROM SanPham WHERE loai = :loai", SanPham.class
        );
        query.setParameter("loai", loai);
        return query.getResultList();
    }
    @Override
    public SanPham findByid(long id)
    {
    	try {
    		Session session = sessionFactory.getCurrentSession();
            Query<SanPham> query = session.createQuery("FROM SanPham WHERE id = :id", SanPham.class);
            query.setParameter("id", id);
            return query.uniqueResult();
		} catch (Exception e) {
			 return null;
		}
    }
    @Override
    public GioHang  addgiohang(GioHang gioHang)
    {
    	Session session = sessionFactory.getCurrentSession();
         session.saveOrUpdate(gioHang);
		return gioHang;
    }
    @Override
    public GioHang findGioHangByAccountAndSanPham(Long accountId, Long sanPhamId) {
        Session session = sessionFactory.getCurrentSession();
        Query<GioHang> query = session.createQuery(
            "FROM GioHang WHERE account.id = :accountId AND sanPham.id = :sanPhamId", GioHang.class
        );
        query.setParameter("accountId", accountId);
        query.setParameter("sanPhamId", sanPhamId);
        return query.uniqueResult();
    }
    @Override
    public List<GioHang> getGioHangByAccount(Long accountId) {
        Session session = sessionFactory.getCurrentSession();
        Query<GioHang> query = session.createQuery(
            "FROM GioHang WHERE account.id = :accountId", GioHang.class
        );
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }
    @Override
    public void deleteGioHang(Long gioHangId) {
        Session session = sessionFactory.getCurrentSession();
        GioHang gioHang = session.get(GioHang.class, gioHangId);
        if (gioHang != null) {
            session.delete(gioHang);
        }
    }
    
}
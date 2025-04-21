package mobile.com.api.dao;

import mobile.com.api.entity.Account;
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
}
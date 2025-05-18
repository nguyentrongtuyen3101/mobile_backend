package mobile.com.api.dao;

import mobile.com.api.DTO.orderrequest;
import mobile.com.api.entity.Account;
import mobile.com.api.entity.Discount;
import mobile.com.api.entity.GioHang;
import mobile.com.api.entity.LoaiSanPham;
import mobile.com.api.entity.OrderDetail;
import mobile.com.api.entity.SanPham;
import mobile.com.api.entity.YeuThich;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import mobile.com.api.entity.Order;
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
    public void saveLoaiSanPham(LoaiSanPham loaiSanPham) {
    	Session session=sessionFactory.getCurrentSession();
        session.saveOrUpdate(loaiSanPham);
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
    public List<SanPham> findByLoai(Long idLoai) {
        Session session = sessionFactory.getCurrentSession();
        Query<SanPham> query = session.createQuery(
            "SELECT sp FROM SanPham sp JOIN FETCH sp.idLoai lp WHERE lp.id = :idLoai ORDER BY sp.id DESC", SanPham.class
        );
        query.setParameter("idLoai", idLoai);
        return query.getResultList();
    }

    @Override
    public SanPham findByid(long id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query<SanPham> query = session.createQuery("FROM SanPham WHERE id = :id ", SanPham.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public GioHang addgiohang(GioHang gioHang) {
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
    @Override
    public YeuThich addThich(YeuThich yeuThich) {
    	Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(yeuThich);
        return yeuThich;
    }
    @Override
    public YeuThich findyeuthuichByAccountAndSanPham(Long accountId, Long sanPhamId)
    {
    	 Session session = sessionFactory.getCurrentSession();
         Query<YeuThich> query = session.createQuery(
             "FROM YeuThich WHERE account.id = :accountId AND sanPham.id = :sanPhamId", YeuThich.class
         );
         query.setParameter("accountId", accountId);
         query.setParameter("sanPhamId", sanPhamId);
         return query.uniqueResult();
    }
    @Override
    public List<YeuThich> getyeuthichByAccount(Long accountId)
    {
    	Session session = sessionFactory.getCurrentSession();
        Query<YeuThich> query = session.createQuery(
            "FROM YeuThich WHERE account.id = :accountId", YeuThich.class
        );
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }
    @Override
    public  void deleteyeuthich(long yeuthichid)
    {
    	 Session session = sessionFactory.getCurrentSession();
         YeuThich yeuThich = session.get(YeuThich.class, yeuthichid);
         if (yeuThich != null) {
             session.delete(yeuThich);
         }
    }
    @Override
    public LoaiSanPham findLoaiSanPhamById(Long id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query<LoaiSanPham> query = session.createQuery(
                "FROM LoaiSanPham WHERE id = :id", LoaiSanPham.class
            );
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public List<LoaiSanPham> findAllLoaiSanPham() { // Lấy danh sách tất cả loại sản phẩm từ database
        Session session = sessionFactory.getCurrentSession();
        Query<LoaiSanPham> query = session.createQuery(
            "FROM LoaiSanPham", LoaiSanPham.class
        );
        return query.getResultList();
    }
    @Override
    public Discount addDiscount(Discount discount)
    {
    	 Session session = sessionFactory.getCurrentSession();
         session.saveOrUpdate(discount);
         return discount;
    }
    @Override
    public Order addorrder(Order order)
    {
    	 Session session = sessionFactory.getCurrentSession();
         session.saveOrUpdate(order);
         return order;
    }
    @Override
    public OrderDetail addorderdetail(OrderDetail oderdetail)
    {
    	 Session session = sessionFactory.getCurrentSession();
         session.saveOrUpdate(oderdetail);
         return oderdetail;
    }
    
}
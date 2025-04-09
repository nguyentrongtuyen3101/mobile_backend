package mobile.com.api.entity;


import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hoa_don")
@Data
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    private double tongTien;

    @ManyToOne
    @JoinColumn(name = "ma_giam_gia_id")
    private MaGiamGia maGiamGia;
}
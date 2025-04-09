package mobile.com.api.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ma_giam_gia")
@Data
public class MaGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private double phanTramGiam;

    @Temporal(TemporalType.DATE)
    private Date ngayHetHan;

    @OneToMany(mappedBy = "maGiamGia", cascade = CascadeType.ALL)
    private List<HoaDon> hoaDons;
}

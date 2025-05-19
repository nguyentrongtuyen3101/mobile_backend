package mobile.com.api.DTO;

public class OrderDetailResponseDTO {
    private Long id; // ID của chi tiết đơn hàng
    private Long idOrder; // ID của đơn hàng
    private Long idSanpham; // ID của sản phẩm
    private int soluong; // Số lượng
    private double giatien; // Giá tiền
    private double tongtiensanpham; // Tổng tiền sản phẩm

    // Constructor mặc định
    public OrderDetailResponseDTO() {
    }

    // Constructor với tham số
    public OrderDetailResponseDTO(Long id, Long idOrder, Long idSanpham, int soluong, double giatien, double tongtiensanpham) {
        this.id = id;
        this.idOrder = idOrder;
        this.idSanpham = idSanpham;
        this.soluong = soluong;
        this.giatien = giatien;
        this.tongtiensanpham = tongtiensanpham;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdSanpham() {
        return idSanpham;
    }

    public void setIdSanpham(Long idSanpham) {
        this.idSanpham = idSanpham;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public double getGiatien() {
        return giatien;
    }

    public void setGiatien(double giatien) {
        this.giatien = giatien;
    }

    public double getTongtiensanpham() {
        return tongtiensanpham;
    }

    public void setTongtiensanpham(double tongtiensanpham) {
        this.tongtiensanpham = tongtiensanpham;
    }
}
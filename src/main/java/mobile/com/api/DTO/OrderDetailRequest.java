package mobile.com.api.DTO;

public class OrderDetailRequest {
    private Long idOrder; // Ánh xạ từ idOrder trong OrderDetail
    private Long idSanpham; // Ánh xạ từ idSanpham trong OrderDetail
    private int soluong; // Ánh xạ từ soluong trong OrderDetail
    private double giatien; // Ánh xạ từ giatien trong OrderDetail
    private double tongtiensanpham; // Ánh xạ từ tongtiensanpham trong OrderDetail

    // Constructor mặc định
    public OrderDetailRequest() {
    }

    // Constructor với tham số
    public OrderDetailRequest(Long idOrder, Long idSanpham, int soluong, double giatien, double tongtiensanpham) {
        this.idOrder = idOrder;
        this.idSanpham = idSanpham;
        this.soluong = soluong;
        this.giatien = giatien;
        this.tongtiensanpham = tongtiensanpham;
    }

    // Getters và Setters
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
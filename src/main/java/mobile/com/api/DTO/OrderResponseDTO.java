package mobile.com.api.DTO;

public class OrderResponseDTO {
    private Long id; // ID của đơn hàng
    private Long idAccount; // ID của tài khoản
    private Long idDiscount; // ID giảm giá (có thể null)
    private String hoTen; // Tên người nhận
    private String sdt; // Số điện thoại
    private String diachigiaohang; // Địa chỉ giao hàng
    private boolean phuongthucthanhtoan; // Phương thức thanh toán (true/false)
    private double tongtien; // Tổng tiền
    private int status; // Trạng thái đơn hàng

    // Constructor mặc định
    public OrderResponseDTO() {
    }

    // Constructor với tham số
    public OrderResponseDTO(Long id, Long idAccount, Long idDiscount, String hoTen, String sdt, String diachigiaohang,
                           boolean phuongthucthanhtoan, double tongtien, int status) {
        this.id = id;
        this.idAccount = idAccount;
        this.idDiscount = idDiscount;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diachigiaohang = diachigiaohang;
        this.phuongthucthanhtoan = phuongthucthanhtoan;
        this.tongtien = tongtien;
        this.status = status;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public Long getIdDiscount() {
        return idDiscount;
    }

    public void setIdDiscount(Long idDiscount) {
        this.idDiscount = idDiscount;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachigiaohang() {
        return diachigiaohang;
    }

    public void setDiachigiaohang(String diachigiaohang) {
        this.diachigiaohang = diachigiaohang;
    }

    public boolean isPhuongthucthanhtoan() {
        return phuongthucthanhtoan;
    }

    public void setPhuongthucthanhtoan(boolean phuongthucthanhtoan) {
        this.phuongthucthanhtoan = phuongthucthanhtoan;
    }

    public double getTongtien() {
        return tongtien;
    }

    public void setTongtien(double tongtien) {
        this.tongtien = tongtien;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
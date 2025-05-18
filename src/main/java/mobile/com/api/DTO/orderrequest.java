package mobile.com.api.DTO;

public class orderrequest {
    private Long idDiscount; // Có thể null, ánh xạ từ idDiscount trong Order
    private String hoTen; // Ánh xạ từ hoTen trong Order
    private String sdt; // Ánh xạ từ sdt trong Order
    private String diachigiaohang; // Ánh xạ từ diachigiaohang trong Order
    private boolean phuongthucthanhtoan; // Ánh xạ từ phuongthucthanhtoan trong Order
    private double tongtien; // Ánh xạ từ tongtien trong Order

    // Constructor mặc định
    public orderrequest() {
    }

    // Constructor với tham số
    public orderrequest(Long idDiscount, String hoTen, String sdt, String diachigiaohang, boolean phuongthucthanhtoan, double tongtien) {
        this.idDiscount = idDiscount;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diachigiaohang = diachigiaohang;
        this.phuongthucthanhtoan = phuongthucthanhtoan;
        this.tongtien = tongtien;
    }

    // Getters và Setters
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
}
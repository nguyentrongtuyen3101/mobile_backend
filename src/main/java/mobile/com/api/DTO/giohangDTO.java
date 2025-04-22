package mobile.com.api.DTO;

public class giohangDTO {
    private Long id;
    private Long accountId;
    private Long sanPhamId;
    private int soLuong;
    private String message;
    public giohangDTO() {
    }
    public giohangDTO(Long id, Long accountId, Long sanPhamId, int soLuong, String message) {
        this.id = id;
        this.accountId = accountId;
        this.sanPhamId = sanPhamId;
        this.soLuong = soLuong;
        this.message = message;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getSanPhamId() {
        return sanPhamId;
    }

    public void setSanPhamId(Long sanPhamId) {
        this.sanPhamId = sanPhamId;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
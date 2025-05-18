package mobile.com.api.DTO;

public class YeuThichDTO {
	private Long id;
    private Long accountId;
    private Long sanPhamId;
    private String message;
    public YeuThichDTO() {
    }
    public YeuThichDTO(Long id, Long accountId, Long sanPhamId, String message) {
        this.id = id;
        this.accountId = accountId;
        this.sanPhamId = sanPhamId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

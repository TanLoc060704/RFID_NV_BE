package namviet.rfid_api.dto;

import lombok.*;

@Data
public class AccountDTO {

    private int accountId;
    private String userName;
    private String email;
    private String password;
    private int roleId;
    private boolean isActive = true;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}

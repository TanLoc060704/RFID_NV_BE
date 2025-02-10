package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    int accountId;

    @Column(name = "user_name", length = 50)
    String userName;

    @Column(name = "email", length = 50, nullable = false)
    String email;

    @Column(name = "password", length = 100, nullable = false)
    String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    Role role;

    @Column(name = "is_active", columnDefinition = "true")
    boolean isActive;

    @Override
    public String toString() {
        return "AccountE{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }

    @OneToOne(mappedBy = "account",fetch = FetchType.LAZY)
    NhanVien nhanVien;
}


package individual.me.module.user;

import lombok.Data;

@Data
public class AddressBook {
    private Long id;
    private Long userId;
    private String receiverName;
    private String phoneNumber;
    private String address;
    private String label;
    private Integer isDefault;// 0 yes 1 no
    private Integer isDeleted;// 0 yes 1 no

}

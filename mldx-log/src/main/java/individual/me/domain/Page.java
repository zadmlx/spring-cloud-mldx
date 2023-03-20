package individual.me.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 每页的数据数量
     */
    private int sizePerPage;
}

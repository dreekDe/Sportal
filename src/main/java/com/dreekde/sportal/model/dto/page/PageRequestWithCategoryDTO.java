package com.dreekde.sportal.model.dto.page;

import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class PageRequestWithCategoryDTO {

    private long category;
    private int page;
    private int sizeOfPage;
}

package com.dreekde.sportal.model.dto.page;

import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class PageRequestByTitle {

    private String title;
    private int page;
    private int sizeOfPage;
}


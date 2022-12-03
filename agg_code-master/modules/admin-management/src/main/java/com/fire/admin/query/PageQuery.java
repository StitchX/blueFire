package com.fire.admin.query;

import lombok.Data;

@Data
public class PageQuery {
    public Integer page = 1;
    public Integer size = 10;
}

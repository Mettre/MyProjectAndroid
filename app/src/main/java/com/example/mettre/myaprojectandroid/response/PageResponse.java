package com.example.mettre.myaprojectandroid.response;

import java.util.List;

/**
 * Created by Mettre on 2018/8/14.
 */

public class PageResponse<T> {

    private boolean hasNextPage;
    private List<T> list;

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

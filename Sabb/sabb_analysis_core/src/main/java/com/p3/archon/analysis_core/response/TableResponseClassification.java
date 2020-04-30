package com.p3.archon.analysis_core.response;


import java.util.Set;


public class TableResponseClassification {
    private Set<String> categories;

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public TableResponseClassification(Set<String> categories) {
        this.categories = categories;
    }
}

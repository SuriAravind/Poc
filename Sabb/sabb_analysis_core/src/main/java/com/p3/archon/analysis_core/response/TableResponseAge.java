package com.p3.archon.analysis_core.response;


public class TableResponseAge {
    private Long totalRowCount;
    private Long qualifiedRowCount;

    public TableResponseAge(Long totalRowCount, Long qualifiedRowCount) {
        this.totalRowCount = totalRowCount;
        this.qualifiedRowCount = qualifiedRowCount;
    }

    public Long getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(Long totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public Long getQualifiedRowCount() {
        return qualifiedRowCount;
    }

    public void setQualifiedRowCount(Long qualifiedRowCount) {
        this.qualifiedRowCount = qualifiedRowCount;
    }
}

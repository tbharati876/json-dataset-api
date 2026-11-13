
package com.assignment.jsondataset.dto.response;

public class QueryDatasetResponse {

    private String datasetName;
    private long totalRecords;
    private String groupBy;
    private String sortBy;
    private String sortOrder;
    private Object data;

    public QueryDatasetResponse() {
    }

    public QueryDatasetResponse(
            String datasetName,
            long totalRecords,
            String groupBy,
            String sortBy,
            String sortOrder,
            Object data) {
        this.datasetName = datasetName;
        this.totalRecords = totalRecords;
        this.groupBy = groupBy;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        this.data = data;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

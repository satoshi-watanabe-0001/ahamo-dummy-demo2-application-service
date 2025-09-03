package com.ahamo.dummy.demo2.application.dto;

import java.util.List;

public class ApplicationListResponse {

    private List<ApplicationResponse> applications;
    private long total;
    private int page;
    private int limit;

    public ApplicationListResponse() {}

    public ApplicationListResponse(List<ApplicationResponse> applications, long total, int page, int limit) {
        this.applications = applications;
        this.total = total;
        this.page = page;
        this.limit = limit;
    }

    public List<ApplicationResponse> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationResponse> applications) {
        this.applications = applications;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

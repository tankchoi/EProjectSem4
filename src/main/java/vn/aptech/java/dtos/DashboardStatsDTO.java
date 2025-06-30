package vn.aptech.java.dtos;

public class DashboardStatsDTO {
    private long totalLaptops;
    private long totalParts;
    private long totalRequests;
    private long totalCustomers;
    private long totalStaff;
    private long pendingRequests;
    private long completedRequests;
    private long totalPartTypes;

    public DashboardStatsDTO() {
    }

    public DashboardStatsDTO(long totalLaptops, long totalParts, long totalRequests,
            long totalCustomers, long totalStaff, long pendingRequests,
            long completedRequests, long totalPartTypes) {
        this.totalLaptops = totalLaptops;
        this.totalParts = totalParts;
        this.totalRequests = totalRequests;
        this.totalCustomers = totalCustomers;
        this.totalStaff = totalStaff;
        this.pendingRequests = pendingRequests;
        this.completedRequests = completedRequests;
        this.totalPartTypes = totalPartTypes;
    }

    // Getters and Setters
    public long getTotalLaptops() {
        return totalLaptops;
    }

    public void setTotalLaptops(long totalLaptops) {
        this.totalLaptops = totalLaptops;
    }

    public long getTotalParts() {
        return totalParts;
    }

    public void setTotalParts(long totalParts) {
        this.totalParts = totalParts;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(long totalStaff) {
        this.totalStaff = totalStaff;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public long getCompletedRequests() {
        return completedRequests;
    }

    public void setCompletedRequests(long completedRequests) {
        this.completedRequests = completedRequests;
    }

    public long getTotalPartTypes() {
        return totalPartTypes;
    }

    public void setTotalPartTypes(long totalPartTypes) {
        this.totalPartTypes = totalPartTypes;
    }
}

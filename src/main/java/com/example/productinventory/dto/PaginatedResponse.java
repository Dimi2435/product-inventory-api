package com.example.productinventory.dto;

import java.util.List;

public class PaginatedResponse<T> {
  private List<T> items;
  private int currentPage;
  private int totalPages;
  private long totalItems;
  private int itemsPerPage;

  public PaginatedResponse(
      List<T> items, int currentPage, int totalPages, long totalItems, int itemsPerPage) {
    this.items = items;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalItems = totalItems;
    this.itemsPerPage = itemsPerPage;
  }

  // Getters
  public List<T> getItems() {
    return items;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public long getTotalItems() {
    return totalItems;
  }

  public int getItemsPerPage() {
    return itemsPerPage;
  }

  // Setters
  public void setItems(List<T> items) {
    this.items = items;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public void setTotalItems(long totalItems) {
    this.totalItems = totalItems;
  }

  public void setItemsPerPage(int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }
}

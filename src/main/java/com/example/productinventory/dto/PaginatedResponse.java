package com.example.productinventory.dto;

import java.util.List;

/**
 * A generic class to represent a paginated response.
 *
 * @param <T> the type of items in the paginated response
 */
public class PaginatedResponse<T> {
  private List<T> items; // The list of items in the current page
  private int currentPage; // The current page number (0-based)
  private int totalPages; // The total number of pages available
  private long totalItems; // The total number of items across all pages
  private int itemsPerPage; // The number of items per page

  /**
   * Constructs a PaginatedResponse with the specified parameters.
   *
   * @param items the list of items in the current page
   * @param currentPage the current page number (0-based)
   * @param totalPages the total number of pages available
   * @param totalItems the total number of items across all pages
   * @param itemsPerPage the number of items per page
   */
  public PaginatedResponse(
      List<T> items, int currentPage, int totalPages, long totalItems, int itemsPerPage) {
    this.items = items;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalItems = totalItems;
    this.itemsPerPage = itemsPerPage;
  }

  // Getters
  /**
   * Returns the list of items in the current page.
   *
   * @return the list of items
   */
  public List<T> getItems() {
    return items;
  }

  /**
   * Returns the current page number (0-based).
   *
   * @return the current page number
   */
  public int getCurrentPage() {
    return currentPage;
  }

  /**
   * Returns the total number of pages available.
   *
   * @return the total number of pages
   */
  public int getTotalPages() {
    return totalPages;
  }

  /**
   * Returns the total number of items across all pages.
   *
   * @return the total number of items
   */
  public long getTotalItems() {
    return totalItems;
  }

  /**
   * Returns the number of items per page.
   *
   * @return the number of items per page
   */
  public int getItemsPerPage() {
    return itemsPerPage;
  }

  // Setters
  /**
   * Sets the list of items in the current page.
   *
   * @param items the list of items to set
   */
  public void setItems(List<T> items) {
    this.items = items;
  }

  /**
   * Sets the current page number (0-based).
   *
   * @param currentPage the current page number to set
   */
  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * Sets the total number of pages available.
   *
   * @param totalPages the total number of pages to set
   */
  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  /**
   * Sets the total number of items across all pages.
   *
   * @param totalItems the total number of items to set
   */
  public void setTotalItems(long totalItems) {
    this.totalItems = totalItems;
  }

  /**
   * Sets the number of items per page.
   *
   * @param itemsPerPage the number of items per page to set
   */
  public void setItemsPerPage(int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }
}

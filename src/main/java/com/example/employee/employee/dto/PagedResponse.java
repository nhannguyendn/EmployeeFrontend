package com.example.employee.employee.dto;

import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Objects;

public class PagedResponse<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int size;               // pageSize
    private int number;             // current page (0-based)
    private int numberOfElements;   // items in current page
    private boolean first;
    private boolean last;
    private boolean empty;

    public PagedResponse() {}

    public PagedResponse(List<T> data, int totalPages, long totalElements,
                         int size, int number, int numberOfElements,
                         boolean first, boolean last, boolean empty) {
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
        this.numberOfElements = numberOfElements;
        this.first = first;
        this.last = last;
        this.empty = empty;
    }

    public static <T> PagedResponse<T> fromPage(Page<T> page) {
        return new PagedResponse<>(
            page.getContent(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.getSize(),
            page.getNumber(),
            page.getNumberOfElements(),
            page.isFirst(),
            page.isLast(),
            page.isEmpty()
        );
    }

    // Getters
    public List<T> getData() { return data; }
    public int getTotalPages() { return totalPages; }
    public long getTotalElements() { return totalElements; }
    public int getSize() { return size; }
    public int getNumber() { return number; }
    public int getNumberOfElements() { return numberOfElements; }
    public boolean isFirst() { return first; }
    public boolean isLast() { return last; }
    public boolean isEmpty() { return empty; }

    // Setters
    public void setData(List<T> data) { this.data = data; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public void setSize(int size) { this.size = size; }
    public void setNumber(int number) { this.number = number; }
    public void setNumberOfElements(int numberOfElements) { this.numberOfElements = numberOfElements; }
    public void setFirst(boolean first) { this.first = first; }
    public void setLast(boolean last) { this.last = last; }
    public void setEmpty(boolean empty) { this.empty = empty; }

    // toString / equals / hashCode (tùy chọn)
    @Override public String toString() {
        return "PagedResponse{data=" + data + ", totalPages=" + totalPages +
               ", totalElements=" + totalElements + ", size=" + size +
               ", number=" + number + ", numberOfElements=" + numberOfElements +
               ", first=" + first + ", last=" + last + ", empty=" + empty + "}";
    }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagedResponse)) return false;
        PagedResponse<?> that = (PagedResponse<?>) o;
        return totalPages == that.totalPages && totalElements == that.totalElements &&
               size == that.size && number == that.number &&
               numberOfElements == that.numberOfElements &&
               first == that.first && last == that.last && empty == that.empty &&
               Objects.equals(data, that.data);
    }
    @Override public int hashCode() {
        return Objects.hash(data, totalPages, totalElements, size, number, numberOfElements, first, last, empty);
    }
}

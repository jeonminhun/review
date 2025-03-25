package com.project.review.category.dto;

public enum categoryEnum {
    IT(1, "IT"),
    Beauty(2, "뷰티"),
    Life(3, "생활"),
    Food(4, "식품"),
    furniture(5, "가구"),
    medical(6, "의료"),
    stationery(7, "문구"),
    toy(8, "완구"),
    book(9, "도서/음반/DVD"),
    Health(10, "헬스"),
    Sports(11, "스포츠");

    private final int code;
    private final String categoryName;

    categoryEnum(int code, String categoryName) {
        this.code = code;
        this.categoryName = categoryName;
    }

    public int getCode() {
        return code;
    }

    public String getCategoryName() {
        return categoryName;
    }

    // 숫자로 역할 찾기
    public static String getCategoryByCode(int code) {
        for (categoryEnum role : categoryEnum.values()) {
            if (role.getCode() == code) {
                return role.getCategoryName();
            }
        }
        throw new IllegalArgumentException("Invalid role code: " + code);
    }

    // 역할로 숫자찾기
    public static int getCodeByCategory(String categoryName) {
        for (categoryEnum role : categoryEnum.values()) {
            if (role.getCategoryName().equals(categoryName)) {
                return role.getCode();
            }
        }
        throw new IllegalArgumentException("Invalid role category: " + categoryName);
    }
}
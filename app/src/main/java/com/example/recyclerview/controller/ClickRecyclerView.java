package com.example.recyclerview.controller;

public interface ClickRecyclerView {
    void onCustomClick(int position);

    void deletePeople(int position);

    void addAge(int position);

    void showMore(Object object, int position);
}

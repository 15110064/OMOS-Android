package com.kt3.android.interfaces;

public interface ViewHolderSubject {
    void add(ObserAdapterClick obserAdapterClick);
    void remove(ObserAdapterClick obserAdapterClick);
    void notifyChange();
}

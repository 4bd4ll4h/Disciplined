package com.example.disciplined;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class factory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Long mParam;


    public factory(Application application, Long param) {
        mApplication = application;
        mParam = param;

    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new NewTaskViewModle(mApplication,mParam);
    }
}

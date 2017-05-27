package com.example.dharkael.tweeter.dagger;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;


public class ViewModelFactory implements ViewModelProvider.Factory {

    private ViewModelSubComponent viewModelSubComponent;
    private final ArrayMap<Class, Callable<? extends ViewModel>> creators = new ArrayMap<>();

    public ViewModelFactory(ViewModelSubComponent viewModelSubComponent) {
        this.viewModelSubComponent = viewModelSubComponent;
        for (Method method : ViewModelSubComponent.class.getMethods()) {
            if (method.getParameterTypes().length > 0) continue;
            final Class<?> returnType = method.getReturnType();
            if (!ViewModel.class.isAssignableFrom(returnType)) continue;
            creators.put(returnType, () -> (ViewModel) method.invoke(viewModelSubComponent));
        }
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        final Callable<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        try {
            return (T) creator.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

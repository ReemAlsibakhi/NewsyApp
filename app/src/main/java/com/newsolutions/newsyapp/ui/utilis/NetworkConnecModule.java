package com.newsolutions.newsyapp.ui.utilis;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;


@InstallIn(ActivityComponent.class)
@Module
class NetworkConnecModule {
    @Provides
    NetworkConnectivity provideNetwork(@ActivityContext Context context) {
        return new NetworkConnectivity(new AppExecutors(), context);
    }
}
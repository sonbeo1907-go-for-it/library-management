package com.example.library.service.home;

import com.example.library.model.home.HomeDto;
import com.example.library.model.user.User;

public interface HomeService {
    HomeDto getHomeData(User currentUser);
}
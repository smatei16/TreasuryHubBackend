package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.UserStockDto;
import com.treasury.treasuryhub.exception.UserStockNotFoundException;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.model.UserStock;
import com.treasury.treasuryhub.repository.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStockService {

    @Autowired
    private UserStockRepository userStockRepository;

    @Autowired
    private UserService userService;

    public UserStock registerUserStock(UserStockDto userStockDto) {
        User user = userService.fetchCurrentUser();
        UserStock userStock = new UserStock();
        userStock.setUserId(user.getId());
        userStock.setSymbol(userStockDto.getSymbol());
        userStock.setQuantity(userStockDto.getQuantity());
        userStock.setPurchasePrice(userStockDto.getPurchasePrice());
        userStock.setPurchaseDate(userStockDto.getPurchaseDate());

        return userStockRepository.save(userStock);
    }

    public UserStock getUserStockById(int id) throws UserStockNotFoundException {
        return userStockRepository.findById(id)
                .orElseThrow(() -> new UserStockNotFoundException(id));
    }

    public List<UserStock> getUserStocks()  {
        User user = userService.fetchCurrentUser();
        return userStockRepository.getUserStockByUserId(user.getId());
    }

    public UserStock updateUserStock(UserStockDto userStockDto, int id) {
        return userStockRepository.findById(id)
                .map(userStock -> {
                    userStock.setSymbol(userStockDto.getSymbol());
                    userStock.setQuantity(userStockDto.getQuantity());
                    userStock.setPurchasePrice(userStockDto.getPurchasePrice());
                    userStock.setPurchaseDate(userStockDto.getPurchaseDate());
                    return userStockRepository.save(userStock);
                }).orElseGet(() -> registerUserStock(userStockDto));
    }

    public UserStock deleteUserStock(int id) throws UserStockNotFoundException {
        UserStock userStock = userStockRepository.findById(id)
                .orElseThrow(() -> new UserStockNotFoundException(id));
        userStockRepository.delete(userStock);
        return userStock;
    }

}

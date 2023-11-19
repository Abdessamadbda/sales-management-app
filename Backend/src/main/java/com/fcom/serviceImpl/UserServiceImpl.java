package com.fcom.serviceImpl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fcom.dbutil.DBUtil;
import com.fcom.model.sale;
import com.fcom.model.user;
import com.fcom.repository.UserRepository;
import com.fcom.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	Connection connection;
	int flag = 0;

	public UserServiceImpl(UserRepository userRepository) throws SQLException {
		this.userRepository = userRepository;
		connection = DBUtil.getConnection();
	}

	@Override
	public user loginValidation(String username, String password) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				String storedPassword = rs.getString("password");
				if (password.equals(storedPassword)) {
					user user = new user();
					user.setId(rs.getLong("id"));
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setNom_complet(rs.getString("nom_complet"));
					user.setVille(rs.getString("ville"));
					user.setPhone(rs.getString("phone"));
					user.setAgence(rs.getString("agence"));
					return user;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public user save(user user) {
		return userRepository.save(user);
	}

	@Override
	public user getUserById(Long id) {
		Optional<user> userOptional = userRepository.findById(id);
		return userOptional.orElse(null);
	}

	@Override
	public user getSellerInfo(Long sellerId) {
		Optional<user> seller = userRepository.findById(sellerId);
		return seller.orElse(null);
	}

	@Override
	public user updateUser(user user) {
		Optional<user> existingUser = userRepository.findById(user.getId());
		if (existingUser.isPresent()) {
			user updatedUser = existingUser.get();
			updatedUser.setNom_complet(user.getNom_complet());
			updatedUser.setVille(user.getVille());
			updatedUser.setAgence(user.getAgence());
			updatedUser.setPhone(user.getPhone());
			updatedUser.setUsername(user.getUsername());
			updatedUser.setPassword(user.getPassword());

			return userRepository.save(updatedUser);
		} else {
			return null;
		}
	}

	@Override
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}

}

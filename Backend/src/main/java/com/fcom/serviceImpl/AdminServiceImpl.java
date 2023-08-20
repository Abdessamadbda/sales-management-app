package com.fcom.serviceImpl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fcom.dbutil.DBUtil;
import com.fcom.model.admin;
import com.fcom.repository.AdminRepository;
import com.fcom.service.AdminService;
@Service
public class AdminServiceImpl implements AdminService{
	private final AdminRepository adminRepository;

	Connection connection;
    
	public AdminServiceImpl(AdminRepository adminRepository) throws SQLException{
        this.adminRepository = adminRepository;

		connection = DBUtil.getConnection();
	}
	
	@Override
	public admin loginValidation1(String username, String password) {
	    try {
	        PreparedStatement statement = connection.prepareStatement("SELECT * FROM admin WHERE username = ?");
	        statement.setString(1, username);
	        ResultSet rs = statement.executeQuery();
			
	        while (rs.next()) {
	        	String storedPassword = rs.getString("password");
				if (password.equals(storedPassword)) {
					admin admin = new admin();
	            	admin.setId(rs.getLong("id"));
	            	admin.setUsername(rs.getString("username"));
	            	admin.setPassword(rs.getString("password"));
					admin.setNom_complet(rs.getString("nom_complet"));
	                return admin;
	              
	            } else {
	                System.out.println("Invalid username/password");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	@Override
    public admin getAdminById(Long id) {
        Optional<admin> adminOptional = adminRepository.findById(id);
        return adminOptional.orElse(null);
    }
	

}

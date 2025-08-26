package com.ecommerce.action.admin;

import com.ecommerce.service.AdminService;

import java.util.Scanner;

public interface AdminAction  {
    void execute(AdminService adminService, Scanner scanner);

}

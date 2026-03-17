package controller;

import dao.DepartmentDAO;
import dao.DepartmentDTO;
import dao.MedicineDTO;
import dao.RoomDTO;           
import dao.RoomTypeDTO;       
import entity.User;
import service.UserService;
import service.DepartmentService;
import service.RoomService;       
import service.RoomTypeService;   

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.MedicineService;

@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
public class AdminController extends HttpServlet {

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
        String cp = request.getContextPath();
        
        // TẠM THỜI COMMENT BẢO MẬT SESSION ĐỂ DEV KHÔNG BỊ VĂNG RA INDEX
        /*
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            response.sendRedirect(cp + "/index.jsp");
            return;
        }
        */

        String action = request.getParameter("action");
        UserService userService = new UserService();
        DepartmentService departmentService = new DepartmentService();
        RoomService roomService = new RoomService();         
        RoomTypeService roomTypeService = new RoomTypeService(); 

        if (action == null || action.isEmpty()) {
            request.getRequestDispatcher("/component/admin/adminDashboard.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                // ==============================================================
                // 1. QUẢN LÝ USER (NGƯỜI DÙNG)
                // ==============================================================
                case "users": {
                    String query = request.getParameter("searchQuery");
                    String role = request.getParameter("role");
                    String statusParam = request.getParameter("statusFilter");
                    
                    if (role == null || role.isEmpty()) role = "doctor";
                    int statusFilter = (statusParam == null || statusParam.isEmpty()) ? 1 : Integer.parseInt(statusParam);

                    List<User> list = userService.searchUsers(query, statusFilter, role);
                    
                    request.setAttribute("userList", list);
                    request.setAttribute("activeTab", role.toLowerCase());
                    request.setAttribute("currentStatus", statusFilter);
                    request.getRequestDispatcher("/component/admin/manageUsers.jsp").forward(request, response);
                    break;
                }
                case "toggleUser": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    userService.toggleUserStatus(id);
                    response.sendRedirect("AdminController?action=users");
                    break;
                }
                case "deleteUser": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    userService.deleteUser(id);
                    response.sendRedirect("AdminController?action=users&msg=deleteSuccess");
                    break;
                }
                case "userTrash": {
                    List<User> list = userService.getDeletedUsers();
                    request.setAttribute("userList", list);
                    // ĐÃ SỬA LẠI TÊN FILE CHUẨN LÀ trashUsers.jsp
                    request.getRequestDispatcher("/component/admin/trashUsers.jsp").forward(request, response);
                    break;
                }
                case "restoreUser": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    userService.restoreUser(id);
                    response.sendRedirect("AdminController?action=userTrash");
                    break;
                }

                // ==============================================================
                // 2. QUẢN LÝ KHOA ĐIỀU TRỊ (DEPARTMENTS)
                // ==============================================================
                case "department": {
                    String keyword = request.getParameter("search");
                    String statusParam = request.getParameter("statusFilter");
                    String type = request.getParameter("type"); 

                    int statusFilter = (statusParam == null || statusParam.isEmpty()) ? -99 : Integer.parseInt(statusParam);
                    if (type == null) type = "all";

                    List<DepartmentDTO> list = departmentService.getDepartmentWithFilter(keyword, statusFilter, type);

                    request.setAttribute("departmentList", list);
                    request.setAttribute("activeTab", type); 
                    request.setAttribute("currentStatus", statusFilter); 
                    request.setAttribute("currentSearch", keyword);
                    
                    request.getRequestDispatcher("/component/admin/manageDepartment.jsp").forward(request, response);
                    break;
                }
                case "toggleDepartment": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    int status = Integer.parseInt(request.getParameter("status"));
                    if (status == 1) departmentService.deactivateDepartment(id);
                    else departmentService.activateDepartment(id);
                    response.sendRedirect("AdminController?action=department");
                    break;
                }
                case "deleteDepartment": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    departmentService.deleteDepartment(id);
                    response.sendRedirect("AdminController?action=department&msg=deleteSuccess");
                    break;
                }
                case "departmentTrash": {
                    List<DepartmentDTO> list = departmentService.getDeletedList(); 
                    request.setAttribute("departmentList", list);
                    request.getRequestDispatcher("/component/admin/departmentTrash.jsp").forward(request, response);
                    break;
                }
                case "restoreDepartment": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    departmentService.restoreDepartment(id); 
                    response.sendRedirect("AdminController?action=departmentTrash");
                    break;
                }

                // ==============================================================
                // 3. QUẢN LÝ PHÒNG BỆNH (ROOMS)
                // ==============================================================
              case "rooms": {
                    String keyword = request.getParameter("search");
                    String deptIdStr = request.getParameter("departmentId");
                    String status = request.getParameter("status"); 
                    String isActiveStr = request.getParameter("isActive");

                    int departmentId = (deptIdStr != null && !deptIdStr.isEmpty()) ? Integer.parseInt(deptIdStr) : 0;
                    
                    // ĐÃ FIX Ở DÒNG NÀY: Sửa -1 thành -99 (Tất cả)
                    int isActive = (isActiveStr != null && !isActiveStr.isEmpty()) ? Integer.parseInt(isActiveStr) : -99;
                    
                    if (status == null || status.isEmpty()) status = "all";

                    List<RoomDTO> roomList;
                    if ((keyword != null && !keyword.isEmpty()) || departmentId > 0 || !"all".equals(status) || isActive != -1) {
                        roomList = roomService.getRoomWithFilter(keyword, departmentId, status, isActive);
                    } else {
                        roomList = roomService.getListForAdmin(); 
                    }

                    request.setAttribute("roomList", roomList);
                    request.setAttribute("totalRooms", roomList.size()); 
                    
                    request.setAttribute("deptList", departmentService.getListForAdmin());
                    request.setAttribute("typeList", roomTypeService.getListForAdmin());

                    request.setAttribute("currentSearch", keyword);
                    request.setAttribute("currentDept", departmentId);
                    request.setAttribute("currentStatus", status);
                    request.setAttribute("currentIsActive", isActive);

                    request.getRequestDispatcher("/component/admin/manageRooms.jsp").forward(request, response);
                    break;
                }
                case "toggleRoom": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    int status = Integer.parseInt(request.getParameter("status"));
                    if (status == 1) roomService.deactivateRoom(id);
                    else roomService.activateRoom(id);
                    response.sendRedirect("AdminController?action=rooms");
                    break;
                }
                case "deleteRoom": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    roomService.deleteRoom(id);
                    response.sendRedirect("AdminController?action=rooms");
                    break;
                }
                case "roomTrash": {
                    List<RoomDTO> list = roomService.getDeletedRooms();
                    request.setAttribute("roomList", list);
                    // Nhớ tạo file trashRooms.jsp nhé
                    request.getRequestDispatcher("/component/admin/trashRooms.jsp").forward(request, response);
                    break;
                }
                case "restoreRoom": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    roomService.restoreRoom(id);
                    response.sendRedirect("AdminController?action=roomTrash");
                    break;
                }
// ==============================================================
// 4. QUẢN LÝ KHO DƯỢC (MEDICINES)
// ==============================================================
case "medicine": {
    String keyword = request.getParameter("search");
    MedicineService medicineService = new MedicineService(); // Khởi tạo service
    
    List<MedicineDTO> list;
    if (keyword != null && !keyword.isEmpty()) {
        list = medicineService.searchMedicines(keyword); // Tìm kiếm thuốc isActive = 1
    } else {
        list = medicineService.getActiveList(); // Lấy tất cả thuốc isActive = 1
    }
    
    request.setAttribute("medicineList", list);
    request.setAttribute("currentSearch", keyword);
    request.getRequestDispatcher("/component/admin/manageMedicine.jsp").forward(request, response);
    break;
}

case "deleteMedicine": {
    int id = Integer.parseInt(request.getParameter("id"));
    MedicineService medicineService = new MedicineService();
    medicineService.softDeleteMedicine(id); // Update isActive = 0
    response.sendRedirect("AdminController?action=medicine&msg=deleteSuccess");
    break;
}

case "medicineTrash": {
    MedicineService medicineService = new MedicineService();
    List<MedicineDTO> list = medicineService.getDeletedMedicines(); // Lấy thuốc isActive = 0
    request.setAttribute("trashList", list);
    request.getRequestDispatcher("/component/admin/trashMedicines.jsp").forward(request, response);
    break;
}

case "restoreMedicine": {
    int id = Integer.parseInt(request.getParameter("id"));
    MedicineService medicineService = new MedicineService();
    medicineService.restoreMedicine(id); // Update isActive = 1
    response.sendRedirect("AdminController?action=medicineTrash&msg=restoreSuccess");
    break;
}
                default:
                    response.sendRedirect(cp + "/AdminController");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(cp + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
      
        String cp = request.getContextPath();
        String action = request.getParameter("action");
        
        UserService userService = new UserService();
        DepartmentService departmentService = new DepartmentService();
        RoomService roomService = new RoomService(); 

        try {
            // ==============================================================
            // 1. QUẢN LÝ USER
            // ==============================================================
            if ("addUser".equals(action)) {
                String name = request.getParameter("userName");
                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                String role = request.getParameter("role");
                userService.addUserByAdmin(name, email, pass, role);
                response.sendRedirect(cp + "/AdminController?action=users");

            } else if ("updateUser".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("userName");
                String email = request.getParameter("email");
                String role = request.getParameter("role");
                userService.updateUserByAdmin(id, name, email, role);
                response.sendRedirect(cp + "/AdminController?action=users");

            // ==============================================================
            // 2. QUẢN LÝ KHOA ĐIỀU TRỊ
            // ==============================================================
            } else if ("addDepartment".equals(action)) {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                departmentService.createNewDepartment(name, description);
                response.sendRedirect("AdminController?action=department");

            } else if ("updateDepartment".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                departmentService.updateDepartment(id, name, description);
                response.sendRedirect("AdminController?action=department");

            // ==============================================================
            // 3. QUẢN LÝ PHÒNG BỆNH
            // ==============================================================
            } else if ("addRoom".equals(action)) {
                String deptStr = request.getParameter("departmentId");
                int departmentId = (deptStr == null || deptStr.trim().isEmpty() || "null".equals(deptStr)) ? 0 : Integer.parseInt(deptStr);
                
                int roomType = Integer.parseInt(request.getParameter("roomType"));
                int roomNumber = Integer.parseInt(request.getParameter("roomNumber"));

                boolean success = roomService.createNewRoom(departmentId, roomType, roomNumber);
                response.sendRedirect("AdminController?action=rooms" + (success ? "" : "&error=addFailed"));

            } else if ("updateRoom".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                
                String oldDeptStr = request.getParameter("oldDepartmentId");
                int oldDepartmentId = (oldDeptStr == null || oldDeptStr.trim().isEmpty() || "null".equals(oldDeptStr)) ? 0 : Integer.parseInt(oldDeptStr);
                
                String newDeptStr = request.getParameter("departmentId");
                int newDepartmentId = (newDeptStr == null || newDeptStr.trim().isEmpty() || "null".equals(newDeptStr)) ? 0 : Integer.parseInt(newDeptStr);
                
                String oldRoomNumStr = request.getParameter("oldRoomNumber");
                int oldRoomNumber = (oldRoomNumStr == null || oldRoomNumStr.trim().isEmpty() || "null".equals(oldRoomNumStr)) ? 0 : Integer.parseInt(oldRoomNumStr);

                int newRoomNumber = Integer.parseInt(request.getParameter("roomNumber")); 
                int roomType = Integer.parseInt(request.getParameter("roomType"));
                String status = request.getParameter("status"); 

                boolean success = roomService.updateRoomInfo(id, oldDepartmentId, newDepartmentId, oldRoomNumber, newRoomNumber, roomType, status);
                response.sendRedirect("AdminController?action=rooms" + (success ? "" : "&error=updateFailed"));
            
            } else if ("toggleRoom".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int status = Integer.parseInt(request.getParameter("status"));
                if (status == 1) roomService.deactivateRoom(id);
                else roomService.activateRoom(id);
                response.sendRedirect("AdminController?action=rooms");
            } else if ("addMedicine".equals(action)) {
    String name = request.getParameter("name");
    String unit = request.getParameter("unit");
    double price = Double.parseDouble(request.getParameter("price"));
    int stock = Integer.parseInt(request.getParameter("stockQuantity"));
    String desc = request.getParameter("description");

    MedicineService medicineService = new MedicineService();
    // Đổi thành createNewMedicine
    medicineService.createNewMedicine(name, unit, price, stock, desc); 
    response.sendRedirect("AdminController?action=medicine");

} else if ("updateMedicine".equals(action)) {
    int id = Integer.parseInt(request.getParameter("id"));
    String name = request.getParameter("name");
    String unit = request.getParameter("unit");
    double price = Double.parseDouble(request.getParameter("price"));
    int stock = Integer.parseInt(request.getParameter("stockQuantity"));
    String desc = request.getParameter("description");

    MedicineService medicineService = new MedicineService();
    // Truyền đúng 5 tham số: id, name, unit, price, desc
    medicineService.updateMedicineInfo(id, name, unit, price, desc);
    response.sendRedirect("AdminController?action=medicine");
}
            

        }
        // ==============================================================
// 4. QUẢN LÝ KHO DƯỢC
// ==============================================================
catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(cp + "/error.jsp");
        }
    }
}
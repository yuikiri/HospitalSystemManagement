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
import service.MedicineService;
import service.ShiftService; // Đã thêm import ShiftService

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        
       
        userService.toggleUserStatus(id);
        
   
        User updatedUser = userService.getUserById(id);
        
        
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().write(String.valueOf(updatedUser.getIsActive()));
        
    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("error");
    }
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
                    MedicineService medicineService = new MedicineService(); 
                    
                    List<MedicineDTO> list;
                    if (keyword != null && !keyword.isEmpty()) {
                        list = medicineService.searchMedicines(keyword); 
                    } else {
                        list = medicineService.getActiveList(); 
                    }
                    
                    request.setAttribute("medicineList", list);
                    request.setAttribute("currentSearch", keyword);
                    request.getRequestDispatcher("/component/admin/manageMedicine.jsp").forward(request, response);
                    break;
                }
                case "deleteMedicine": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    MedicineService medicineService = new MedicineService();
                    medicineService.softDeleteMedicine(id); 
                    response.sendRedirect("AdminController?action=medicine&msg=deleteSuccess");
                    break;
                }
                case "medicineTrash": {
                    MedicineService medicineService = new MedicineService();
                    List<MedicineDTO> list = medicineService.getDeletedMedicines(); 
                    request.setAttribute("trashList", list);
                    request.getRequestDispatcher("/component/admin/trashMedicines.jsp").forward(request, response);
                    break;
                }
                case "restoreMedicine": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    MedicineService medicineService = new MedicineService();
                    medicineService.restoreMedicine(id); 
                    response.sendRedirect("AdminController?action=medicineTrash&msg=restoreSuccess");
                    break;
                }

                // ==============================================================
                // 5. QUẢN LÝ CA TRỰC (SHIFTS) - TÍCH HỢP MỚI
                // ==============================================================
                case "shifts": {
                    String type = request.getParameter("type");
                    String emailSearch = null;
                    if ("doctor".equals(type)) {
                        emailSearch = request.getParameter("docEmail");
                    } else if ("staff".equals(type)) {
                        emailSearch = request.getParameter("staffEmail");
                    }

                    String weekOffsetStr = request.getParameter("weekOffset");
                    int weekOffset = (weekOffsetStr == null || weekOffsetStr.isEmpty()) ? 0 : Integer.parseInt(weekOffsetStr);
                    
                    ShiftService shiftService = new ShiftService();

                    if (emailSearch != null && !emailSearch.trim().isEmpty()) {
                        if ("doctor".equals(type)) {
                            request.setAttribute("docSchedule", shiftService.getScheduleMatrix(emailSearch, "doctor", weekOffset));
                            request.setAttribute("docEmail", emailSearch);
                        } else if ("staff".equals(type)) {
                            request.setAttribute("staffSchedule", shiftService.getScheduleMatrix(emailSearch, "staff", weekOffset));
                            request.setAttribute("staffEmail", emailSearch);
                        }
                    }

                    request.setAttribute("activeType", type);
                    request.setAttribute("currentWeekOffset", weekOffset);
                    
                    // --- ĐÂY LÀ ĐOẠN QUAN TRỌNG NHẤT ---
                    // 1. Nạp danh sách phòng vào trước
                    request.setAttribute("roomList", roomService.getListForAdmin());
                    
                    // 2. Chuyển sang trang JSP (CHỈ GỌI 1 LẦN DUY NHẤT Ở CUỐI)
                    request.getRequestDispatcher("/component/admin/manageShifts.jsp").forward(request, response);
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

            // ==============================================================
            // 4. QUẢN LÝ KHO DƯỢC
            // ==============================================================
            } else if ("addMedicine".equals(action)) {
                String name = request.getParameter("name");
                String unit = request.getParameter("unit");
                double price = Double.parseDouble(request.getParameter("price"));
                int stock = Integer.parseInt(request.getParameter("stockQuantity"));
                String desc = request.getParameter("description");

                MedicineService medicineService = new MedicineService();
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
                medicineService.updateMedicineInfo(id, name, unit, price, desc);
                response.sendRedirect("AdminController?action=medicine");

            // ==============================================================
            // 5. QUẢN LÝ CA TRỰC (SHIFTS) - TÍCH HỢP MỚI
            // ==============================================================
            } else if ("updateShift".equals(action)) {
                String personType = request.getParameter("personType"); 
                String email = request.getParameter("targetEmail"); // Email của bác sĩ/nhân viên đang được sửa lịch
                
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                String roleNote = request.getParameter("roleNote");
                int dayOfWeek = Integer.parseInt(request.getParameter("dayOfWeek"));
                int shiftNumber = Integer.parseInt(request.getParameter("shiftNumber"));
                int weekOffset = Integer.parseInt(request.getParameter("weekOffset"));

                ShiftService shiftService = new ShiftService();
                boolean success = shiftService.saveOrUpdateShift(personType, email, roomId, roleNote, dayOfWeek, shiftNumber, weekOffset);
                
                // Trả về đúng email và tab hiện tại để giao diện không bị load lại mất dữ liệu
                String emailParam = "doctor".equals(personType) ? "&docEmail=" + email : "&staffEmail=" + email;
                response.sendRedirect(cp + "/AdminController?action=shifts&type=" + personType + emailParam + "&weekOffset=" + weekOffset + (success ? "" : "&error=true"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(cp + "/error.jsp");
        }
    }
}
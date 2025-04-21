package com.tdtu.DesignPattern.Jeweluxe.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tdtu.DesignPattern.Jeweluxe.model.Category;
import com.tdtu.DesignPattern.Jeweluxe.model.Product;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.service.CartService;
import com.tdtu.DesignPattern.Jeweluxe.service.CategoryService;
import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import com.tdtu.DesignPattern.Jeweluxe.service.ProductService;
import com.tdtu.DesignPattern.Jeweluxe.service.UserService;
import com.tdtu.DesignPattern.Jeweluxe.util.CommonUtil;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // Đánh dấu lớp này là một Spring Bean, mặc định là Singleton
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(OrderService orderService) {
        this.orderService = orderService;
         System.out.println("Creating AdminController instance: " + this.hashCode()); // Log khi tạo
         System.out.println("Injecting OrderService instance: " + orderService.hashCode()); // Log instance được inject
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User User = userService.getUserByEmail(email);
            m.addAttribute("user", User);
            Integer countCart = cartService.getCountCart(User.getId());
            m.addAttribute("countCart", countCart);
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m) {
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(Model m, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        // m.addAttribute("categorys", categoryService.getAllCategory());
        Page<Category> page = categoryService.getAllCategorPagination(pageNo, pageSize);
        List<Category> categorys = page.getContent();
        m.addAttribute("categorys", categorys);

        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());

        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                               HttpSession session) throws IOException {

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        Boolean existCategory = categoryService.existCategory(category.getName());

        if (existCategory) {
            session.setAttribute("errorMsg", "Category Name already exists");
        } else {

            Category saveCategory = categoryService.saveCategory(category);

            if (ObjectUtils.isEmpty(saveCategory)) {
                session.setAttribute("errorMsg", "Not saved ! internal server error");
            } else {

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("succMsg", "Saved successfully");
            }
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        Boolean deleteCategory = categoryService.deleteCategory(id);

        if (deleteCategory) {
            session.setAttribute("succMsg", "category delete success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model m) {
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                                 HttpSession session) throws IOException {

        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(category)) {

            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imageName);
        }

        Category updateCategory = categoryService.saveCategory(oldCategory);

        if (!ObjectUtils.isEmpty(updateCategory)) {

            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("succMsg", "Category update success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/loadEditCategory/" + category.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
                              HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        Product saveProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {

            File saveFile = new ClassPathResource("static/img").getFile();

            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                    + image.getOriginalFilename());

            // System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg", "Product Saved Success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model m, @RequestParam(defaultValue = "") String ch,
                                  @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {


        Page<Product> page = null;
        if (ch != null && ch.length() > 0) {
            page = productService.searchProductPagination(pageNo, pageSize, ch);
        } else {
            page = productService.getAllProductsPagination(pageNo, pageSize);
        }
        m.addAttribute("products", page.getContent());

        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());

        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        Boolean deleteProduct = productService.deleteProduct(id);
        if (deleteProduct) {
            session.setAttribute("succMsg", "Product delete success");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model m) {
        m.addAttribute("product", productService.getProductById(id));
        m.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
                                HttpSession session, Model m) {

        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            session.setAttribute("errorMsg", "invalid Discount");
        } else {
            Product updateProduct = productService.updateProduct(product, image);
            if (!ObjectUtils.isEmpty(updateProduct)) {
                session.setAttribute("succMsg", "Product update success");
            } else {
                session.setAttribute("errorMsg", "Something wrong on server");
            }
        }
        return "redirect:/admin/editProduct/" + product.getId();
    }

    @GetMapping("/users")
    public String getAllUsers(Model m, @RequestParam Integer type) {
        List<User> users = null;
        if (type == 1) {
            users = userService.getUsers("ROLE_USER");
        } else {
            users = userService.getUsers("ROLE_ADMIN");
        }
        m.addAttribute("userType",type);
        m.addAttribute("users", users);
        return "/admin/users";
    }

    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,@RequestParam Integer type, HttpSession session) {
        Boolean f = userService.updateAccountStatus(id, status);
        if (f) {
            session.setAttribute("succMsg", "Account Status Updated");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/users?type="+type;
    }

    @GetMapping("/orders")
    public String getAllOrders(Model m, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
//		List<OrderItem> allOrders = orderService.getAllOrders();
//		m.addAttribute("orders", allOrders);
//		m.addAttribute("srch", false);

        Page<OrderItem> page = orderService.getAllOrdersPagination(pageNo, pageSize);
        m.addAttribute("orders", page.getContent());
        m.addAttribute("srch", false);

        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());

        return "/admin/orders";
    }

    // Trong AdminController.java

//    @PostMapping("/update-order-status")
//    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, RedirectAttributes redirectAttributes) {
//
//        OrderStatus[] values = OrderStatus.values();
//        String status = null;
//        for (OrderStatus orderSt : values) {
//            if (orderSt.getId().equals(st)) {
//                status = orderSt.getName();
//                break;
//            }
//        }
//        if(status == null) {
//            redirectAttributes.addFlashAttribute("errorMsg", "Invalid order status selected.");
//            return "redirect:/admin/orders";
//        }
//
//        try {
//            OrderItem updatedOrder = orderService.updateOrderStatus(id, status); // Gọi đúng phương thức trả về OrderItem
//
//            // Kiểm tra kq có null k
//            if (updatedOrder != null) {
//                redirectAttributes.addFlashAttribute("succMsg", "Item Status Updated to " + status + " for Item ID: " + id);
//
//                try {
//                } catch (Exception mailEx) {
//                    System.err.println("Error sending order update email: " + mailEx.getMessage());
//                }
//
//            } else {
//                redirectAttributes.addFlashAttribute("errorMsg", "Order status not updated. Item not found or server error.");
//            }
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMsg", "Error updating order status: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return "redirect:/admin/orders";
//    }

    // Trong AdminController.java
//    @PostMapping("/update-order-status")
//    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, RedirectAttributes redirectAttributes) {
//        OrderStatus newStatus = OrderStatus.fromId(st);
//
//        if (newStatus == null) {
//            redirectAttributes.addFlashAttribute("errorMsg", "Trạng thái đơn hàng không hợp lệ.");
//            return "redirect:/admin/orders";
//        }
//
//        try {
//            OrderItem updatedOrder = orderService.updateOrderStatus(id, newStatus);
//
//            if (updatedOrder != null) {
//                redirectAttributes.addFlashAttribute("succMsg", "Trạng thái đơn hàng đã được cập nhật thành công thành: " + newStatus.getName());
//            } else {
//                redirectAttributes.addFlashAttribute("errorMsg", "Không thể cập nhật trạng thái. Không tìm thấy đơn hàng hoặc có lỗi máy chủ.");
//            }
//        } catch (IllegalStateException e) { // Bắt lỗi nếu service có kiểm tra logic chuyển đổi
//            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi cập nhật: " + e.getMessage());
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi khi cập nhật trạng thái đơn hàng.");
//            e.printStackTrace();
//        }
//        return "redirect:/admin/orders";
//    }

    // Trong AdminController.java

    @PostMapping("/ship-order") // Hoặc GetMapping tùy thiết kế URL
    public String shipOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.shipOrder(id); // Gọi phương thức nghiệp vụ mới
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được cập nhật trạng thái gửi đi.");
        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống.");
            // Log lỗi
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/cancel-order")
    public String cancelOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            // Khi gọi orderService.cancelOrder(), chúng ta đang gọi đến
            // đối tượng OrderServiceImpl Singleton duy nhất.
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được hủy.");
        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống.");
            // Log lỗi
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/pack-order") // URL để đóng gói đơn hàng
    public String packOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.packOrder(id); // Gọi service đóng gói
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được cập nhật trạng thái đóng gói.");
        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi khi đóng gói: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống khi đóng gói đơn.");
            e.printStackTrace();
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/deliver-order") // URL để xác nhận giao hàng
    public String deliverOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deliverOrder(id); // Gọi service giao hàng
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được cập nhật trạng thái đã giao.");
        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi khi xác nhận giao hàng: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống khi xác nhận giao hàng.");
            e.printStackTrace();
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/receive-order") // URL để xác nhận nhận đơn hàng
    public String receiveOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.receiveOrder(id); // Gọi service nhận đơn hàng
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được cập nhật trạng thái đã nhận.");
        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi khi xác nhận nhận đơn: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống khi xác nhận nhận đơn.");
            e.printStackTrace();
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/search-order")
    public String searchProduct(@RequestParam String orderId, Model m, HttpSession session,
                                @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        if (orderId != null && orderId.length() > 0) {

            OrderItem order = orderService.getOrdersByOrderId(orderId.trim());

            if (ObjectUtils.isEmpty(order)) {
                session.setAttribute("errorMsg", "Incorrect orderId");
                m.addAttribute("orderDtls", null);
            } else {
                m.addAttribute("orderDtls", order);
            }

            m.addAttribute("srch", true);
        } else {
//			List<OrderItem> allOrders = orderService.getAllOrders();
//			m.addAttribute("orders", allOrders);
//			m.addAttribute("srch", false);

            Page<OrderItem> page = orderService.getAllOrdersPagination(pageNo, pageSize);
            m.addAttribute("orders", page);
            m.addAttribute("srch", false);

            m.addAttribute("pageNo", page.getNumber());
            m.addAttribute("pageSize", pageSize);
            m.addAttribute("totalElements", page.getTotalElements());
            m.addAttribute("totalPages", page.getTotalPages());
            m.addAttribute("isFirst", page.isFirst());
            m.addAttribute("isLast", page.isLast());

        }
        return "/admin/orders";

    }

    @PostMapping("/save-admin")
    public String saveAdmin(@ModelAttribute User user, @RequestParam("img") MultipartFile file, HttpSession session)
            throws IOException {

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        User saveUser = userService.saveAdmin(user);

        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                        + file.getOriginalFilename());

//				System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Register successfully");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/add-admin";
    }

    @GetMapping("/profile")
    public String profile() {
        return "/admin/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute User user, @RequestParam MultipartFile img, HttpSession session) {
        User updateUserProfile = userService.updateUserProfile(user, img);
        if (ObjectUtils.isEmpty(updateUserProfile)) {
            session.setAttribute("errorMsg", "Profile not updated");
        } else {
            session.setAttribute("succMsg", "Profile Updated");
        }
        return "redirect:/admin/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
                                 HttpSession session) {
        User loggedInUserDetails = commonUtil.getLoggedInUserDetails(p);

        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

        if (matches) {
            String encodePassword = passwordEncoder.encode(newPassword);
            loggedInUserDetails.setPassword(encodePassword);
            User updateUser = userService.updateUser(loggedInUserDetails);
            if (ObjectUtils.isEmpty(updateUser)) {
                session.setAttribute("errorMsg", "Password not updated !! Error in server");
            } else {
                session.setAttribute("succMsg", "Password Updated sucessfully");
            }
        } else {
            session.setAttribute("errorMsg", "Current Password incorrect");
        }

        return "redirect:/admin/profile";
    }
    // --- Hiển thị Form Add User/Admin ---
    @GetMapping("/add-user")
    public String loadAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("formTitle", "Add New Customer"); // Bạn có thể dùng title này hoặc cái trong HTML
        model.addAttribute("isAdminForm", false); // Đặt là false cho form add customer
        model.addAttribute("userType", 1);
        return "admin/add_user"; // Trỏ đến file templates/admin/add_user.html
    }

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam("img") MultipartFile file,
                           @RequestParam Integer userType,
                           HttpSession session) {
        try {
            // Kiểm tra email tồn tại
            Boolean existsEmail = userService.existsEmail(user.getEmail());
            if (existsEmail) {
                session.setAttribute("errorMsg", "Email already exists");
                // Quay lại form tương ứng
                return (userType == 1) ? "redirect:/admin/add-user" : "redirect:/admin/add-admin";
            }

            // Đặt vai trò và mã hóa mật khẩu
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setIsEnable(true); // Mặc định là active khi tạo mới
            if (userType == 2) {
                user.setRole("ROLE_ADMIN");
            } else {
                user.setRole("ROLE_USER"); // Mặc định là user
            }


            // Xử lý ảnh
            String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
            user.setProfileImage(imageName);

            User savedUser = userService.saveUser(user); // Giả sử có phương thức này

            if (savedUser != null) {
                // Lưu ảnh nếu có
                if (!file.isEmpty()) {
                    commonUtil.uploadFile(file, "profile_img"); // Sử dụng CommonUtil nếu có
                    // Hoặc dùng code copy file như cũ:
                    /*
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + file.getOriginalFilename());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    */
                }
                session.setAttribute("succMsg", (userType == 1 ? "Customer" : "Admin") + " added successfully");
            } else {
                session.setAttribute("errorMsg", "Something went wrong on server");
            }

        } catch (IOException e) {
            session.setAttribute("errorMsg", "Error uploading image: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Server error: " + e.getMessage());
            e.printStackTrace();
        }
        // Quay về trang danh sách tương ứng
        return "redirect:/admin/users?type=" + userType;
    }


    // --- Hiển thị Form Edit User/Admin ---
    @GetMapping("/edit-user/{id}")
    public String loadEditUserForm(@PathVariable Integer id,
                                   @RequestParam Integer type,
                                   Model model, HttpSession session) {
        User user = userService.getUserById(id);
        if (user == null) {
            session.setAttribute("errorMsg", "User not found with ID: " + id);
            return "redirect:/admin/users?type=" + type;
        }
        model.addAttribute("user", user);
        model.addAttribute("formTitle", "Edit " + (type == 1 ? "Customer" : "Admin"));
        model.addAttribute("userType", type); // Truyền type để form biết
        return "admin/edit_user"; // Trỏ đến file edit_user.html
    }

    // --- Xử lý Cập nhật User/Admin ---
    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute User userFromForm,
                             @RequestParam("img") MultipartFile file,
                             @RequestParam Integer userType, // Lấy type từ input ẩn
                             @RequestParam(name = "newPassword", required = false) String newPassword,
                             HttpSession session) {
        try {
            User existingUser = userService.getUserById(userFromForm.getId());
            if (existingUser == null) {
                session.setAttribute("errorMsg", "User not found for update.");
                return "redirect:/admin/users?type=" + userType;
            }

            existingUser.setName(userFromForm.getName());
            existingUser.setMobileNumber(userFromForm.getMobileNumber());
            existingUser.setAddress(userFromForm.getAddress());
            existingUser.setCity(userFromForm.getCity());
            existingUser.setState(userFromForm.getState());
            existingUser.setPincode(userFromForm.getPincode());
            existingUser.setIsEnable(userFromForm.getIsEnable());

            if (newPassword != null && !newPassword.isEmpty()) {
                if (newPassword.length() >= 6) { // Kiểm tra độ dài tối thiểu
                    existingUser.setPassword(passwordEncoder.encode(newPassword));
                } else {
                    session.setAttribute("errorMsg", "New password must be at least 6 characters long.");
                    return "redirect:/admin/edit-user/" + userFromForm.getId() + "?type=" + userType;
                }
            }

            String oldImageName = existingUser.getProfileImage();
            if (!file.isEmpty()) {
                String newImageName = file.getOriginalFilename();
                existingUser.setProfileImage(newImageName);
                commonUtil.uploadFile(file, "profile_img");

                if (oldImageName != null && !oldImageName.equals("default.jpg") && !oldImageName.equals(newImageName)) {
                    commonUtil.deleteFile(oldImageName, "profile_img");
                }
            }

            User updatedUser = userService.updateUser(existingUser);

            if (updatedUser != null) {
                session.setAttribute("succMsg", (userType == 1 ? "Customer" : "Admin") + " details updated successfully");
            } else {
                session.setAttribute("errorMsg", "Something went wrong on server during update");
            }

        } catch (IOException e) {
            session.setAttribute("errorMsg", "Error uploading new image: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Server error during update: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/admin/users?type=" + userType;
    }


    // --- Xử lý Xóa User/Admin ---
    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Integer id, @RequestParam Integer type, HttpSession session) {
        try {
            // Có thể thêm kiểm tra không cho xóa chính mình hoặc admin cuối cùng
            boolean isDeleted = userService.deleteUser(id);
            if (isDeleted) {
                session.setAttribute("succMsg", "User deleted successfully.");
            } else {
                session.setAttribute("errorMsg", "Could not delete user. User might not exist or have related data.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/admin/users?type=" + type;
    }

}


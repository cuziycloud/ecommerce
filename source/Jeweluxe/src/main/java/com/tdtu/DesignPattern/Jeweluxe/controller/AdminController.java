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
import com.tdtu.DesignPattern.Jeweluxe.command.*;

import com.tdtu.DesignPattern.Jeweluxe.template.user.AdminCreator;
import com.tdtu.DesignPattern.Jeweluxe.template.user.CustomerCreator;

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


    private final AdminCreator adminCreator;
    private final CustomerCreator customerCreator;

    @Autowired
    public AdminController(OrderService orderService, AdminCreator adminCreator, CustomerCreator customerCreator) {
        this.orderService = orderService;
        this.adminCreator = adminCreator;
        this.customerCreator = customerCreator;
        System.out.println("Creating AdminController instance: " + this.hashCode());
         System.out.println("Injecting OrderService instance: " + orderService.hashCode());
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        List<Category> allActiveCategory = null;
        try {
            allActiveCategory = categoryService.getAllActiveCategory();
        } catch (Exception e) {
            System.err.println("Error fetching active categories in getUserDetails: " + e.getMessage());
            allActiveCategory = List.of();
        }
        m.addAttribute("categorys", allActiveCategory);

        if (p != null) {
            String email = p.getName();
            User userDtls = null;
            try {
                userDtls = userService.getUserByEmail(email); // thtin  user
            } catch (Exception e) {
                System.err.println("Error fetching user by email '" + email + "' in getUserDetails: " + e.getMessage());
            }

            if (userDtls != null) {
                m.addAttribute("user", userDtls);
                try {
                    Integer countCart = cartService.getCountCart(userDtls.getId());
                    m.addAttribute("countCart", countCart);
                } catch (Exception e) {
                    System.err.println("Error getting cart count for user ID " + userDtls.getId() + ": " + e.getMessage());
                    m.addAttribute("countCart", 0);
                }
            } else {
                System.err.println("Warning in getUserDetails: Principal found ('" + email + "'), but userService.getUserByEmail returned null.");
                m.addAttribute("user", null);
                m.addAttribute("countCart", 0);
            }

        } else {
            // chưa đăng nhập
            m.addAttribute("user", null);
            m.addAttribute("countCart", 0);
        }
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
    public String editProduct(@PathVariable int id,
                              @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
                              @RequestParam(name = "ch", required = false, defaultValue = "") String ch,
                              Model m) {
        m.addAttribute("product", productService.getProductById(id));
        m.addAttribute("categories", categoryService.getAllCategory());
        m.addAttribute("pageNo", pageNo);
        m.addAttribute("searchQuery", ch);

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


    @PostMapping("/create-customer")
    public String createCustomer(
            @RequestParam String name, @RequestParam String email, @RequestParam String password,
            @RequestParam(required = false) String mobileNumber, @RequestParam(required = false) String address,
            @RequestParam(required = false) String city, @RequestParam(required = false) String state, @RequestParam(required = false) String pincode,
            @RequestParam(defaultValue = "true") Boolean isEnable, @RequestParam("img") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        boolean success = customerCreator.processUserCreation(
                name, email, password, mobileNumber, address, city, state, pincode,
                isEnable, file, redirectAttributes, commonUtil
        );
        if (!success) { return "redirect:/admin/add-user"; }
        return "redirect:/admin/users?type=1";
    }

    @PostMapping("/create-admin")
    public String createAdmin(
            @RequestParam String name, @RequestParam String email, @RequestParam String password,
            @RequestParam(required = false) String mobileNumber, @RequestParam(required = false) String address,
            @RequestParam(required = false) String city, @RequestParam(required = false) String state, @RequestParam(required = false) String pincode,
            @RequestParam(defaultValue = "true") Boolean isEnable, // isEnable này có thể bị override bởi AdminCreator
            @RequestParam("img") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        boolean success = adminCreator.processUserCreation(
                name, email, password, mobileNumber, address, city, state, pincode,
                isEnable, file, redirectAttributes, commonUtil
        );
        if (!success) { return "redirect:/admin/add-admin"; }
        return "redirect:/admin/users?type=2";
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


    @PostMapping("/ship-order")
    public String shipOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        // 1. Tạo Command Object
        OrderCommand shipCommand = new ShipOrderCommand(orderService, id);

        try {
            // 2. Command
            shipCommand.execute();

            // 3. Thành công
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được cập nhật trạng thái gửi đi.");

        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi khi cập nhật trạng thái gửi đi: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống không mong muốn khi gửi hàng.");
            e.printStackTrace();
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/cancel-order")
    public String cancelOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        // 1. Tạo Command Object
        OrderCommand cancelCommand = new CancelOrderCommand(orderService, id);

        try {
            // 2. Command
            cancelCommand.execute();

            // 3. Thành công
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được hủy thành công.");

        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi khi hủy đơn hàng: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống khi hủy đơn hàng.");
            e.printStackTrace();
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/pack-order") // URL  đóng gói đơn hàng
    public String packOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        // 1. TẠO COMMAND OBJECT
        OrderCommand packCommand = new PackOrderCommand(orderService, id);

        try {
            // 2. COMMAND
            packCommand.execute();

            // 3. Thành công
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được cập nhật trạng thái đóng gói.");

        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi khi đóng gói: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống khi đóng gói đơn.");
            e.printStackTrace();
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/deliver-order") // URL  xác nhận giao hàng
    public String deliverOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        // 1. Tạo Command Object
        OrderCommand deliverCommand = new DeliverOrderCommand(orderService, id);

        try {
            // 2. Command
            deliverCommand.execute();

            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được cập nhật trạng thái đã giao.");

        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi khi xác nhận giao hàng: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi hệ thống khi xác nhận giao hàng.");
            e.printStackTrace();
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/receive-order") // URL  xác nhận nhận đơn hàng
    public String receiveOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        // 1. TẠO COMMAND OBJECT
        OrderCommand receiveCommand = new ReceiveOrderCommand(orderService, id);

        try {
            // 2. COMMAND
            receiveCommand.execute();

            // 3.
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng ID " + id + " đã được xác nhận nhận.");

        } catch (EntityNotFoundException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi khi nhận đơn: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi hệ thống khi nhận đơn hàng.");
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

    @GetMapping("/add-admin")
    public String loadAddAdminForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("formTitle", "Add New Admin");
        model.addAttribute("isAdminForm", true);
        model.addAttribute("userType", 2);
        return "admin/add_user";
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

    @GetMapping("/add-user")
    public String loadAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("formTitle", "Add New Customer");
        model.addAttribute("isAdminForm", false);
        model.addAttribute("userType", 1);
        return "admin/add_user";
    }



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
        model.addAttribute("userType", type);
        return "admin/edit_user";
    }

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
                if (newPassword.length() >= 6) { // Độ dài pwd
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


    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Integer id, @RequestParam Integer type, HttpSession session) {
        try {
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


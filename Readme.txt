JEWELUXE - Hướng dẫn cài đặt và chạy dự án


YÊU CẦU MÔI TRƯỜNG
---------------------
1.  JDK 21 hoặc cao hơn.
2.  Apache Maven 3.6+ (Để build dự án).
3.  MySQL Server 8.x.
4.  Công cụ quản lý MySQL: phpMyAdmin (thông qua XAMPP)
5.  IDE: IntelliJ IDEA (hoặc IDE khác hỗ trợ Maven + Spring Boot)
 

HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY DỰ ÁN
---------------------------------
1. Thiết lập Cơ sở dữ liệu MySQL:
Sử dụng công cụ phpMyAdmin:
- Tạo một database mới với tên là "jeweluxe". Sử dụng bảng mã là "utf8mb4_unicode_ci"
- Chọn database jeweluxe vừa tạo. Import file "jeweluxe.sql" (nằm trong thư mục gốc source).

2. Cấu hình ứng dụng (application.properties):
- Mở file "src/main/resources/application.properties".
- Đảm bảo "spring.datasource.url" trỏ đúng đến server MySQL của máy (của bài là "localhost:3306").
- Quan trọng: Thay đổi "spring.datasource.username" và "spring.datasource.password" cho phù hợp với tài khoản MySQL trên máy cá nhân (nếu có). Nếu MySQL không có mật khẩu thì không cần chỉnh sửa gì.
Lưu ý: Nếu port 8080 đã được sử dụng, vui lòng thay đổi giá trị server.port (DÒNG SỐ 3) trong application.properties sang port khác (ví dụ 8081).


3. Build và Chạy ứng dụng:
a. Khởi động XAMPP Control Panel (bắt buộc)
- Đảm bảo 1: Module Apache (truy cập phpMyAdmin qua trình duyệt) và Module MySQL đều đang ở trạng thái "Running" (hiện màu xanh lá). 
- Đảm bảo 2: Đã tồn tại CSDL tên "jeweluxe" (thực hiên import file sql trước đó).

b. Sử dụng IDE (IntelliJ IDEA):
- Truy cập thư mục source, ta thấy có thư mục Jeweluxe, chuột phải và chọn "Open folder as IntelliJ IDEA project"
- Tìm file "src/main/java/com/tdtu/DesignPattern/Jeweluxe/JeweluxeApplication.java".
- Chuột phải vào file này và chọn "Run 'JeweluxeApplication....main()'".
- Theo dõi cửa sổ Console/Terminal. Nếu thấy các dòng log tương tự như sau, ứng dụng đã khởi động thành công:
    ...
    ... INFO ... Tomcat started on port 8080 (http) with context path '/' ...
    ... INFO ... Started JeweluxeApplication in X.XXX seconds ...

Note: có thể xảy ra xung đột của Spring Boot DevTools trong lần đầu khởi động, lúc này bạn có thể dừng hoàn toàn project (restart) và tiến hành Build -> ReBuild Project để ổn định/ nhất quán lại


4. Truy cập ứng dụng:
- Mở trình duyệt web và truy cập: "http://localhost:8080/" (nếu cổng 8080 đang bận hoặc đã thay đổi cổng trong file application.properties trước đó, hãy đổi lại đường dẫn với cổng tương ứng)
=> Kết quả hiển thị ra là giao diện home trang web cửa hàng (chưa đăng nhập)
Tùy chọn: Tiến hành đăng nhập hoặc đăng ký tài khoản mới

---------------------------------------------------------------
Thông tin đăng nhập đã được tạo trước đó (đã có khi import sql)
1. Tài khoản Quản trị (Admin):
- Email: tranmyvan6157@gmail.com
- Mật khẩu: test123

2. Tài khoản Người dùng:
- Email: ayjssi0109@gmail.com
- Mật khẩu: test123


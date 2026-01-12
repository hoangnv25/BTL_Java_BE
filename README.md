# BTL Java Backend
Dự án gồm 2 repo, file readme viết chung cho cả dự án. 

Nhóm tác giả:
- Vũ Anh Tuấn
- Đinh Việt Dũng
- Nguyễn Vĩnh Trung
- Nguyễn Vĩnh Tùng
- Nguyễn Văn Hoàng

## Mục lục

1. [Giới thiệu dự án](#1-giới-thiệu-dự-án)
2. [Thiết kế Database](#2-thiết-kế-database)
3. [Back End](#3-back-end)
   - [3.1 Công nghệ](#31-công-nghệ)
   - [3.2 API](#32-api)
4. [Front End](#4-front-end)
5. [Hướng dẫn cài đặt và chạy dự án](#5-hướng-dẫn-cài-đặt-và-chạy-dự-án)


---

## 1. Giới thiệu dự án

### FASHCO - Nền Tảng Thương Mại Điện Tử Thời Trang

FASHCO là website bán quần áo trực tuyến dành cho giới trẻ với kiến trúc tách biệt Frontend và Backend, tập trung vào trải nghiệm mua sắm tiện lợi và quy trình quản lý tối ưu.

### 📖 Tổng quan hệ thống

Hệ thống cung cấp quy trình khép kín từ tìm kiếm sản phẩm, đặt hàng, thanh toán online đến theo dõi vận đơn và chăm sóc khách hàng.

**Các phân hệ chính:**
- **Client**: Giao diện thân thiện, responsive cho khách hàng
- **Admin**: Dashboard quản lý tập trung toàn bộ hệ thống
- **Server & Database**: Xử lý nghiệp vụ logic và lưu trữ dữ liệu

### 🚀 Tính năng nổi bật

#### 🛒 Dành cho Khách hàng
- **Tài khoản & Bảo mật**: Đăng ký/Đăng nhập (OAuth2/Google) và quản lý hồ sơ
- **Mua sắm**: Tìm kiếm, lọc đa tiêu chí, xem biến thể (màu sắc, kích thước), quản lý giỏ hàng
- **Thanh toán**: Thanh toán trực tuyến qua VNPay và theo dõi đơn hàng real-time
- **Chat**: Hệ thống chat real-time với Admin

#### 🛠 Dành cho Quản trị viên
- **Dashboard**: Báo cáo doanh thu, đơn hàng và hiệu suất kinh doanh
- **Quản lý sản phẩm**: Danh mục, sản phẩm và biến thể (SKU, tồn kho)
- **Quản lý vận hành**: Xử lý đơn hàng, người dùng và các đợt khuyến mãi

### 🏗 Kiến trúc & Công nghệ

**Backend:**
- **Framework**: Spring Ecosystem (RESTful API)
- **Security**: JWT (JSON Web Token)
- **Communication**: WebSocket (Real-time Chat)
- **Integrations**: Cloudinary (lưu trữ), VNPay (thanh toán)

**Frontend:**
- **Framework**: React (SPA)
- **UI/UX**: Material UI/Ant Design
- **State Management**: Quản lý trạng thái và API calls

**Database:**
- **RDBMS**: MySQL
- **ORM**: JPA/Hibernate

### 🌐 Triển khai

- **Frontend**: Vercel
- **Backend & Database**: Railway (CI/CD tự động)

## 2. Thiết kế Database

![ahd](/readmeIMG/BTL_JAVA_ER.png)
## 3. Back End

### 3.1 Công nghệ

**Framework & Core:**
- Spring Boot (RESTful API)
- Spring Security với JWT (JSON Web Token) cho xác thực và phân quyền
- OAuth2/Google Login cho đăng nhập nhanh
- WebSocket/Socket.IO cho Chat real-time

**Tích hợp dịch vụ:**
- **Cloudinary**: Upload và lưu trữ ảnh, trả về URL để lưu trong Database
- **VNPay Sandbox**: Tích hợp cổng thanh toán trực tuyến (môi trường test)

**Database & ORM:**
- MySQL với JPA/Hibernate
- CRUD tiêu chuẩn cho tất cả các entity

### 3.2 API

**Authentication & Authorization:**
- `POST /auth/token` - Đăng nhập (Admin/User)
- `POST /auth/introspect` - Xác thực token
- `POST /auth/refresh` - Làm mới token
- `POST /auth/logout` - Đăng xuất
- `GET /users` - Lấy danh sách users
- `POST /user` - Tạo user mới
- `PUT /users/{id}` - Cập nhật user
- `GET /users/myInfor` - Lấy thông tin cá nhân
- `POST /permissions` - Tạo permission
- `GET /permissions` - Lấy danh sách permissions
- `POST /roles` - Tạo role
- `DELETE /roles/{name}` - Xóa role

**Category:**
- `GET /category` - Lấy danh sách category
- `POST /category` - Tạo category
- `PUT /category/{id}` - Cập nhật category
- `DELETE /category/{id}` - Xóa category

**Product:**
- `GET /products` - Lấy danh sách sản phẩm
- `POST /products` - Tạo sản phẩm
- `PUT /products/{id}` - Cập nhật sản phẩm
- `GET /products/search` - Tìm kiếm sản phẩm (keyword, price range, colors)

**Product Variation:**
- `POST /variations` - Tạo biến thể sản phẩm (size, color, stock)
- `GET /variations/{id}` - Lấy thông tin biến thể
- `PUT /variations/{id}` - Cập nhật biến thể

**Reviews:**
- `POST /reviews` - Tạo đánh giá
- `GET /reviews` - Lấy tất cả đánh giá
- `PUT /reviews/{id}` - Cập nhật đánh giá
- `DELETE /reviews/{id}` - Xóa đánh giá
- `GET /reviews/user/{userId}` - Lấy đánh giá theo user
- `GET /reviews/rating/{rating}` - Lấy đánh giá theo rating
- `GET /reviews/rating/min/{minRating}` - Lấy đánh giá từ rating tối thiểu

**Cart:**
- `POST /cart/add` - Thêm sản phẩm vào giỏ hàng
- `GET /cart` - Lấy giỏ hàng
- `PUT /cart/update/{id}` - Cập nhật số lượng
- `DELETE /cart/remove/{id}` - Xóa sản phẩm khỏi giỏ hàng

**Order:**
- `POST /orders` - Tạo đơn hàng
- `GET /orders` - Lấy đơn hàng của user
- `GET /all-orders` - Lấy tất cả đơn hàng (Admin)
- `PATCH /orders/{id}/cancel` - Hủy đơn hàng
- `PATCH /orders/{id}/status` - Cập nhật trạng thái đơn hàng
- `DELETE /orders/{id}` - Xóa đơn hàng

**Feedback:**
- `GET /feedback/{orderId}` - Lấy feedback theo đơn hàng
- `POST /feedback/{orderId}` - Tạo feedback
- `DELETE /feedback/{orderId}/{feedbackId}` - Xóa feedback

**Sales:**
- `POST /sales` - Tạo đợt khuyến mãi
- `PUT /sales/{id}` - Cập nhật khuyến mãi
- `GET /sales` - Lấy danh sách khuyến mãi
- `DELETE /sales/{id}` - Xóa khuyến mãi

**Address:**
- `POST /address` - Tạo địa chỉ
- `PUT /address/{id}` - Cập nhật địa chỉ
- `GET /address` - Lấy danh sách địa chỉ
- `GET /address/default` - Lấy địa chỉ mặc định

**Chat:**
- `POST /chat/conversations/ensure` - Tạo/đảm bảo conversation
- `POST /chat/messages` - Gửi tin nhắn
- `GET /chat/conversations` - Lấy danh sách conversation
- `GET /chat/conversations/{id}` - Lấy chi tiết conversation


## 4. Front End
Xem tại [Github](https://github.com/hoangnv25/BTL_Java_FE)

## 5. Hướng dẫn cài đặt và chạy dự án

### 📋 Yêu cầu hệ thống

- **Java**: JDK 8 trở lên
- **MySQL**: MySQL Server 8.0 trở lên
- **Node.js**: 16.x trở lên (cho Frontend)
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **Postman**: Để test API (tùy chọn)

### Bước 0: Clone source code
```bash
git clone https://github.com/hoangnv25/BTL_Java_BE.git
```

Hoặc tải file ZIP từ GitHub và giải nén.

### 🗄️ Bước 1: Cài đặt Database

1. **Tải và cài đặt MySQL Workbench**
   - Tải từ [MySQL Official Website](https://dev.mysql.com/downloads/workbench/)

2. **Tạo kết nối MySQL**
   - Mở MySQL Workbench
   - Tạo một kết nối mặc định với MySQL Server

3. **Import Database**
   - Tìm file `DB_JAVA.sql` ở thư mục gốc của dự án
   - Mở file `DB_JAVA.sql` trên MySQL Workbench
   - Chạy toàn bộ script SQL
   - Click **Reload** và kiểm tra xem vừa có Schema mới tên là `java_db` vừa được tạo với rất nhiều bảng và dữ liệu trong bảng chưa.

### ⚙️ Bước 2: Cài đặt Backend

#### 2.1. Cấu hình Backend
Mở dự án trong 1 IDE (IntelliJ, Netbeans...)
**Lưu ý**: File `application.properties` chứa thông tin cấu hình theo máy (DB username/password) nên **không được commit lên GitHub** (đã được thêm vào `.gitignore`). Bạn cần tự tạo file này.

**Tạo file cấu hình:**

Tạo file tại đường dẫn: `BTL/src/main/resources/application.properties`

```properties
server.port=8080

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/java_db
spring.datasource.username=root
spring.datasource.password=12345
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===================== JWT =====================
jwt.signerKey=sigerKey_cua_toi
jwt.valid-duration=3600 
jwt.refresh-duration=36000 
TZ="Asia/Ho_Chi_Minh"


# ===================== outbound (không dùng tới tính năng nhưng phải có để đọc) =====================
outbound.identity.google.client-id="random-oogpvtn4a.apps.googleusercontent.com"
outbound.identity.google.client-secret="randomGOPX-fri3"
outbound.identity.redirect-uri="http://localhost:5173/auth/OAuth"

# ===================== more =====================
spring.sql.init.mode=never
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```

**Thay thế các giá trị:**
- `java_db`: Tên schema đã tạo khi chạy scrip trên workbench hồi nãy, đừng sửa cái này nếu nãy không sửa
- `<db_user>`: Username MySQL của bạn
- `<db_pass>`: Password MySQL của bạn

#### 2.3. Chạy Backend

1. Tìm đến file: `BTL/src/main/java/com/BTL_JAVA/BTL/BtlApplication.java`
2. Chạy file `BtlApplication.java` (Run/Debug)
3. Backend sẽ chạy tại: [http://localhost:8080](http://localhost:8080)
4. Kiểm tra bằng cách truy cập link [http://localhost:8080/products](http://localhost:8080/products), nếu trả về rất nhiều dòng json dữ liệu thì thành công, nếu chưa thì hãy kiểm tra lại.

### 🧪 Bước 3: Kiểm tra API với Postman (bỏ qua nếu không thích)
Bước này để kiểm tra xem hệ thống bao gồm BE và DB đã hoạt động trơn tru chưa, có nhiều cách kiểm tra, dưới đây là dùng Postman. 
Hướng dẫn dưới dây sẽ import tất cả API mà chúng tôi có lên Postman để kiểm thử.

#### 3.1. Import Collection vào Postman

1. **Tải Postman** (nếu chưa có)
   - Tải từ [Postman Official Website](https://www.postman.com/downloads/)

2. **Import API Collection**
   - Tìm file `BTL_JAVA.postman_collection.json` ở thư mục gốc của dự án
   - Mở Postman, chọn **Import**
   - Import file `BTL_JAVA.postman_collection.json`
   - Bạn sẽ thấy một folder 2 cấp với các API đã được tổ chức

#### 3.2. Test API

1. Chạy thử API **Đăng nhập Admin**
2. Nếu trả về response thành công như sau:

```json
{
    "code": 0,
    "result": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "authenticated": true,
        "role": null
    }
}
```

→ Hệ thống BE và DB đã chạy thành công! Bạn có thể sử dụng các API khác để truy cập CSDL.

### 🎨 Bước 4: Cài đặt Frontend

1. **Clone dự án Frontend**
   ```bash
   git clone https://github.com/hoangnv25/BTL_Java_FE.git
   ```
   Hoặc tải file ZIP từ GitHub.

2. **Cài đặt dependencies**
   ```bash
   cd BTL_Java_FE
   npm install
   ```

3. **Chạy Frontend**
   ```bash
   npm run dev
   ```

4. Frontend sẽ chạy tại địa chỉ `http://localhost:5173`

### ✅ Hoàn tất

Sau khi hoàn thành tất cả các bước trên, bạn đã có:
- ✅ Database MySQL đã được import và sẵn sàng
- ✅ Backend API chạy tại `http://localhost:8080`
- ✅ Frontend chạy tại `http://localhost:5173` và có thể tương tác với Backend qua giao diện người dùng

### Tài khoản
- Quản trị: Tài khoản: `admin`, mật khẩu `admin`
- Người dùng: Tạo mới hoặc Tài khoản: `hoang`, mật khẩu `hoang`

## 6. Lưu ý

- **Báo cáo dự án**: file `Báo cáo BTL Java.pdf` trong thư mục chính của dự án phần Backend
- **Mục đích**: Dự án được phát triển phục vụ mục đích học tập, không vì mục đích thương mại
- **Tích hợp dịch vụ**: 
  - OAuth2 Google và VNPay yêu cầu cấu hình credentials/API keys riêng. Khi clone dự án về, bạn cần tự cấu hình các thông tin này trong file cấu hình để các tính năng này hoạt động
  - Tham khảo tài liệu của Google OAuth2 và VNPay Sandbox để thiết lập
- **Hình ảnh**: Các hình ảnh sản phẩm trong dự án được tham khảo từ các website thương mại điện tử, chỉ sử dụng cho mục đích học tập và demo

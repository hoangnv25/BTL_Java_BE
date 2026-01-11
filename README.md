# BTL Java Backend

## Mục lục

1. [Giới thiệu dự án](#1-giới-thiệu-dự-án)
2. [Chi tiết BE](#2-chi-tiết-be)
3. [Chi tiết FE](#3-chi-tiết-fe)
4. [Hướng dẫn cài đặt và chạy dự án](#4-hướng-dẫn-cài-đặt-và-chạy-dự-án)


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
- **Chat**: Hệ thống chat trực tuyến với Admin

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

## 2. Chi tiết BE

## 3. Chi tiết FE

## 4. Hướng dẫn cài đặt và chạy dự án

### 📋 Yêu cầu hệ thống

- **Java**: JDK 8 trở lên
- **MySQL**: MySQL Server 8.0 trở lên
- **Node.js**: 16.x trở lên (cho Frontend)
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **Postman**: Để test API (tùy chọn)

### 🗄️ Bước 1: Cài đặt Database

1. **Tải và cài đặt MySQL Workbench**
   - Tải từ [MySQL Official Website](https://dev.mysql.com/downloads/workbench/)

2. **Tạo kết nối MySQL**
   - Mở MySQL Workbench
   - Tạo một kết nối mặc định với MySQL Server

3. **Tạo Schema**
   - Tạo một schema mới (ví dụ: `btl_java`)

4. **Import Database**
   - Tìm file `DB_JAVA.sql` ở thư mục gốc của dự án
   - Mở file `DB_JAVA.sql` trên MySQL Workbench
   - Thêm dòng `USE btl_java;` lên trên cùng của file
   - Chạy toàn bộ script SQL
   - Click **Reload** và kiểm tra các bảng đã có dữ liệu

### ⚙️ Bước 2: Cài đặt Backend

#### 2.1. Clone source code

```bash
git clone https://github.com/hoangnv25/BTL_Java_BE.git
```

Hoặc tải file ZIP từ GitHub và giải nén.

#### 2.2. Cấu hình Backend

**Lưu ý**: File `application.properties` chứa thông tin cấu hình theo máy (DB username/password) nên **không được commit lên GitHub** (đã được thêm vào `.gitignore`). Bạn cần tự tạo file này.

**Tạo file cấu hình:**

Tạo file tại đường dẫn: `BTL/src/main/resources/application.properties`

```properties
server.port=8080

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/<db_name>
spring.datasource.username=<db_user>
spring.datasource.password=<db_pass>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Database sẽ được import thủ công bằng SQL Workbench (không auto init)
spring.sql.init.mode=never
spring.jpa.hibernate.ddl-auto=validate
```

**Thay thế các giá trị:**
- `<db_name>`: Tên schema đã tạo (ví dụ: `btl_java`)
- `<db_user>`: Username MySQL của bạn
- `<db_pass>`: Password MySQL của bạn

#### 2.3. Chạy Backend

1. Mở dự án trong IDE (IntelliJ IDEA / Eclipse)
2. Tìm đến file: `BTL/src/main/java/com/BTL_JAVA/BTL/BtlApplication.java`
3. Chạy file `BtlApplication.java` (Run/Debug)
4. Backend sẽ chạy tại: `http://localhost:8080`

### 🧪 Bước 3: Kiểm tra API với Postman 
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

Bạn có thể bắt đầu sử dụng hệ thống FASHCO!
